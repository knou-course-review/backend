package knou.course.dto.mail.request;

import knou.course.domain.mail.MailHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailCreateRequest {

    private String email;

    @Builder
    public MailCreateRequest(final String email) {
        this.email = email;
    }

    public MailHistory toEntity(final int code, final boolean confirm) {
        return MailHistory.builder()
                .email(email)
                .code(code)
                .confirm(confirm)
                .build();
    }
}