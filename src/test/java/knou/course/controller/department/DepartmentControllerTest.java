package knou.course.controller.department;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.response.DepartmentResponse;
import knou.course.service.department.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartmentController.class)
@EnableMethodSecurity
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    @DisplayName("학과 등록")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createDepartment() throws Exception {
        // given
        DepartmentCreateRequest request = DepartmentCreateRequest.builder()
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/department").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("학과명을 입력하지 않으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createDepartmentWithoutDepartmentName() throws Exception {
        // given
        DepartmentCreateRequest request = DepartmentCreateRequest.builder()
//                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/department").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과명은 필수입니다."));
    }

    @DisplayName("학과를 등록할 때 ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void createDepartmentWithoutAdmin() throws Exception {
        // given
        DepartmentCreateRequest request = DepartmentCreateRequest.builder()
                .departmentName("학과명")
                .build();

        // TODO: 핸들러 적용하여 Response 생성
        // when // then
        mockMvc.perform(
                        post("/api/v1/department").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("등록되어있는 학과 전체 조회")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getAllDepartments() throws Exception {
        // given
        List<DepartmentResponse> result = List.of();

        BDDMockito.given(departmentService.getAllDepartments()).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/departments").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.message").value("OK"));
    }

}