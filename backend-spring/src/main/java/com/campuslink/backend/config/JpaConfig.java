package com.campuslink.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // createdAt/updatedAt 자동 처리
public class JpaConfig { }
