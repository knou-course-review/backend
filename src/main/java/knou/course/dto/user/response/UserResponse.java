package knou.course.dto.user.response;

import knou.course.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    @Builder
    public UserResponse(final Long id, final String username, final String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public static UserResponse of(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}