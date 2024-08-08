package knou.course.dto.review.response;

import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewListResponse {

    private Long id;

    private Long userId;

    private String content;

    private String username;

    private Long courseId;

    private boolean isOwner;

    private LocalDateTime createdAt;

    @Builder
    public ReviewListResponse(final Long id, final Long userId, final String content, final String username, final Long courseId, final boolean isOwner, final LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.username = username;
        this.courseId = courseId;
        this.isOwner = isOwner;
        this.createdAt = createdAt;
    }

    public static ReviewListResponse of(Review review, Map<Long, String> usernameMap, final Long userId) {
        return ReviewListResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .content(review.getContent())
                .username(usernameMap.get(review.getUserId()))
                .courseId(review.getCourseId())
                .isOwner(review.getUserId().equals(userId))
                .createdAt(review.getCreatedAt())
                .build();
    }
}
