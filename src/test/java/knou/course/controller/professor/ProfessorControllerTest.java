package knou.course.controller.professor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.request.ProfessorUpdateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
import knou.course.service.professor.ProfessorService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfessorController.class)
@EnableMethodSecurity
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfessorService professorService;

    @DisplayName("교수 등록")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createProfessor() throws Exception {
        // given
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/professor").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("교수를 등록할 때, 교수명을 적지 않으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createProfessorWithoutProfessorName() throws Exception {
        // given
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
//                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/professor").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("교수명은 필수입니다."));
    }

    @DisplayName("교수를 등록할 때, 학과명을 적지 않으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createProfessorWithoutDepartmentName() throws Exception {
        // given
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
                .professorName("교수명")
//                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/professor").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과명은 필수입니다."));
    }

    @DisplayName("교수를 등록할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void createProfessorWithoutAdmin() throws Exception {
        // given
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/professor").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("교수 전체 목록을 조회한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getAllProfessors() throws Exception {
        // given
        List<ProfessorResponse> result = List.of();

        BDDMockito.given(professorService.getAllProfessors()).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/professors").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("선택한 교수를 조회한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getProfessorById() throws Exception {
        // given
        ProfessorResponse result = ProfessorResponse.builder().build();

        BDDMockito.given(professorService.getProfessorById(1L)).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @DisplayName("등록된 교수 수정")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateProfessor() throws Exception {
        // given
        ProfessorUpdateRequest request = ProfessorUpdateRequest.builder()
                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("등록된 교수를 수정할 때, 교수명이 없으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateProfessorWithoutProfessorName() throws Exception {
        // given
        ProfessorUpdateRequest request = ProfessorUpdateRequest.builder()
//                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("교수명은 필수입니다."));
    }

    @DisplayName("등록된 교수를 수정할 때, 학과명이 없으면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateProfessorWithoutDepartmentName() throws Exception {
        // given
        ProfessorUpdateRequest request = ProfessorUpdateRequest.builder()
                .professorName("교수명")
//                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("학과명은 필수입니다."));
    }

    @DisplayName("등록된 교수를 수정할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void updateProfessorWithoutAdmin() throws Exception {
        // given
        ProfessorUpdateRequest request = ProfessorUpdateRequest.builder()
                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("등록된 교수를 삭제한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void deleteProfessor() throws Exception {
        // when // then
        mockMvc.perform(
                        delete("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("등록된 교수를 삭제할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void deleteProfessorWithoutAdmin() throws Exception {
        // when // then
        mockMvc.perform(
                        delete("/api/v1/professor/{professorId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}