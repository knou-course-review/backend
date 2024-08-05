package knou.course.dto.mail.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.mail.MailHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "test@knou.ac.kr")
    private String email;

    @Schema(example = "139285")
    private int code;

    @Schema(example = "true, false")
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
