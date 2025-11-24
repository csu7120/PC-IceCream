package com.campuslink.backend.domain.rental.repository;

import com.campuslink.backend.domain.rental.entity.RentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentPolicyRepository extends JpaRepository<RentPolicy, Integer> {
    Optional<RentPolicy> findByItem_ItemId(Integer itemId);
}
