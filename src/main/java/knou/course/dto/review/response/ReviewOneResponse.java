package knou.course.dto.review.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewOneResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "13")
    private Long courseId;

    @Schema(example = "이 강의 추천합니다.")
    private String content;

    @Schema(example = "true, false")
    private boolean isOwner;

    @Builder
    public ReviewOneResponse(final Long id, final Long userId, final Long courseId, final String content, final boolean isOwner) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.content = content;
        this.isOwner = isOwner;
    }

    public static ReviewOneResponse of(Review review, final Long userId) {
        return ReviewOneResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .courseId(review.getCourseId())
                .content(review.getContent())
                .isOwner(review.getUserId().equals(userId))
                .build();
    }
}
