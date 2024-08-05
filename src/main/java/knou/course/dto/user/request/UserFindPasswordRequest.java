package knou.course.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserFindPasswordRequest {

    @Schema(example = "아이디")
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @Schema(example = "test@knou.ac.kr")
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[\\w._%+-]+@knou\\.ac\\.kr$", message = "이메일은 @knou.ac.kr 도메인이어야 합니다.")
    private String email;

    @Builder
    public UserFindPasswordRequest(final String username, final String email) {
        this.username = username;
        this.email = email;
    }
}
