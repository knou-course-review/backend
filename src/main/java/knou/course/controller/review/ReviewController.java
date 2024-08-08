package knou.course.controller.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.response.ReviewResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.review.ReviewService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static knou.course.exception.ErrorCode.INVALID_INPUT_VALUE;
import static knou.course.exception.ErrorCode.NOT_FOUND_USER;

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
}
