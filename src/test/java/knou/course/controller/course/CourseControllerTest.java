package knou.course.controller.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.request.CourseUpdateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CourseResponse;
import knou.course.dto.professor.request.ProfessorUpdateRequest;
import knou.course.service.course.CourseService;
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

@WebMvcTest(controllers = CourseController.class)
@EnableMethodSecurity
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @DisplayName("강의를 등록한다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createCourse() throws Exception {
        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseName("강의명")
                .grade(1)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/course").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("강의를 등록할 때 ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void createCourseWithoutAdmin() throws Exception {
        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseName("강의명")
                .grade(1)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/course").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("강의를 등록할 때, 강의명은 필수다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void createCourseWithoutCourseName() throws Exception {
        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
//                .courseName("강의명")
                .grade(1)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        post("/api/v1/course").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("강의명은 필수입니다."));
    }

    @DisplayName("강의 전체 목록을 조회한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void getAllCourses() throws Exception {
        // given
        List<CourseListResponse> result = List.of();

        BDDMockito.given(courseService.getAllCourses()).willReturn(result);

        // when // then
        mockMvc.perform(
                        get("/api/v1/courses").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("등록된 강의 수정")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateCourse() throws Exception {
        // given
        CourseUpdateRequest request = CourseUpdateRequest.builder()
                .courseName("강의명")
                .professorId(1L)
                .departmentId(1L)
                .grade(3)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/course/{courseId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("등록된 강의를 수정할 때, ADMIN이 아니면 예외가 발생한다.")
    @WithMockUser(username = "1", roles = "USER")
    @Test
    void updateCourseWithoutAdmin() throws Exception {
        // given
        CourseUpdateRequest request = CourseUpdateRequest.builder()
                .courseName("강의명")
                .professorId(1L)
                .departmentId(1L)
                .grade(3)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/course/{courseId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("등록된 강의를 수정할 때, 강의명은 필수다.")
    @WithMockUser(username = "1", roles = "ADMIN")
    @Test
    void updateCourseWithoutCourseName() throws Exception {
        // given
        CourseUpdateRequest request = CourseUpdateRequest.builder()
//                .courseName("강의명")
                .professorId(1L)
                .departmentId(1L)
                .grade(3)
                .credit(3)
                .classType("출석")
                .classification("전공")
                .semester("1학기")
                .build();

        // when // then
        mockMvc.perform(
                        put("/api/v1/course/{courseId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("강의명은 필수입니다."));
    }
}