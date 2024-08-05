package knou.course.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // Common
    OK(HttpStatus.OK, "응답 성공"),
    NOT_ADMIN(HttpStatus.FORBIDDEN, "ADMIN이 아닙니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    MAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "인증번호 서비스에 문제가 발생하였습니다. 고객센터 문의 바랍니다."),

    // User
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Email
    NOT_FOUND_EMAIL_AUTHENTICATION(HttpStatus.NOT_FOUND, "이메일 인증이 필요합니다."),
    EXPIRED_MAIL_AUTHENTICATION(HttpStatus.BAD_REQUEST, "인증 시간을 초과하였습니다."),

    // Department
    ALREADY_EXIST_DEPARTMENT_NAME(HttpStatus.CONFLICT, "이미 존재하는 학과명입니다."),
    NOT_FOUND_DEPARTMENT(HttpStatus.NOT_FOUND, "존재하지 않는 학과입니다."),

    // Professor;
    NOT_FOUND_PROFESSOR(HttpStatus.NOT_FOUND, "존재하지 않는 교수입니다."),

    // Course
    NOT_FOUND_COURSE(HttpStatus.NOT_FOUND, "존재하지 않는 강의입니다.");

    private final HttpStatus status;

    private final String message;
}
