package knou.course.dto.review.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "1")
    private Long courseId;

    @Schema(example = "강의 추천합니다.")
    private String content;

    @Builder
    public ReviewResponse(final Long id, final Long userId, final Long courseId, final String content) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.content = content;
    }

    public static ReviewResponse of(final Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .courseId(review.getCourseId())
                .content(review.getContent())
                .build();
    }
}
