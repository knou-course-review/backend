package knou.course.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.user.request.EmailRequest;
import knou.course.dto.user.request.UserCreateRequest;
import knou.course.dto.user.request.UsernameRequest;
import knou.course.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("회원가입")
    @Test
    void createUser() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
                .password("password")
                .email("email@knou.ac.kr")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("회원가입할 때 아이디는 필수값이다.")
    @Test
    void createUserWithoutUsername() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
//                .username("username")
                .password("password")
                .email("email@knou.ac.kr")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("아이디는 필수입니다."));
    }

    @DisplayName("회원가입할 때 비밀번호는 필수값이다.")
    @Test
    void createUserWithoutPassword() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
//                .password("password")
                .email("email@knou.ac.kr")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."));
    }

    @DisplayName("회원가입할 때 이메일은 필수값이다.")
    @Test
    void createUserWithoutEmail() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
                .password("password")
//                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("회원가입할 때 이메일은 @knou.ac.kr 형식이여야 한다.")
    @Test
    void createUserWithoutEmailPattern() throws Exception {
        // given
        UserCreateRequest request = UserCreateRequest.builder()
                .username("username")
                .password("password")
                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/sign-up").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 @knou.ac.kr 도메인이어야 합니다."));
    }

    @DisplayName("아이디 중복검사")
    @Test
    void duplicateUsername() throws Exception {
        // given
        UsernameRequest request = UsernameRequest.builder()
                .username("username")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/duplicate-username").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("아이디 중복검사할 때 아이디는 필수값이다.")
    @Test
    void duplicateUsernameWithoutUsername() throws Exception {
        // given
        UsernameRequest request = UsernameRequest.builder()
//                .username("username")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/duplicate-username").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("아이디는 필수입니다."));
    }

    @DisplayName("이메일 중복검사")
    @Test
    void duplicateEmail() throws Exception {
        // given
        EmailRequest request = EmailRequest.builder()
                .email("email@knou.ac.kr")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/duplicate-email").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이메일 중복검사할 때 이메일은 필수값이다.")
    @Test
    void duplicateEmailWithoutEmail() throws Exception {
        // given
        EmailRequest request = EmailRequest.builder()
//                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/duplicate-email").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("이메일 중복검사할 때 이메일은 @knou.ac.kr 형식이여야한다.")
    @Test
    void duplicateEmailWithoutEmailPattern() throws Exception {
        // given
        EmailRequest request = EmailRequest.builder()
                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/users/duplicate-email").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 @knou.ac.kr 도메인이어야 합니다."));
    }

}