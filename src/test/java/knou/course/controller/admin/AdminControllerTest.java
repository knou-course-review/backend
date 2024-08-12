package knou.course.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.user.response.UserPagedResponse;
import knou.course.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@EnableMethodSecurity
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("유저 목록을 페이징 조회한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void getAllUsersPaged() throws Exception {
        // given
        UserPagedResponse result = UserPagedResponse.builder().build();

        BDDMockito.given(userService.getAllUsersPaged(1)).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/admin/users").with(csrf())
                                .param("page", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("유저 목록을 페이징 조회할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getAllUsersPagedWithoutAdmin() throws Exception {
        // given
        UserPagedResponse result = UserPagedResponse.builder().build();

        BDDMockito.given(userService.getAllUsersPaged(1)).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/admin/users").with(csrf())
                                .param("page", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("유저의 계정 상태를 변경한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateUserStatus() throws Exception {
        // given
        Long userId = 2L;

        // when // then
        mockMvc.perform(
                        put("/api/v1/admin/user/{userId}", userId).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("유저의 계정 상태를 변경할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void updateUserStatusWithoutAdmin() throws Exception {
        // given
        Long userId = 2L;

        // when // then
        mockMvc.perform(
                        put("/api/v1/admin/user/{userId}", userId).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}