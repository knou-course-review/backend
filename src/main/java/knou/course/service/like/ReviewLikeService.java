package knou.course.service.like;

import knou.course.domain.like.ReviewLike;
import knou.course.domain.like.ReviewLikeRepository;
import knou.course.dto.like.response.ReviewLikeResponse;
import knou.course.dto.like.response.ReviewLikeStatusResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public void deleteReviewLike(final Long reviewId, final Long userId) {
        ReviewLike reviewLike = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_LIKE, ErrorCode.NOT_FOUND_LIKE.getMessage()));

        reviewLikeRepository.deleteById(reviewLike.getId());
    }

    public List<ReviewLikeStatusResponse> getLikeStatusByReviewIds(final List<Long> reviewIds, final Long userId) {
        Map<Long, Long> likeCountMap = getLikeCountMapByReviewId(reviewIds);
        Map<Long, Boolean> likeStatusMap = getLikeStatusMapByUserId(reviewIds, userId);

        return getReviewLikeStatusResponses(reviewIds, likeCountMap, likeStatusMap);
    }

    private List<ReviewLikeStatusResponse> getReviewLikeStatusResponses(final List<Long> reviewIds, final Map<Long, Long> likeCountMap, final Map<Long, Boolean> likeStatusMap) {
        List<ReviewLikeStatusResponse> responses = new ArrayList<>();
        for (Long reviewId : reviewIds) {
            Long likeCount = likeCountMap.getOrDefault(reviewId, 0L);
            Boolean isLike = likeStatusMap.getOrDefault(reviewId, false);

            responses.add(ReviewLikeStatusResponse.of(
                    reviewId, likeCount, isLike
            ));
        }
        return responses;
    }

    private Map<Long, Boolean> getLikeStatusMapByUserId(final List<Long> reviewIds, final Long userId) {
        List<Object[]> userLiked = reviewLikeRepository.getUserLikeStatus(reviewIds, userId);
        return createLikeStatusMapBy(userLiked);
    }

    private Map<Long, Long> getLikeCountMapByReviewId(final List<Long> reviewIds) {
        List<Object[]> result = reviewLikeRepository.countLikesByReviewIds(reviewIds);
        return createLikeCountMapBy(result);
    }

    private Map<Long, Boolean> createLikeStatusMapBy(final List<Object[]> userLiked) {
        Map<Long, Boolean> likeStatusMap = new HashMap<>();
        for (Object[] liked : userLiked) {
            Long reviewId = (Long) liked[0];
            Boolean isLike = (Boolean) liked[1];
            likeStatusMap.put(reviewId, isLike);
        }

        return likeStatusMap;
    }

    private Map<Long, Long> createLikeCountMapBy(final List<Object[]> result) {
        Map<Long, Long> likeCountMap = new HashMap<>();
        for (Object[] row : result) {
            Long reviewId = (Long) row[0];
            Long count = (Long) row[1];
            likeCountMap.put(reviewId, count);
        }

        return likeCountMap;
    }

    private boolean isLiked(final Long reviewId, final Long userId) {
        return reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId);
    }
}
