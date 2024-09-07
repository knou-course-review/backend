package knou.course.dto.review.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewCountResponse {

    @Schema(example = "1")
    private Long courseId;

    @Schema(example = "3")
    private Long reviewCount;

    @Builder
    public ReviewCountResponse(final Long courseId, final Long reviewCount) {
        this.courseId = courseId;
        this.reviewCount = reviewCount;
    }

    public static ReviewCountResponse of(final Long courseId, final Long reviewCount) {
        return ReviewCountResponse.builder()
                .courseId(courseId)
                .reviewCount(reviewCount)
                .build();
    }
}
