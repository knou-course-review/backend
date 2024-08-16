package knou.course.service.report;

import jakarta.transaction.Transactional;
import knou.course.domain.report.Report;
import knou.course.domain.report.ReportRepository;
import knou.course.dto.report.request.ReportCreateRequest;
import knou.course.dto.report.response.ReportResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public ReportResponse createReport(ReportCreateRequest request, final Long targetId, final Long userId) {
        if (targetId.equals(userId)) {
            throw new AppException(ErrorCode.INVALID_SELF_REPORT, ErrorCode.INVALID_SELF_REPORT.getMessage());
        }

        if (reportRepository.existsByTargetIdAndUserId(targetId, userId)) {
            throw new AppException(ErrorCode.ALREADY_EXIST_REPORT, ErrorCode.ALREADY_EXIST_REPORT.getMessage());
        }

        Report savedReport = reportRepository.save(request.toEntity(userId, targetId));

        return ReportResponse.of(savedReport);
    }
}
