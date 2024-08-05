package knou.course.dto.mail.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import knou.course.domain.mail.MailHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailCreateRequest {

    @Schema(example = "test@knou.ac.kr")
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[\\w._%+-]+@knou\\.ac\\.kr$", message = "이메일은 @knou.ac.kr 도메인이어야 합니다.")
    private String email;

    @Builder
    public MailCreateRequest(final String email) {
        this.email = email;
    }

    public MailHistory toEntity(final int code, final boolean confirm, final LocalDateTime registeredDateTime) {
        return MailHistory.builder()
                .email(email)
                .code(code)
                .confirm(confirm)
                .registeredDateTime(registeredDateTime)
                .build();
    }
}
