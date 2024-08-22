package knou.course.service.user;

import knou.course.domain.course.Course;
import knou.course.domain.department.Department;
import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.course.response.CoursePagedResponse;
import knou.course.dto.user.request.*;
import knou.course.dto.user.response.UserPagedResponse;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.AppException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @DisplayName("등록된 유저를 페이징 조회한다. 고정 size - 10, id 정렬까지 테스트")
    @Test
    void getAllUsersPaged() {
        // given
        final Integer page = 1;
        User user1 = createUser("name1", "password", "user1@knou.ac.kr");
        User user2 = createUser("name2", "password", "user2@knou.ac.kr");
        User user3 = createUser("name3", "password", "user3@knou.ac.kr");
        User user4 = createUser("name4", "password", "user4@knou.ac.kr");
        User user5 = createUser("name5", "password", "user5@knou.ac.kr");
        User user6 = createUser("name6", "password", "user6@knou.ac.kr");
        User user7 = createUser("name7", "password", "user7@knou.ac.kr");
        User user8 = createUser("name8", "password", "user8@knou.ac.kr");
        User user9 = createUser("name9", "password", "user9@knou.ac.kr");
        User user10 = createUser("name10", "password", "user10@knou.ac.kr");
        User user11 = createUser("name11", "password", "user11@knou.ac.kr");
        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11));

        // when
        UserPagedResponse pagedResponse = userService.getAllUsersPaged(page);

        // then
        assertThat(pagedResponse.getContent()).hasSize(10)
                .extracting("username")
                .containsExactly(
                        "name1", "name2", "name3", "name4", "name5",
                        "name6", "name7", "name8", "name9", "name10"
                );
        assertThat(pagedResponse)
                .extracting("pageNumber", "pageSize", "totalElements", "totalPages", "first", "last")
                .containsExactlyInAnyOrder(
                        1, 10, 11L, 2, true, false
                );
    }

    @DisplayName("로그인 중인 회원 정보를 조회한다.")
    @Test
    void getLoggedInUser() {
        // given
        final String email = "email@knou.ac.kr";
        User user = createUser("username", "password", email);
        userRepository.save(user);

        // when
        UserResponse userResponse = userService.getLoggedInUser(user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse)
                .extracting("username", "email")
                .containsExactlyInAnyOrder("username", "email@knou.ac.kr");
    }

    @DisplayName("로그인 중인 회원 비밀번호를 변경한다.")
    @Test
    void modifyPassword() {
        // given
        final String email = "email@knou.ac.kr";
        final String password = "password";
        User user = createUser("username", bCryptPasswordEncoder.encode(password), email);
        userRepository.save(user);

        UserModifyPasswordRequest request = UserModifyPasswordRequest.builder()
                .nowPassword(password)
                .password("changePassword")
                .rePassword("changePassword")
                .build();

        // when
        UserResponse userResponse = userService.modifyPassword(request, user.getId());

        // then
        assertThat(userResponse.getId()).isNotNull();
    }

    @DisplayName("로그인 중인 회원 비밀번호를 변경할 때, 현재 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void modifyPasswordNotMatchNowPassword() {
        // given
        final String email = "email@knou.ac.kr";
        final String password = "password";
        User user = createUser("username", bCryptPasswordEncoder.encode(password), email);
        userRepository.save(user);

        UserModifyPasswordRequest request = UserModifyPasswordRequest.builder()
                .nowPassword("missPassword")
                .password("changePassword")
                .rePassword("changePassword")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.modifyPassword(request, user.getId()))
                .isInstanceOf(AppException.class)
                .hasMessage("현재 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("로그인 중인 회원 비밀번호를 변경할 때, 변경 비밀번호와 재확인 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void modifyPasswordWithPasswordNotMatchRePassword() {
        // given
        final String email = "email@knou.ac.kr";
        final String password = "password";
        User user = createUser("username", bCryptPasswordEncoder.encode(password), email);
        userRepository.save(user);

        UserModifyPasswordRequest request = UserModifyPasswordRequest.builder()
                .nowPassword("password")
                .password("changePassword")
                .rePassword("missPassword")
                .build();

        // when // then
        assertThatThrownBy(() -> userService.modifyPassword(request, user.getId()))
                .isInstanceOf(AppException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
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