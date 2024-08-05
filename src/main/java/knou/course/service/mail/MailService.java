package knou.course.service.mail;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.dto.mail.response.MailResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static knou.course.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final MailClient mailClient;
    private final MailHistoryRepository mailHistoryRepository;

    @Transactional
    public MailResponse mailSend(final MailCreateRequest request, final LocalDateTime registeredDateTime) {
        int code = mailClient.createRandomNumber();
        boolean result = mailClient.sendMail(request.getEmail(), "회원가입 인증번호입니다.", String.valueOf(code));

        if (!result) {
            throw new AppException(MAIL_ERROR, MAIL_ERROR.getMessage());
        }

        MailHistory savedMailHistory = mailHistoryRepository.save(request.toEntity(code, false, registeredDateTime));
        return MailResponse.of(savedMailHistory);
    }

    @Transactional
    public MailResponse confirmMail(final MailConfirmRequest request, final LocalDateTime now) {
        MailHistory mailHistory = mailHistoryRepository.findByEmailAndCode(request.getEmail(), request.getCode())
                .orElseThrow(() -> new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage()));

        if (now.isAfter(mailHistory.getRegisteredDateTime().plusMinutes(5))) {
            throw new AppException(EXPIRED_MAIL_AUTHENTICATION, EXPIRED_MAIL_AUTHENTICATION.getMessage());
        }

        mailHistory.updateConfirm(true);

        return MailResponse.of(mailHistory);
    }

}
