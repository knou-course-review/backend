package knou.course.service.review;

import knou.course.domain.course.Course;
import knou.course.domain.department.Department;
import knou.course.domain.professor.Professor;
import knou.course.domain.review.Review;
import knou.course.domain.review.ReviewRepository;
import knou.course.dto.course.response.CoursePagedResponse;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.response.ReviewOneResponse;
import knou.course.dto.review.response.ReviewPagedResponse;
import knou.course.dto.review.response.ReviewResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @DisplayName("등록된 리뷰를 페이징 조회한다. 고정 size - 10, Id 정렬까지 테스트")
    @Test
    void getAllReviewsPaged() {
        // given
        final Long userId = 1L;
        final Integer page = 1;
        Review review1 = createReview("내용1", 1L, 1L);
        Review review2 = createReview("내용2", 1L, 2L);
        Review review3 = createReview("내용3", 1L, 3L);
        reviewRepository.saveAll(List.of(review1, review2, review3));

        // when
        ReviewPagedResponse reviewPagedResponse = reviewService.getAllReviewsPaged(page, userId);

        // then
        assertThat(reviewPagedResponse.getContent()).hasSize(3)
                .extracting("userId", "content", "isOwner")
                .containsExactly(
                        tuple(3L, "내용3", false),
                        tuple(2L, "내용2", false),
                        tuple(1L, "내용1", true)
                );
    }

    @DisplayName("선택한 리뷰를 조회한다. (본인 작성 isOwner true)")
    @Test
    void getReviewByIdWithOwnerIsTrue() {
        // given
        final Long userId = 1L;
        Review review = createReview("내용1", 1L, 1L);
        reviewRepository.save(review);

        // when
        ReviewOneResponse reviewOneResponse = reviewService.getReviewById(review.getId(), userId);

        // then
        assertThat(reviewOneResponse.getId()).isNotNull();
        assertThat(reviewOneResponse)
                .extracting("userId", "content", "isOwner")
                .containsExactlyInAnyOrder(1L, "내용1", true);
    }

    @DisplayName("선택한 리뷰를 조회한다. (타인 작성 isOwner false)")
    @Test
    void getReviewByIdWithOwnerIsFalse() {
        // given
        final Long userId = 2L;
        Review review = createReview("내용1", 1L, 1L);
        reviewRepository.save(review);

        // when
        ReviewOneResponse reviewOneResponse = reviewService.getReviewById(review.getId(), userId);

        // then
        assertThat(reviewOneResponse.getId()).isNotNull();
        assertThat(reviewOneResponse)
                .extracting("userId", "content", "isOwner")
                .containsExactlyInAnyOrder(1L, "내용1", false);
    }

    private Review createReview(final String content, final Long courseId, final Long userId) {
        return Review.builder()
                .content(content)
                .courseId(courseId)
                .userId(userId)
                .build();
    }
}