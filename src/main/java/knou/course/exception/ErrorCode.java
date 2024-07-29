package knou.course.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    OK(HttpStatus.OK, "응답 성공"),
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),

    NOT_FOUND_EMAIL_AUTHENTICATION(HttpStatus.NOT_FOUND, "이메일 인증이 필요합니다.");

    private final HttpStatus status;

    private final String message;
}
