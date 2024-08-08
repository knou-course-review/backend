package knou.course.service.review;

import knou.course.domain.review.Review;
import knou.course.domain.review.ReviewRepository;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.request.ReviewUpdateRequest;
import knou.course.dto.review.response.ReviewListResponse;
import knou.course.dto.review.response.ReviewOneResponse;
import knou.course.dto.review.response.ReviewPagedResponse;
import knou.course.dto.review.response.ReviewResponse;
import knou.course.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static knou.course.exception.ErrorCode.NOT_AUTHORITY;
import static knou.course.exception.ErrorCode.NOT_FOUND_REVIEW;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse createReview(final ReviewCreateRequest request, final Long userId) {
        Review savedReview = reviewRepository.save(request.toEntity(userId));

        return ReviewResponse.of(savedReview);
    }

    public ReviewPagedResponse getAllReviewsPaged(Integer page, final Long userId) {
        if (page < 1) {
            page = 1;
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<Review> reviews = reviewRepository.findAll(pageRequest);

        Map<Long, String> usernameMap = findUsernamesBy(reviews.getContent());

        List<ReviewListResponse> result = reviews.getContent()
                .stream()
                .map(review -> ReviewListResponse.of(review, usernameMap, userId))
                .toList();

        return ReviewPagedResponse.of(result, reviews);
    }

    private Map<Long, String> findUsernamesBy(List<Review> reviews) {
        List<Long> usernameIds = reviews.stream()
                .map(Review::getUserId)
                .toList();
        List<User> users = userRepository.findAllById(usernameIds);

        return users.stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));
    }

    public ReviewOneResponse getReviewById(final Long reviewId, final Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(NOT_FOUND_REVIEW, NOT_FOUND_REVIEW.getMessage()));

        return ReviewOneResponse.of(review, userId);
    }

    @Transactional
    public ReviewResponse updateReview(final Long reviewId, final Long userId, final ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(NOT_FOUND_REVIEW, NOT_FOUND_REVIEW.getMessage()));

        if (!userId.equals(review.getUserId())) {
            throw new AppException(NOT_AUTHORITY, NOT_AUTHORITY.getMessage());
        }

        review.updateReview(request.getContent());

        return ReviewResponse.of(review);
    }

    @Transactional
    public void deleteReview(final Long reviewId, final Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(NOT_FOUND_REVIEW, NOT_FOUND_REVIEW.getMessage()));

        if (!userId.equals(review.getUserId())) {
            throw new AppException(NOT_AUTHORITY, NOT_AUTHORITY.getMessage());
        }

        reviewRepository.delete(review);
    }
}
