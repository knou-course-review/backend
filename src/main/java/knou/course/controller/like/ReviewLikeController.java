package knou.course.controller.like;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import knou.course.dto.ApiResponse;
import knou.course.dto.like.response.ReviewLikeResponse;
import knou.course.dto.like.response.ReviewLikeStatusResponse;
import knou.course.service.like.ReviewLikeService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰에 좋아요를 취소합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_LIKE})
    @DeleteMapping("/api/v1/like/{reviewId}")
    public void deleteReviewLike(@PathVariable Long reviewId,
                                 Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        reviewLikeService.deleteReviewLike(reviewId, userId);
    }

    @Operation(summary = "리뷰 좋아요 개수, 여부 확인", description = "리뷰에 좋아요 개수와 좋아요 여부를 확인합니다. <br> Param 값으로 List<Long> reviewIds 값을 넘기면 됩니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE})
    @GetMapping("/api/v1/likes")
    public ApiResponse<List<ReviewLikeStatusResponse>> getReviewLikes(@RequestParam List<Long> reviewIds,
                                              Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        List<ReviewLikeStatusResponse> result = reviewLikeService.getLikeStatusByReviewIds(reviewIds, userId);
        return ApiResponse.ok(result);
    }
}
