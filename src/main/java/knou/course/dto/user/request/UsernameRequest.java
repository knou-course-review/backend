package knou.course.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UsernameRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @Builder
    public UsernameRequest(final String username) {
        this.username = username;
    }
}
