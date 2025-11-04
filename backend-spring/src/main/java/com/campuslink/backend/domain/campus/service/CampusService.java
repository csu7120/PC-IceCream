package com.campuslink.backend.domain.campus.service;

import com.campuslink.backend.domain.campus.dto.CampusResponse;
import com.campuslink.backend.domain.campus.entity.Campus;
import com.campuslink.backend.domain.campus.repository.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campuses;

    public List<CampusResponse> getAll() {
        return campuses.findAll()
                .stream()
                .map(CampusResponse::from)
                .toList();
    }

    public Campus getById(Integer campusId) {
        return campuses.findById(campusId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 캠퍼스입니다."));
    }

    public void validateEmailDomain(Integer campusId, String email) {
        Campus campus = getById(campusId);

        String domainFromEmail = email.substring(email.indexOf("@") + 1);
        String campusDomain = campus.getEmailDomain();

        if (campusDomain != null && !campusDomain.isBlank()) {
            if (!domainFromEmail.equalsIgnoreCase(campusDomain)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "해당 캠퍼스 이메일 도메인(" + campusDomain + ")과 일치하지 않는 이메일입니다."
                );
            }
        }
    }
}
