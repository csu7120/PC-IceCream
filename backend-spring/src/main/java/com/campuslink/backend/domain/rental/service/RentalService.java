package com.campuslink.backend.domain.rental.service;

import com.campuslink.backend.common.exception.BusinessException;
import com.campuslink.backend.common.exception.ErrorCode;
import com.campuslink.backend.domain.item.entity.Item;
import com.campuslink.backend.domain.item.repository.ItemRepository;
import com.campuslink.backend.domain.rental.dto.RentalRequest;
import com.campuslink.backend.domain.rental.dto.RentalResponse;
import com.campuslink.backend.domain.rental.entity.RentPolicy;
import com.campuslink.backend.domain.rental.entity.Rental;
import com.campuslink.backend.domain.rental.entity.RentalStatus;
import com.campuslink.backend.domain.rental.repository.RentPolicyRepository;
import com.campuslink.backend.domain.rental.repository.RentalRepository;
import com.campuslink.backend.domain.user.entity.User;
import com.campuslink.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentPolicyRepository rentPolicyRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public RentalResponse requestRental(String renterEmail, RentalRequest req) {
        User renter = userRepository.findByEmail(renterEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Item item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        if (!Boolean.TRUE.equals(item.getRentAvailable())) {
            throw new BusinessException(ErrorCode.ITEM_NOT_RENTABLE);
        }

        User lender = item.getUser();
        if (lender.getUserId().equals(renter.getUserId())) {
            throw new BusinessException(ErrorCode.RENT_SELF_NOT_ALLOWED);
        }

        if (req.endAt().isBefore(req.startAt())) {
            throw new BusinessException(ErrorCode.RENT_DATE_INVALID);
        }

        long days = Duration.between(req.startAt(), req.endAt()).toDays();
        if (days <= 0) days = 1;

        RentPolicy policy = rentPolicyRepository.findByItem_ItemId(item.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RENT_POLICY_NOT_FOUND));

        if (days < policy.getMinDays() || days > policy.getMaxDays()) {
            throw new BusinessException(ErrorCode.RENT_DAYS_OUT_OF_RANGE);
        }

        List<RentalStatus> busy = List.of(
                RentalStatus.REQUESTED,
                RentalStatus.ACCEPTED,
                RentalStatus.PICKED_UP,
                RentalStatus.IN_USE,
                RentalStatus.LATE
        );
        if (!rentalRepository.findByItem_ItemIdAndStatusIn(item.getItemId(), busy).isEmpty()) {
            throw new BusinessException(ErrorCode.RENT_ALREADY_IN_PROGRESS);
        }

        Rental rental = Rental.builder()
                .item(item)
                .lender(lender)
                .renter(renter)
                .startAt(req.startAt())
                .endAt(req.endAt())
                .dailyPrice(policy.getDefaultDailyPrice())
                .deposit(policy.getDefaultDeposit())
                .status(RentalStatus.REQUESTED)
                .build();

        Rental saved = rentalRepository.save(rental);
        return toResponse(saved);
    }

    @Transactional
    public RentalResponse acceptRental(String lenderEmail, Integer rentalId) {
        Rental rental = getRental(rentalId);

        if (!rental.getLender().getEmail().equals(lenderEmail)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (rental.getStatus() != RentalStatus.REQUESTED) {
            throw new BusinessException(ErrorCode.RENT_STATUS_INVALID);
        }

        rental.setStatus(RentalStatus.ACCEPTED);
        return toResponse(rental);
    }

    @Transactional
    public RentalResponse cancelRental(String userEmail, Integer rentalId) {
        Rental rental = getRental(rentalId);

        boolean isRenter = rental.getRenter().getEmail().equals(userEmail);
        boolean isLender = rental.getLender().getEmail().equals(userEmail);

        if (!isRenter && !isLender) throw new BusinessException(ErrorCode.FORBIDDEN);

        if (rental.getStatus() == RentalStatus.PICKED_UP ||
                rental.getStatus() == RentalStatus.IN_USE ||
                rental.getStatus() == RentalStatus.RETURNED) {
            throw new BusinessException(ErrorCode.RENT_CANNOT_CANCEL_NOW);
        }

        rental.setStatus(RentalStatus.CANCELLED);
        return toResponse(rental);
    }

    @Transactional
    public RentalResponse pickupRental(String userEmail, Integer rentalId) {
        Rental rental = getRental(rentalId);

        boolean isRenter = rental.getRenter().getEmail().equals(userEmail);
        boolean isLender = rental.getLender().getEmail().equals(userEmail);

        if (!isRenter && !isLender) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        if (rental.getStatus() != RentalStatus.ACCEPTED) {
            throw new BusinessException(ErrorCode.RENT_STATUS_INVALID);
        }

        rental.setPickedUpAt(java.time.LocalDateTime.now());
        rental.setStatus(RentalStatus.IN_USE);

        Item item = rental.getItem();
        item.setRentAvailable(false);
        item.setSaleAvailable(false);
        itemRepository.save(item);

        return toResponse(rental);
    }

    // ✅ 반납 API
    @Transactional
    public RentalResponse returnRental(String userEmail, Integer rentalId) {
        Rental rental = getRental(rentalId);

        boolean isRenter = rental.getRenter().getEmail().equals(userEmail);
        boolean isLender = rental.getLender().getEmail().equals(userEmail);

        if (!isRenter && !isLender) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        if (rental.getStatus() != RentalStatus.IN_USE &&
                rental.getStatus() != RentalStatus.LATE) {
            throw new BusinessException(ErrorCode.RENT_STATUS_INVALID);
        }

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        rental.setReturnedAt(now);

        long lateDays = 0;
        if (now.isAfter(rental.getEndAt())) {
            lateDays = java.time.temporal.ChronoUnit.DAYS.between(rental.getEndAt(), now);
            if (lateDays < 0) lateDays = 0;
        }

        if (lateDays > 0) {
            java.math.BigDecimal lateFee =
                    rental.getDailyPrice().multiply(java.math.BigDecimal.valueOf(lateDays));
            rental.setLateFee(lateFee);
            rental.setStatus(RentalStatus.LATE); // 참고용(아래에서 RETURNED로 종료됨)
        } else {
            rental.setLateFee(java.math.BigDecimal.ZERO);
        }

        rental.setStatus(RentalStatus.RETURNED);

        Item item = rental.getItem();
        item.setRentAvailable(true);
        item.setSaleAvailable(true);
        itemRepository.save(item);

        return toResponse(rental);
    }

    // ✅✅✅ 자동 연체 전환 메서드 (스케줄러가 호출)
    @Transactional
    public void markLateRentals() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // IN_USE인데 기한 지난 렌탈 전부 조회
        List<Rental> overdueList =
                rentalRepository.findByStatusAndEndAtBefore(RentalStatus.IN_USE, now);

        for (Rental rental : overdueList) {
            rental.setStatus(RentalStatus.LATE);
            // lateFee는 "반납 시" 계산하는 정책이므로 여기서는 안 만짐
        }
    }

    @Transactional(readOnly = true)
    public List<RentalResponse> myRentals(String renterEmail) {
        User renter = userRepository.findByEmail(renterEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return rentalRepository.findByRenter_UserId(renter.getUserId())
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<RentalResponse> myLendings(String lenderEmail) {
        User lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return rentalRepository.findByLender_UserId(lender.getUserId())
                .stream().map(this::toResponse).toList();
    }

    public Rental getRental(Integer rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RENTAL_NOT_FOUND));
    }

    private RentalResponse toResponse(Rental r) {
        return new RentalResponse(
                r.getRentalId(),
                r.getItem().getItemId(),
                r.getLender().getUserId(),
                r.getRenter().getUserId(),
                r.getStartAt(),
                r.getEndAt(),
                r.getDailyPrice(),
                r.getDeposit(),
                r.getStatus(),
                r.getPickedUpAt(),
                r.getReturnedAt(),
                r.getLateFee(),
                r.getCreatedAt()
        );
    }
}
