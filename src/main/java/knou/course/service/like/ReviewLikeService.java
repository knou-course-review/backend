package knou.course.service.like;

import knou.course.domain.like.ReviewLike;
import knou.course.domain.like.ReviewLikeRepository;
import knou.course.dto.like.response.ReviewLikeResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public ReviewLikeResponse createReviewLike(final Long reviewId, final Long userId) {
        if (isLiked(reviewId, userId)) {
            throw new AppException(ErrorCode.ALREADY_EXIST_LIKE, ErrorCode.ALREADY_EXIST_LIKE.getMessage());
        }

        ReviewLike savedReviewLike = reviewLikeRepository.save(ReviewLike.create(userId, reviewId));
        return ReviewLikeResponse.of(savedReviewLike);
    }

    private boolean isLiked(final Long reviewId, final Long userId) {
        return reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId);
    }
}
