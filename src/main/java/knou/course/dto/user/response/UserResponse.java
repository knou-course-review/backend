package knou.course.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "아이디")
    private String username;

    @Schema(example = "test@knou.ac.kr")
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