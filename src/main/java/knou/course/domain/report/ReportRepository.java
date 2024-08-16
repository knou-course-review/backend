package knou.course.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByTargetIdAndUserId(Long targetId, Long userId);
}
