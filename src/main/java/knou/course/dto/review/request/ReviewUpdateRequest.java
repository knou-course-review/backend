package knou.course.dto.review.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewUpdateRequest {

    @Schema(example = "강의 추천합니다.")
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public ReviewUpdateRequest(final String content) {
        this.content = content;
    }
}
