package knou.course.dto.report.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import knou.course.domain.report.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReportCreateRequest {

    @Schema(example = "신고 사유")
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public ReportCreateRequest(final String content) {
        this.content = content;
    }

    public Report toEntity(final Long userId, final Long targetId) {
        return Report.builder()
                .content(content)
                .userId(userId)
                .targetId(targetId)
                .build();
    }
}
