package knou.course.controller.mail;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.dto.mail.response.MailResponse;
import knou.course.service.mail.MailService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static knou.course.exception.ErrorCode.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@RestController
public class MailController {

    private final MailService mailService;

    @Operation(summary = "메일 인증번호 전송", description = "입력한 메일로 인증번호를 전송합니다.")
    @ApiErrorCodeExamples({MAIL_ERROR, INVALID_INPUT_VALUE})
    @PostMapping
    public ApiResponse<MailResponse> createMail(@Valid @RequestBody MailCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.ok(mailService.mailSend(request, now));
    }

    @Operation(summary = "인증번호 확인", description = "인증번호를 확인합니다.")
    @ApiErrorCodeExamples({NOT_FOUND_EMAIL_AUTHENTICATION, EXPIRED_MAIL_AUTHENTICATION, INVALID_INPUT_VALUE})
    @PutMapping
    public ApiResponse<MailResponse> confirmMail(@Valid @RequestBody MailConfirmRequest request) {
        LocalDateTime now = LocalDateTime.now();

        return ApiResponse.ok(mailService.confirmMail(request, now));
    }
}
