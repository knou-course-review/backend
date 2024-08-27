package knou.course.dto.review.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewMyListResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "강의 추천합니다.")
    private String content;

    @Schema(example = "홍길동")
    private String username;

    @Schema(example = "13")
    private Long courseId;

    @Schema(example = "이산수학")
    private String courseName;

    @Schema(example = "작성자 본인 여부 - true, false")
    private boolean isOwner;

    @Schema(example = "2024-08-08 13:11:21.122801")
    private LocalDateTime createdAt;

    @Builder
    public ReviewMyListResponse(final Long id, final Long userId, final String content, final String username, final Long courseId, final String courseName, final boolean isOwner, final LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.username = username;
        this.courseId = courseId;
        this.courseName = courseName;
        this.isOwner = isOwner;
        this.createdAt = createdAt;
    }

    public static ReviewMyListResponse of(Review review, Map<Long, String> usernameMap, final Long userId, Map<Long, String> courseNameMap) {
        return ReviewMyListResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .content(review.getContent())
                .username(usernameMap.get(review.getUserId()))
                .courseId(review.getCourseId())
                .courseName(courseNameMap.get(review.getCourseId()))
                .isOwner(review.getUserId().equals(userId))
                .createdAt(review.getCreatedAt())
                .build();
    }
}
