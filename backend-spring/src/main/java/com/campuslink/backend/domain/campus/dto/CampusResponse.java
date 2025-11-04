package com.campuslink.backend.domain.campus.dto;

import com.campuslink.backend.domain.campus.entity.Campus;

public record CampusResponse(
        Integer campusId,
        String name,
        String emailDomain,
        String address
) {
    public static CampusResponse from(Campus c) {
        return new CampusResponse(
                c.getCampusId(),
                c.getName(),
                c.getEmailDomain(),
                c.getAddress()
        );
    }
}
