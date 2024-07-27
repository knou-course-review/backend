package knou.course.controller.mail;

import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
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
    public String createMail(@RequestBody MailCreateRequest request) {
        mailService.mailSend(request);

        return "Create Mail";
    }

    @PutMapping
    public String confirmMail(@RequestBody MailConfirmRequest request) {
        LocalDateTime now = LocalDateTime.now();
        mailService.confirmMail(request, now);

        return "Confirm Mail";
    }
}
