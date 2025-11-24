package com.campuslink.backend.domain.rental.repository;

import com.campuslink.backend.domain.rental.entity.Rental;
import com.campuslink.backend.domain.rental.entity.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByRenter_UserId(Integer renterId);
    List<Rental> findByLender_UserId(Integer lenderId);
    List<Rental> findByItem_ItemIdAndStatusIn(Integer itemId, List<RentalStatus> statuses);
    List<Rental> findByStatusAndEndAtBefore(RentalStatus status, LocalDateTime time);
}
