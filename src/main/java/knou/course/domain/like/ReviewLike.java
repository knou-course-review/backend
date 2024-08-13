package knou.course.domain.like;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import knou.course.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long reviewId;

    @Builder
    public ReviewLike(final Long userId, final Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public static ReviewLike create(final Long userId, final Long reviewId) {
        return ReviewLike.builder()
                .userId(userId)
                .reviewId(reviewId)
                .build();
    }
}
