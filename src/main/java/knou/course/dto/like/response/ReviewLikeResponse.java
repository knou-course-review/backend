package knou.course.dto.like.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.like.ReviewLike;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewLikeResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "13")
    private Long userId;

    @Schema(example = "20")
    private Long reviewId;

    @Builder
    public ReviewLikeResponse(final Long id, final Long userId, final Long reviewId) {
        this.id = id;
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public static ReviewLikeResponse of(ReviewLike reviewLike) {
        return ReviewLikeResponse.builder()
                .id(reviewLike.getId())
                .userId(reviewLike.getUserId())
                .reviewId(reviewLike.getReviewId())
                .build();
    }
}
