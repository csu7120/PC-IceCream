package com.campuslink.backend.domain.campus.repository;

import com.campuslink.backend.domain.campus.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Integer> {
}
