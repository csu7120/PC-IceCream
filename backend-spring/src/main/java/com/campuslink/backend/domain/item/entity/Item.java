package com.campuslink.backend.domain.item.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.campuslink.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "items")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    private String title;
    private String description;
    private Double price;

    // ✅ DB에 category 컬럼이 있어서 기존대로 유지
    private String category;

    // ✅ 판매 가능 여부 (DB sale_available)
    @Builder.Default
    @Column(name = "sale_available", nullable = false)
    private Boolean saleAvailable = true;

    // ✅ 대여 가능 여부 (DB rent_available)
    @Builder.Default  
    @Column(name = "rent_available", nullable = false)
    private Boolean rentAvailable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("item")
    private List<ItemImage> images;
    
}
