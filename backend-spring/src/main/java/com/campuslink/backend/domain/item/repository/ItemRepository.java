package com.campuslink.backend.domain.item.repository;

import com.campuslink.backend.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT i FROM Item i WHERE i.title LIKE %:keyword% OR i.description LIKE %:keyword%")
    List<Item> searchByKeyword(@Param("keyword") String keyword);
}
