package com.campuslink.backend.domain.item.repository;

import com.campuslink.backend.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("""
            select i 
            from Item i
            where
                (:keyword is null or
                    lower(i.title) like lower(concat('%', :keyword, '%')) or
                    lower(i.description) like lower(concat('%', :keyword, '%')) or
                    lower(i.category) like lower(concat('%', :keyword, '%'))
                )
              and (:category is null or i.category = :category)
              and (:minPrice is null or i.price >= :minPrice)
              and (:maxPrice is null or i.price <= :maxPrice)
            """)
    Page<Item> search(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    Page<Item> findByUser_UserId(Integer userId, Pageable pageable);
}
