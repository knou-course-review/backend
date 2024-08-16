package knou.course.dto.like.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewLikeStatusResponse {

    @Schema(example = "1")
    private Long reviewId;

    @Schema(example = "3")
    private Long likeCount;

    @Schema(example = "true")
    private Boolean isLike;

    @Builder
    public ReviewLikeStatusResponse(final Long reviewId, final Long likeCount, final Boolean isLike) {
        this.reviewId = reviewId;
        this.likeCount = likeCount;
        this.isLike = isLike;
    }

    public static ReviewLikeStatusResponse of(final Long reviewId, final Long likeCount, final Boolean isLike) {
        return ReviewLikeStatusResponse.builder()
                .reviewId(reviewId)
                .likeCount(likeCount)
                .isLike(isLike)
                .build();
    }
}
