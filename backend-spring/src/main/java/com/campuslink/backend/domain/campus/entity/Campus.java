package com.campuslink.backend.domain.campus.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_id")
    private Integer campusId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "email_domain", length = 100)
    private String emailDomain;

    @Column(length = 255)
    private String address;
}
