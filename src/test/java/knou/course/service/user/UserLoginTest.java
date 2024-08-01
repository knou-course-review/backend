package knou.course.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.UserLoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 API")
    @Test
    void signIn() throws Exception {
        // given
        final String password = "password";
        User user = createUser("username", passwordEncoder.encode(password), "email@knou.ac.kr");
        userRepository.save(user);

        UserLoginRequest request = UserLoginRequest.builder()
                .username(user.getUsername())
                .password(password)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 시 아이디가 틀렸을 때 예외가 발생한다.")
    @Test
    void signInWithIncorrectUsername() throws Exception {
        // given
        final String password = "password";
        User user = createUser("username", passwordEncoder.encode(password), "email@knou.ac.kr");
        userRepository.save(user);

        UserLoginRequest request = UserLoginRequest.builder()
                .username("user")
                .password(password)
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그인 시 비밀번호가 틀렸을 때 예외가 발생한다.")
    @Test
    void signInWithIncorrectPassword() throws Exception {
        // given
        final String password = "password";
        User user = createUser("username", passwordEncoder.encode(password), "email@knou.ac.kr");
        userRepository.save(user);

        UserLoginRequest request = UserLoginRequest.builder()
                .username("username")
                .password("pass")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
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
