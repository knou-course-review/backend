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

import java.util.List;

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

    @DisplayName("등록되어있는 교수 전체 목록을 조회한다.")
    @Test
    void getAllProfessors() {
        // given
        Department department1 = createDepartment("학과명1");
        Department department2 = createDepartment("학과명2");
        Department department3 = createDepartment("학과명3");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        Professor professor1 = createProfessor("교수명1", department1);
        Professor professor2 = createProfessor("교수명2", department2);
        Professor professor3 = createProfessor("교수명3", department3);
        professorRepository.saveAll(List.of(professor1, professor2, professor3));

        // when
        List<ProfessorResponse> result = professorService.getAllProfessors();

        // then
        assertThat(result).hasSize(3)
                .extracting("departmentName", "professorName")
                .containsExactlyInAnyOrder(
                        tuple("학과명1", "교수명1"),
                        tuple("학과명2", "교수명2"),
                        tuple("학과명3", "교수명3")
                );
    }

    private Department createDepartment(final String departmentName) {
        return Department.builder()
                .departmentName(departmentName)
                .build();
    }

    private Professor createProfessor(final String professorName, Department department) {
        return Professor.builder()
                .professorName(professorName)
                .department(department)
                .build();
    }
}