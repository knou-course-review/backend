package knou.course.service.mail;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.dto.mail.response.MailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final MailHistoryRepository mailHistoryRepository;

    public MailResponse mailSend(final MailCreateRequest request) {
        int code = createRandomNumber();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("회원가입 인증번호입니다.");
        message.setText(String.valueOf(code));
        mailSender.send(message);

        MailHistory savedMailHistory = mailHistoryRepository.save(request.toEntity(code, false));

        return MailResponse.of(savedMailHistory);
    }

    @Transactional
    public MailResponse confirmMail(final MailConfirmRequest request, final LocalDateTime now) {
        MailHistory mailHistory = mailHistoryRepository.findByEmailAndCode(request.getEmail(), request.getCode())
                .orElseThrow(() -> new IllegalArgumentException(""));

        if (now.isAfter(mailHistory.getCreatedAt().plusMinutes(5))) {
            throw new IllegalArgumentException("");
        }

        mailHistory.updateConfirm(true);

        return MailResponse.of(mailHistory);
    }

    private int createRandomNumber() {
        return 100000 + (int) (Math.random() * 900000);
    }
}
