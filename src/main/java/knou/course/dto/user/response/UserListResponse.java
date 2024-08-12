package knou.course.dto.user.response;

import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserListResponse {

    private Long id;

    private String username;

    private String email;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public UserListResponse(final Long id, final String username, final String email, final Status status, final LocalDateTime createdAt, final LocalDateTime updatedAt, final LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static UserListResponse of(final User user) {
        return UserListResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}
