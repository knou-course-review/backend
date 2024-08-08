package knou.course.service.review;

import knou.course.domain.review.Review;
import knou.course.domain.review.ReviewRepository;
import knou.course.dto.review.request.ReviewCreateRequest;
import knou.course.dto.review.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponse createReview(final ReviewCreateRequest request, final Long userId) {
        Review savedReview = reviewRepository.save(request.toEntity(userId));

        return ReviewResponse.of(savedReview);
    }
}
