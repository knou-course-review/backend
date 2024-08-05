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
public class UserChangePassword {

    @Schema(example = "test@knou.ac.kr")
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[\\w._%+-]+@knou\\.ac\\.kr$", message = "이메일은 @knou.ac.kr 도메인이어야 합니다.")
    private String email;

    @Schema(example = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(example = "비밀번호확인")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String rePassword;

    @Builder
    public UserChangePassword(final String email, final String password, final String rePassword) {
        this.email = email;
        this.password = password;
        this.rePassword = rePassword;
    }
}
