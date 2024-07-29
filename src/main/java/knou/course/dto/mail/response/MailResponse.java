package knou.course.dto.mail.response;

import knou.course.domain.mail.MailHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailResponse {

    private Long id;

    private String email;

    private int code;

    private boolean confirm;

    @Builder
    public MailResponse(final Long id, final String email, final int code, final boolean confirm) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.confirm = confirm;
    }

    public static MailResponse of(final MailHistory mailHistory) {
        return MailResponse.builder()
                .id(mailHistory.getId())
                .email(mailHistory.getEmail())
                .code(mailHistory.getCode())
                .confirm(mailHistory.isConfirm())
                .build();
    }
}
