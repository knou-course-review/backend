package knou.course.service.review;

import knou.course.domain.review.ReviewRepository;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.response.ReviewResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAllInBatch();
    }

    @DisplayName("리뷰를 등록한다.")
    @Test
    void createReview() {
        // given
        final Long userId = 1L;
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .content("리뷰")
                .courseId(1L)
                .build();

        // when
        ReviewResponse reviewResponse = reviewService.createReview(request, userId);

        // then
        assertThat(reviewResponse.getId()).isNotNull();
        assertThat(reviewResponse)
                .extracting("userId", "content", "courseId")
                .containsExactlyInAnyOrder(
                        1L, "리뷰", 1L
                );
    }
}