package knou.course.service.report;

import knou.course.domain.report.ReportRepository;
import knou.course.dto.report.request.ReportCreateRequest;
import knou.course.dto.report.response.ReportResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @AfterEach
    void tearDown() {
        reportRepository.deleteAllInBatch();
    }

    @DisplayName("유저를 신고한다.")
    @Test
    void createReview() {
        // given
        final Long userId = 1L;
        final Long targetId = 2L;
        ReportCreateRequest request = ReportCreateRequest.builder()
                .content("신고내용")
                .build();

        // when
        ReportResponse reportResponse = reportService.createReport(request, userId, targetId);

        // then
        assertThat(reportResponse.getId()).isNotNull();
        System.out.println("reportResponse.getUserId() = " + reportResponse.getUserId());
        System.out.println("reportResponse.getTargetId() = " + reportResponse.getTargetId());
        assertThat(reportResponse)
                .extracting("userId", "targetId", "content")
                .containsExactlyInAnyOrder(1L, 2L, "신고내용");
    }

    @DisplayName("유저를 신고할 때, 자기자신을 신고할 수 없다.")
    @Test
    void createReviewNotSelfReport() {
        // given
        final Long userId = 1L;
        final Long targetId = 1L;
        ReportCreateRequest request = ReportCreateRequest.builder()
                .content("신고내용")
                .build();

        // when // then
        assertThatThrownBy(() -> reportService.createReport(request, userId, targetId))
                .isInstanceOf(AppException.class)
                .hasMessage("자기자신을 신고할 수 없습니다.");
    }
}