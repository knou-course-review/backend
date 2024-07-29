package knou.course.service.user;

import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.EmailRequest;
import knou.course.dto.user.request.UsernameRequest;
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

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
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