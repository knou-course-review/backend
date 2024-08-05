package knou.course.service.user;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.*;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static knou.course.exception.ErrorCode.NOT_FOUND_EMAIL_AUTHENTICATION;
import static knou.course.exception.ErrorCode.NOT_FOUND_USER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailHistoryRepository mailHistoryRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        mailHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("아이디 중복검사를 성공한다.")
    @Test
    void checkUsername() {
        // given
        final String username = "username";
        UsernameRequest request = UsernameRequest.builder()
                .username(username)
                .build();

        // when // then
        userService.checkUsernameDuplication(request.getUsername());
    }

    @DisplayName("중복된 아이디가 존재하여 예외가 발생한다.")
    @Test
    void checkUsernameDuplication() {
        // given
        final String username = "username";
        User user = createUser(username, "password", "email@knou.ac.kr");
        userRepository.save(user);

        UsernameRequest request = UsernameRequest.builder()
                .username(username)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.checkUsernameDuplication(request.getUsername()))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 유저입니다.");
    }

    @DisplayName("이메일 중복검사를 성공한다.")
    @Test
    void checkEmail() {
        // given
        final String email = "email@knou.ac.kr";
        EmailRequest request = EmailRequest.builder()
                .email(email)
                .build();

        // when // then
        userService.checkEmailDuplication(request.getEmail());
    }

    @DisplayName("중복된 이메일이 존재하여 예외가 발생한다.")
    @Test
    void checkEmailDuplication() {
        // given
        final String email = "email@knou.ac.kr";
        User user = createUser("username", "password", email);
        userRepository.save(user);

        EmailRequest request = EmailRequest.builder()
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.checkEmailDuplication(request.getEmail()))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 유저입니다.");
    }

    @DisplayName("회원가입을 완료한다.")
    @Test
    void createUser() {
        // given
        MailHistory mailHistory = MailHistory.builder()
                .code(123456)
                .email("email@knou.ac.kr")
                .confirm(true)
                .build();
        mailHistoryRepository.save(mailHistory);

        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
                .password("password")
                .email("email@knou.ac.kr")
                .build();

        // when
        UserResponse userResponse = userService.singUp(request);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "email")
                .containsExactlyInAnyOrder("username", "email@knou.ac.kr");
    }

    @DisplayName("회원가입을 진행할 때 이메일 인증을 안 하면 예외가 발생한다.")
    @Test
    void createUserWithoutEmailAuthentication() {
        // given
        MailHistory mailHistory = MailHistory.builder()
                .code(123456)
                .email("email@knou.ac.kr")
                .confirm(false)
                .build();
        mailHistoryRepository.save(mailHistory);

        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
                .password("password")
                .email("email@knou.ac.kr")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.singUp(request))
                .isInstanceOf(AppException.class)
                .hasMessage("이메일 인증이 필요합니다.");
    }

    @DisplayName("이메일로 아이디를 찾는다.")
    @Test
    void findUsername() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, true, now);
        mailHistoryRepository.save(mailHistory);

        User user = createUser("username", "password", email);
        userRepository.save(user);

        EmailRequest request = EmailRequest.builder()
                .email(email)
                .build();

        // when
        UserResponse userResponse = userService.findUsername(request);

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "email")
                .containsExactlyInAnyOrder(
                        "username", "email@knou.ac.kr"
                );
    }

    @DisplayName("이메일로 아이디를 찾을 때, 이메일 인증이 되어있지 않으면 예외가 발생한다.")
    @Test
    void findUsernameWithoutEmailAuthentication() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, false, now);
        mailHistoryRepository.save(mailHistory);

        User user = createUser("username", "password", email);
        userRepository.save(user);

        EmailRequest request = EmailRequest.builder()
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findUsername(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
    }

    @DisplayName("이메일로 아이디를 찾을 때, 이메일 인증을 해도 회원가입이 되어있지 않으면 예외가 발생한다.")
    @Test
    void findUsernameWithoutUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, false, now);
        mailHistoryRepository.save(mailHistory);


        EmailRequest request = EmailRequest.builder()
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findUsername(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_USER.getMessage());
    }

    @DisplayName("비밀번호 재설정을 위해 이메일과 아이디를 이용하여 이메일 인증을 확인한다.")
    @Test
    void findPassword() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, true, now);
        mailHistoryRepository.save(mailHistory);

        User user = createUser("username", "password", email);
        userRepository.save(user);

        UserFindPasswordRequest request = UserFindPasswordRequest.builder()
                .username("username")
                .email(email)
                .build();

        // when
        UserResponse userResponse = userService.findPassword(request);

        // then
        assertThat(userResponse.getId()).isNotNull();
    }

    @DisplayName("비밀번호 재설정을 위해 이메일과 아이디를 이용할 때, 이메일 인증이 되어있지 않으면 예외가 발생한다.")
    @Test
    void findPasswordWithoutEmailAuthentication() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, false, now);
        mailHistoryRepository.save(mailHistory);

        User user = createUser("username", "password", email);
        userRepository.save(user);

        UserFindPasswordRequest request = UserFindPasswordRequest.builder()
                .username("username")
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findPassword(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
    }

    @DisplayName("비밀번호 재설정을 위해 이메일과 아이디를 이용할 때, 존재하지 않은 유저면 예외가 발생한다.")
    @Test
    void findPasswordNotFoundUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, true, now);
        mailHistoryRepository.save(mailHistory);

        UserFindPasswordRequest request = UserFindPasswordRequest.builder()
                .username("username")
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findPassword(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_USER.getMessage());
    }

    @DisplayName("비밀번호 재설정을 위해 이메일과 아이디를 이용할 때, 입력한 아이디와 이메일로 찾은 아이디가 일치하지 않으면 예외가 발생한다.")
    @Test
    void findPasswordMissingInputUsername() {
        // given
        LocalDateTime now = LocalDateTime.now();
        final String email = "email@knou.ac.kr";
        MailHistory mailHistory = createMailHistory(email, 123456, true, now);
        mailHistoryRepository.save(mailHistory);

        User user = createUser("miss", "password", email);
        userRepository.save(user);

        UserFindPasswordRequest request = UserFindPasswordRequest.builder()
                .username("username")
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findPassword(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_USER.getMessage());
    }

    @DisplayName("비밀번호 재설정을 위해 이메일과 아이디를 이용할 때, 이메일 인증을 시도하지 않았으면 예외가 발생한다.")
    @Test
    void findPasswordWithoutSendEmailAuthentication() {
        // given
        final String email = "email@knou.ac.kr";

        User user = createUser("username", "password", email);
        userRepository.save(user);

        UserFindPasswordRequest request = UserFindPasswordRequest.builder()
                .username("username")
                .email(email)
                .build();

        // when // then
        assertThatThrownBy(() -> userService.findPassword(request))
                .isInstanceOf(AppException.class)
                .hasMessage(NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
    }

    @DisplayName("이메일 인증 후, 비밀번호를 변경한다.")
    @Test
    void changePasswordWithEmailAuthentication() {
        // given
        final String email = "email@knou.ac.kr";
        LocalDateTime now = LocalDateTime.now();
        MailHistory mailHistory = createMailHistory(email, 123456, true, now);
        mailHistoryRepository.save(mailHistory);
        final String password = "password";

        User user = createUser("username", password, email);
        userRepository.save(user);

        UserChangePassword request = UserChangePassword.builder()
                .email(email)
                .password(password)
                .rePassword(password)
                .build();

        // when
        UserResponse userResponse = userService.changePassword(request);

        // then
        assertThat(userResponse.getId()).isNotNull();
    }

    private User createUser(final String username, final String password, final String email) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
    }

    private MailHistory createMailHistory(final String email, final int code, final boolean confirm, final LocalDateTime now) {
        return MailHistory.builder()
                .email(email)
                .code(code)
                .confirm(confirm)
                .registeredDateTime(now)
                .build();
    }
}