package knou.course.service.user;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.EmailRequest;
import knou.course.dto.user.request.UserCreateRequest;
import knou.course.dto.user.request.UsernameRequest;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    private User createUser(final String username, final String password, final String email) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
    }
}