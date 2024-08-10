package knou.course.controller.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.report.request.ReportCreateRequest;
import knou.course.dto.report.response.ReportResponse;
import knou.course.service.report.ReportService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static knou.course.exception.ErrorCode.*;

@Tag(name = "Report Controller - 신고 컨트롤러", description = "회원을 신고합니다.")
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "유저 신고", description = "유저를 신고합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_USER, INVALID_SELF_REPORT})
    @PostMapping("/api/v1/report/{targetId}")
    public ApiResponse<ReportResponse> createReport(@PathVariable Long targetId,
                                                    @Valid @RequestBody ReportCreateRequest request,
                                                    Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reportService.createReport(request, targetId, userId));
    }
}
