package knou.course.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequest {

    @Schema(example = "아이디")
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @Schema(example = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(example = "test@knou.ac.kr")
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[\\w._%+-]+@knou\\.ac\\.kr$", message = "이메일은 @knou.ac.kr 도메인이어야 합니다.")
    private String email;

    @Builder
    public UserCreateRequest(final String username, final String password, final String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User toEntity(String password) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
    }
}
