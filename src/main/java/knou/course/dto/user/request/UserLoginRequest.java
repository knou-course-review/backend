package knou.course.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequest {

    private String username;

    private String password;

    @Builder
    public UserLoginRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
