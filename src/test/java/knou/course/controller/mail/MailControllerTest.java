package knou.course.controller.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.mail.request.MailConfirmRequest;
import knou.course.dto.mail.request.MailCreateRequest;
import knou.course.service.mail.MailService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MailController.class)
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MailService mailService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("1", "", List.of()));
    }

    @DisplayName("메일 전송 기능")
    @Test
    void createMail() throws Exception {
        // given
        MailCreateRequest request = MailCreateRequest.builder()
                .email("email@knou.ac.kr")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("메일을 전송할 때 이메일은 필수값이다.")
    @Test
    void createMailWithoutEmail() throws Exception {
        // given
        MailCreateRequest request = MailCreateRequest.builder()
//                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("메일을 전송할 때 이메일은 @knou.ac.kr 형식이여야 한다.")
    @Test
    void createMailWithoutEmailPattern() throws Exception {
        // given
        MailCreateRequest request = MailCreateRequest.builder()
                .email("email@naver.com")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 @knou.ac.kr 도메인이어야 합니다."));
    }

    @DisplayName("이메일 인증")
    @Test
    void confirmMail() throws Exception {
        // given
        MailConfirmRequest request = MailConfirmRequest.builder()
                .email("email@knou.ac.kr")
                .code(123456)
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("이메일 인증할 때 이메일은 필수값이다.")
    @Test
    void confirmMailWithoutEmail() throws Exception {
        // given
        MailConfirmRequest request = MailConfirmRequest.builder()
//                .email("email@naver.com")
                .code(123456)
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수입니다."));
    }

    @DisplayName("이메일 인증할 때 이메일은 @knou.ac.kr 형식이여야 한다.")
    @Test
    void confirmMailWithoutEmailPattern() throws Exception {
        // given
        MailConfirmRequest request = MailConfirmRequest.builder()
                .email("email@naver.com")
                .code(123456)
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 @knou.ac.kr 도메인이어야 합니다."));
    }

    @DisplayName("이메일 인증할 때 인증번호는 100_000 이상 이다.")
    @Test
    void confirmMailWithAtLeast100_000() throws Exception {
        // given
        MailConfirmRequest request = MailConfirmRequest.builder()
                .email("email@knou.ac.kr")
                .code(99999)
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("인증번호는 최소 100,000 이상 입니다."));
    }

    @DisplayName("이메일 인증할 때 인증번호는 999_999 이하 이다.")
    @Test
    void confirmMailWithAtMost999_999() throws Exception {
        // given
        MailConfirmRequest request = MailConfirmRequest.builder()
                .email("email@knou.ac.kr")
                .code(1_000_000)
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/mail").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("인증번호는 최대 999,999 이하 입니다."));
    }
}