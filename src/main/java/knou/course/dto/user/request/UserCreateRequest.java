package knou.course.dto.user.request;

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

    private String username;

    private String password;

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
