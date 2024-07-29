package knou.course.dto.mail.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MailConfirmRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = "^[\\w._%+-]+@knou\\.ac\\.kr$", message = "이메일은 @knou.ac.kr 도메인이어야 합니다.")
    private String email;

    @Min(value = 100000, message = "인증번호는 최소 100,000 이상 입니다.")
    @Max(value = 999999, message = "인증번호는 최대 999,999 이하 입니다.")
    private int code;

    @Builder
    public MailConfirmRequest(final String email, final int code) {
        this.email = email;
        this.code = code;
    }

}
