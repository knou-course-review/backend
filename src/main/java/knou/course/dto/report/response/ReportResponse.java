package knou.course.dto.report.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.report.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReportResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "2")
    private Long targetId;

    @Schema(example = "신고 사유")
    private String content;

    @Builder
    public ReportResponse(final Long id, final Long userId, final Long targetId, final String content) {
        this.id = id;
        this.userId = userId;
        this.targetId = targetId;
        this.content = content;
    }

    public static ReportResponse of(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .userId(report.getUserId())
                .targetId(report.getTargetId())
                .content(report.getContent())
                .build();
    }
}
