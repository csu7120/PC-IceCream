package com.campuslink.backend.domain.rental.repository;

import com.campuslink.backend.domain.rental.entity.RentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentPolicyRepository extends JpaRepository<RentPolicy, Integer> {

    // 아이템 삭제할 때 같이 지우려고 쓸 메서드
	void deleteByItem_ItemId(Integer itemId);
}
