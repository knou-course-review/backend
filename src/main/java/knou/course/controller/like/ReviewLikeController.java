package knou.course.controller.like;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import knou.course.dto.ApiResponse;
import knou.course.dto.like.response.ReviewLikeResponse;
import knou.course.service.like.ReviewLikeService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static knou.course.exception.ErrorCode.*;

@Tag(name = "ReviewLike Controller - 리뷰 좋아요 컨트롤러", description = "리뷰에 좋아요, 좋아요 취소를 할 수 있습니다.")
@RequiredArgsConstructor
@RestController
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @Operation(summary = "리뷰 좋아요", description = "리뷰에 좋아요를 누릅니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, ALREADY_EXIST_LIKE})
    @PostMapping("/api/v1/like/{reviewId}")
    public ApiResponse<ReviewLikeResponse> createReviewLike(@PathVariable Long reviewId,
                                                            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reviewLikeService.createReviewLike(reviewId, userId));
    }

}
