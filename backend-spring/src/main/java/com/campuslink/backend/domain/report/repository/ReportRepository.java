package com.campuslink.backend.domain.report.repository;

import com.campuslink.backend.domain.report.dto.BlacklistUserResponse;
import com.campuslink.backend.domain.report.entity.Report;
import com.campuslink.backend.domain.report.entity.ReportStatus;
import com.campuslink.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByReporter(User reporter);

    List<Report> findByTargetUser(User targetUser);

    long countByTargetUserAndStatusIn(User targetUser, Collection<ReportStatus> statuses);

    @Query("""
        SELECT new com.campuslink.backend.domain.report.dto.BlacklistUserResponse(
            r.targetUser.id,
            r.targetUser.name,
            COUNT(r)
        )
        FROM Report r
        WHERE r.status IN :statuses
        GROUP BY r.targetUser.id, r.targetUser.name
        HAVING COUNT(r) >= :threshold
        """)
    List<BlacklistUserResponse> findBlacklistedUsers(
            @Param("statuses") Collection<ReportStatus> statuses,
            @Param("threshold") long threshold
    );
}
