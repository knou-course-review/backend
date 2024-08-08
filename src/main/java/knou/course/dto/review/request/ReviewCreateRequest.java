package knou.course.dto.review.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import knou.course.domain.review.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewCreateRequest {

    @Schema(example = "강의 추천합니다.")
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Schema(example = "1")
    private Long courseId;

    @Builder
    public ReviewCreateRequest(final String content, final Long courseId) {
        this.content = content;
        this.courseId = courseId;
    }

    public Review toEntity(final Long userId) {
        return Review.builder()
                .userId(userId)
                .courseId(courseId)
                .content(content)
                .build();
    }
}
