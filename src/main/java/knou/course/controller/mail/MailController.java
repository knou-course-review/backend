package knou.course.controller.mail;

import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.dto.mail.response.MailResponse;
import knou.course.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ApiResponse<MailResponse> createMail(@Valid @RequestBody MailCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return ApiResponse.ok(mailService.mailSend(request, now));
    }

    @PutMapping
    public ApiResponse<MailResponse> confirmMail(@Valid @RequestBody MailConfirmRequest request) {
        LocalDateTime now = LocalDateTime.now();

        return ApiResponse.ok(mailService.confirmMail(request, now));
    }
}
