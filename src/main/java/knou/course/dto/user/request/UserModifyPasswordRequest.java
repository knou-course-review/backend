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
public class UserModifyPasswordRequest {

    @Schema(example = "현재 비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String nowPassword;

    @Schema(example = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(example = "비밀번호확인")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String rePassword;

    @Builder
    public UserModifyPasswordRequest(final String nowPassword, final String password, final String rePassword) {
        this.nowPassword = nowPassword;
        this.password = password;
        this.rePassword = rePassword;
    }
}
