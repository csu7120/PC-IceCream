package com.campuslink.backend.domain.item.repository;

import com.campuslink.backend.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemImageRepository extends JpaRepository<Item, Integer> {

    // 부분일치(대소문자 무시) 검색: title/description/category
    @Query("""
        SELECT i FROM Item i
        WHERE (:keyword IS NULL OR :keyword = '' 
               OR LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(i.category) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<Item> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
