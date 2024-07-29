package knou.course.service.mail;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.dto.mail.response.MailResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    @MockBean
    private MailClient mailClient;

    @Autowired
    private MailHistoryRepository mailHistoryRepository;

    @AfterEach
    void tearDown() {
        mailHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final int code = 123456;
        final String email = "email@knou.ac.kr";
        MailCreateRequest request = MailCreateRequest.builder()
                .email(email)
                .build();

        BDDMockito.given(mailClient.sendMail(anyString(), anyString(), anyString()))
                .willReturn(true);

        BDDMockito.given(mailClient.createRandomNumber())
                .willReturn(code);

        // when
        MailResponse mailResponse = mailService.mailSend(request, now);

        // then
        assertThat(mailResponse.getId()).isNotNull();
        assertThat(mailResponse.getCode()).isEqualTo(code);
        assertThat(mailResponse.getEmail()).isEqualTo(email);
        assertThat(mailResponse.isConfirm()).isFalse();
    }

    @DisplayName("이메일 인증번호 확인")
    @Test
    void confirmMail() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final int code = 123456;
        final String email = "email@knou.ac.kr";

        MailHistory mailHistory = createMailHistory(email, code, false, now);
        mailHistoryRepository.save(mailHistory);

        MailConfirmRequest request = MailConfirmRequest.builder()
                .email(email)
                .code(code)
                .build();

        // when
        MailResponse mailResponse = mailService.confirmMail(request, now);

        // then
        assertThat(mailResponse.getId()).isNotNull();
        assertThat(mailResponse.getCode()).isEqualTo(code);
        assertThat(mailResponse.getEmail()).isEqualTo(email);
        assertThat(mailResponse.isConfirm()).isTrue();
    }

    @DisplayName("이메일 인증번호 5분이 지나면 예외가 발생한다.")
    @Test
    void confirmMailExpired5Minute() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final int code = 123456;
        final String email = "email@knou.ac.kr";

        MailHistory mailHistory = createMailHistory(email, code, false, now);
        mailHistoryRepository.save(mailHistory);

        MailConfirmRequest request = MailConfirmRequest.builder()
                .email(email)
                .code(code)
                .build();

        // when // then
        assertThatThrownBy(() -> mailService.confirmMail(request, now.plusMinutes(5).plusSeconds(1)))
                .isInstanceOf(AppException.class);
    }

    @DisplayName("이메일 인증을 하지 않고 회원가입을 시도하면 예외가 발생한다.")
    @Test
    void confirmMailWithoutSendMail() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final int code = 123456;
        final String email = "email@knou.ac.kr";

        MailConfirmRequest request = MailConfirmRequest.builder()
                .email(email)
                .code(code)
                .build();

        // when // then
        assertThatThrownBy(() -> mailService.confirmMail(request, now))
                .isInstanceOf(AppException.class);
    }

    private MailHistory createMailHistory(final String email, final int code, final boolean confirm, final LocalDateTime registeredDateTime) {
        return MailHistory.builder()
                .email(email)
                .code(code)
                .confirm(confirm)
                .registeredDateTime(registeredDateTime)
                .build();
    }
}