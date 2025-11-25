package com.campuslink.backend.domain.rental.repository;

import com.campuslink.backend.domain.rental.entity.RentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentPolicyRepository extends JpaRepository<RentPolicy, Integer> {

    Optional<RentPolicy> findByItem_ItemId(Integer itemId);

    void deleteByItem_ItemId(Integer itemId);
}
