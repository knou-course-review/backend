package knou.course.service.department;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.user.Role;
import knou.course.domain.user.Status;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.response.DepartmentResponse;
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
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("학과명을 등록한다.")
    @Test
    void createDepartment() {
        // given
        User user = createUser("username", "password", "email@knou.ac.kr");
        userRepository.save(user);

        DepartmentCreateRequest request = DepartmentCreateRequest.builder()
                .departmentName("학과명")
                .build();

        // when
        DepartmentResponse departmentResponse = departmentService.createDepartment(request);

        // then
        assertThat(departmentResponse.getId()).isNotNull();
        assertThat(departmentResponse)
                .extracting("id", "departmentName")
                .containsExactlyInAnyOrder(departmentResponse.getId(), "학과명");
    }

    @DisplayName("학과명을 등록할 때 이미 존재하는 학과명이면 예외가 발생한다.")
    @Test
    void createDepartmentWithDuplicateDepartmentName() {
        // given
        User user = createUser("username", "password", "email@knou.ac.kr");
        userRepository.save(user);

        Department department = createDepartment("학과명");
        departmentRepository.save(department);

        DepartmentCreateRequest request = DepartmentCreateRequest.builder()
                .departmentName("학과명")
                .build();

        // when // then
        assertThatThrownBy(() -> departmentService.createDepartment(request))
                .isInstanceOf(AppException.class)
                .hasMessage("이미 존재하는 학과명입니다.");
    }

    @DisplayName("학과명을 전체 조회한다.")
    @Test
    void getAllDepartments() {
        // given
        Department department1 = createDepartment("학과명1");
        Department department2 = createDepartment("학과명2");
        Department department3 = createDepartment("학과명3");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        // when
        List<DepartmentResponse> result = departmentService.getAllDepartments();

        // then
        assertThat(result).hasSize(3)
                .extracting("id", "departmentName")
                .containsExactlyInAnyOrder(
                        tuple(department1.getId(), "학과명1"),
                        tuple(department2.getId(), "학과명2"),
                        tuple(department3.getId(), "학과명3")
                );
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

    private Department createDepartment(final String departmentName) {
        return Department.builder()
                .departmentName(departmentName)
                .build();
    }
}