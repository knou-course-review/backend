package knou.course.controller.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.request.ReviewUpdateRequest;
import knou.course.dto.review.response.ReviewOneResponse;
import knou.course.dto.review.response.ReviewPagedResponse;
import knou.course.dto.review.response.ReviewResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.review.ReviewService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static knou.course.exception.ErrorCode.*;

@Tag(name = "Review Controller - 리뷰 컨트롤러", description = "강의리뷰를 등록, 수정, 삭제, 조회합니다.")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_USER})
    @PostMapping("/api/v1/review")
    public ApiResponse<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request,
                                                    Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reviewService.createReview(request, userId));
    }

    @Operation(summary = "리뷰 페이징 조회 - size 10 고정", description = "리뷰를 페이징 조회합니다. <br> 게시글 정보는 data.content로 접근해주세요.")
    @GetMapping("/api/v2/reviews")
    public ApiResponse<ReviewPagedResponse> getAllReviewsPaged(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reviewService.getAllReviewsPaged(page, userId));
    }

    @Operation(summary = "리뷰 단건 조회", description = "선택한 리뷰를 조회합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_REVIEW})
    @GetMapping("/api/v1/review/{reviewId}")
    public ApiResponse<ReviewOneResponse> getReviewById(@PathVariable Long reviewId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reviewService.getReviewById(reviewId, userId));
    }

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_REVIEW, NOT_AUTHORITY})
    @PutMapping("/api/v1/review/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable Long reviewId,
                                                    Authentication authentication,
                                                    @Valid @RequestBody ReviewUpdateRequest request) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(reviewService.updateReview(reviewId, userId, request));
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_REVIEW, NOT_AUTHORITY})
    @DeleteMapping("/api/v1/review/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        reviewService.deleteReview(reviewId, userId);
    }
}
