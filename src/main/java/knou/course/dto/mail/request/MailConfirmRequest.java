package knou.course.dto.mail.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailConfirmRequest {

    private String email;

    private int code;

    @Builder
    public MailConfirmRequest(final String email, final int code) {
        this.email = email;
        this.code = code;
    }

}
