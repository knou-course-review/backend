package knou.course.service.professor;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.professor.ProfessorRepository;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
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
class ProfessorServiceTest {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @AfterEach
    void tearDown() {
        professorRepository.deleteAllInBatch();
        departmentRepository.deleteAllInBatch();
    }

    @DisplayName("교수를 등록한다.")
    @Test
    void createProfessor() {
        // given
        final String departmentName = "학과명";
        Department department = createDepartment(departmentName);
        departmentRepository.save(department);
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
                .professorName("교수명")
                .departmentName(departmentName)
                .build();

        // when
        ProfessorResponse professorResponse = professorService.createProfessor(request);

        // then
        assertThat(professorResponse.getId()).isNotNull();
    }

    @DisplayName("교수를 등록할 때, 존재하지 않는 학과명이면 예외가 발생한다.")
    @Test
    void createProfessorWithDepartment() {
        // given
        ProfessorCreateRequest request = ProfessorCreateRequest.builder()
                .professorName("교수명")
                .departmentName("학과명")
                .build();

        // when // then
        assertThatThrownBy(() -> professorService.createProfessor(request))
                .isInstanceOf(AppException.class)
                .hasMessage("존재하지 않는 학과입니다.");
    }

    private Department createDepartment(final String departmentName) {
        return Department.builder()
                .departmentName(departmentName)
                .build();
    }
}