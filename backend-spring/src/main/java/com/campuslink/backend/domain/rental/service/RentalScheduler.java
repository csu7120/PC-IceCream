package com.campuslink.backend.domain.rental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RentalScheduler {

    private final RentalService rentalService;
    
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void checkOverdueRentals() {
        rentalService.markLateRentals();
    }
}
