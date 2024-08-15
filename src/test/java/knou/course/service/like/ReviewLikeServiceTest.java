package knou.course.service.like;

import knou.course.domain.like.ReviewLike;
import knou.course.domain.like.ReviewLikeRepository;
import knou.course.dto.like.response.ReviewLikeResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewLikeServiceTest {

    @Autowired
    private ReviewLikeService reviewLikeService;

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @AfterEach
    void tearDown() {
        reviewLikeRepository.deleteAllInBatch();
    }

    @DisplayName("리뷰 좋아요")
    @Test
    void createReviewLike() {
        // given
        final Long reviewId = 1L;
        final Long userId = 2L;

        // when
        ReviewLikeResponse reviewLikeResponse = reviewLikeService.createReviewLike(reviewId, userId);

        // then
        assertThat(reviewLikeResponse.getId()).isNotNull();
        assertThat(reviewLikeResponse)
                .extracting("reviewId", "userId")
                .containsExactlyInAnyOrder(1L, 2L);
    }
    
    @DisplayName("리뷰 좋아요를 눌렀을 때, 이미 누른 상태면 예외가 발생한다.")
    @Test
    void createReviewLikeWithDuplication() {
        // given
        final Long reviewId = 1L;
        final Long userId = 2L;
        ReviewLike reviewLike = createReviewLike(reviewId, userId);
        reviewLikeRepository.save(reviewLike);

        // when // then
        assertThatThrownBy(() -> reviewLikeService.createReviewLike(reviewId, userId))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 좋아요를 눌렀습니다.");
    }

    @DisplayName("리뷰 좋아요 취소")
    @Test
    void deleteReviewLike() {
        // given
        final Long reviewId = 1L;
        final Long userId = 2L;
        ReviewLike reviewLike = createReviewLike(reviewId, userId);
        reviewLikeRepository.save(reviewLike);

        // when
        reviewLikeService.deleteReviewLike(reviewId, userId);

        // then
        assertThatThrownBy(() -> reviewLikeRepository.findById(reviewLike.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("리뷰 좋아요 취소를 할 때, 좋아요가 존재하지 않으면 예외가 발생한다.")
    @Test
    void deleteReviewLikeWithoutReviewLike() {
        // given
        final Long reviewId = 1L;
        final Long userId = 2L;

        // when // then
        assertThatThrownBy(() -> reviewLikeService.deleteReviewLike(reviewId, userId))
                .isInstanceOf(AppException.class)
                .hasMessage("좋아요가 존재하지 않습니다.");
    }
    
    private ReviewLike createReviewLike(final Long reviewId, final Long userId) {
        return ReviewLike.builder()
                .reviewId(reviewId)
                .userId(userId)
                .build();
    }

}