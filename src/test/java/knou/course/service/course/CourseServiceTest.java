package knou.course.service.course;

import knou.course.domain.course.Course;
import knou.course.domain.course.CourseRepository;
import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.professor.ProfessorRepository;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.request.CourseUpdateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CoursePagedResponse;
import knou.course.dto.course.response.CourseResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @AfterEach
    void tearDown() {
        courseRepository.deleteAllInBatch();
        professorRepository.deleteAllInBatch();
        departmentRepository.deleteAllInBatch();
    }

    @DisplayName("강의를 등록한다.")
    @Test
    void createCourse() {
        // given
        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseName("강의명")
                .classType("출석")
                .classification("전공")
                .grade(3)
                .credit(3)
                .semester("1학기")
                .build();

        // when
        CourseResponse courseResponse = courseService.createCourse(request);

        // then
        assertThat(courseResponse.getId()).isNotNull();
        assertThat(courseResponse)
                .extracting("courseName", "classType", "classification", "grade", "credit", "semester")
                .containsExactlyInAnyOrder("강의명", "출석", "전공", 3, 3, "1학기");
    }

    @DisplayName("등록된 강의 전체를 조회한다.")
    @Test
    void getAllCourses() {
        // given
        Department department1 = createDepartment("컴퓨터과학");
        Department department2 = createDepartment("국어국문학과");
        Department department3 = createDepartment("영어영문학과");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        Professor professor1 = createProfessor("컴퓨터과학교수", department1);
        Professor professor2 = createProfessor("국어국문학과교수", department2);
        Professor professor3 = createProfessor("영어영문학과교수", department3);
        professorRepository.saveAll(List.of(professor1, professor2, professor3));

        Course course1 = createCourse(department1.getId(), professor1.getId(), "운영체제");
        Course course2 = createCourse(department2.getId(), professor2.getId(), "글쓰기");
        Course course3 = createCourse(department3.getId(), professor3.getId(), "영어쓰기");
        courseRepository.saveAll(List.of(course1, course2, course3));

        // when
        List<CourseListResponse> result = courseService.getAllCourses();

        // then
        assertThat(result).hasSize(3)
                .extracting("courseName", "professorName", "departmentName")
                .containsExactlyInAnyOrder(
                        tuple("운영체제", "컴퓨터과학교수", "컴퓨터과학"),
                        tuple("글쓰기", "국어국문학과교수", "국어국문학과"),
                        tuple("영어쓰기", "영어영문학과교수", "영어영문학과")
                );
    }

    @DisplayName("선택한 강의를 조회한다.")
    @Test
    void getCourseById() {
        // given
        Course course = createCourse(1L, 1L, "운영체제");
        courseRepository.save(course);

        // when
        CourseResponse courseResponse = courseService.getCourseById(course.getId());

        // then
        assertThat(courseResponse.getId()).isNotNull();
        assertThat(courseResponse)
                .extracting("departmentId", "professorId", "courseName")
                .containsExactlyInAnyOrder(1L, 1L, "운영체제");
    }

    @DisplayName("등록된 강의를 페이징 조회한다. 고정 size - 10, courseName 정렬까지 테스트")
    @Test
    void getAllCoursesPaged() {
        // given
        final Integer page = 1;
        Department department1 = createDepartment("컴퓨터과학과");
        Department department2 = createDepartment("국어국문학과");
        Department department3 = createDepartment("영어영문학과");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        Professor professor1 = createProfessor("컴퓨터과학과교수", department1);
        Professor professor2 = createProfessor("국어국문학과교수", department2);
        Professor professor3 = createProfessor("영어영문학과교수", department3);
        professorRepository.saveAll(List.of(professor1, professor2, professor3));

        Course course1 = createCourse(department1.getId(), professor1.getId(), "디지털논리회로");
        Course course2 = createCourse(department2.getId(), professor2.getId(), "글쓰기");
        Course course3 = createCourse(department3.getId(), professor3.getId(), "영어쓰기");
        courseRepository.saveAll(List.of(course1, course2, course3));

        // when
        CoursePagedResponse pagedResponse = courseService.getAllCoursesPaged(page);

        // then
        assertThat(pagedResponse.getContent()).hasSize(3)
                .extracting("courseName", "professorName", "departmentName")
                .containsExactly(
                        tuple("글쓰기", "국어국문학과교수", "국어국문학과"),
                        tuple("디지털논리회로", "컴퓨터과학과교수", "컴퓨터과학과"),
                        tuple("영어쓰기", "영어영문학과교수", "영어영문학과")
                );
    }

    @DisplayName("등록된 강의를 수정한다.")
    @Test
    void updateCourse() {
        // given
        Course course = createCourse(1L, 1L, "운영체제");
        courseRepository.save(course);

        CourseUpdateRequest request = CourseUpdateRequest.builder()
                .courseName("이산수학")
                .professorId(1L)
                .departmentId(1L)
                .build();

        // when
        CourseResponse courseResponse = courseService.updateCourse(course.getId(), request);

        // then
        assertThat(courseResponse.getId()).isNotNull();
        assertThat(courseResponse)
                .extracting("courseName", "professorId", "departmentId")
                .containsExactlyInAnyOrder("이산수학", 1L, 1L);
    }

    @DisplayName("등록된 강의를 삭제한다.")
    @Test
    void deleteCourse() {
        // given
        Course course = createCourse(1L, 1L, "강의");
        courseRepository.save(course);

        // when
        courseService.deleteCourse(course.getId());

        // then
        assertThatThrownBy(() -> courseRepository.findById(course.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("강의를 검색하여 페이징 조회한다. searchType = courseName, name = 이산수학")
    @Test
    void getAllCoursesPagedBySearchWithCourseName() {
        // given
        final Integer page = 1;
        final String searchType = "courseName";
        final String name = "이산수학";
        Course course1 = createCourse(1L, 1L, "디지털논리회로");
        Course course2 = createCourse(2L, 2L, "글쓰기");
        Course course3 = createCourse(3L, 3L, "영어쓰기");
        Course course4 = createCourse(4L, 4L, "이산수학");
        courseRepository.saveAll(List.of(course1, course2, course3, course4));

        // when
        CoursePagedResponse pagedResponse = courseService.getAllCoursesPagedSearchBy(page, searchType, name);

        // then
        assertThat(pagedResponse.getContent()).hasSize(1)
                .extracting("courseName", "professorId", "departmentId")
                .containsExactly(
                        tuple("이산수학", 4L, 4L)
                );
    }

    @DisplayName("강의를 검색하여 페이징 조회한다. searchType = professorName, name = 홍길동")
    @Test
    void getAllCoursesPagedBySearchWithProfessorName() {
        // given
        final Integer page = 1;
        final String searchType = "professorName";
        final String name = "홍길동";

        Department department1 = createDepartment("컴퓨터과학과");
        Department department2 = createDepartment("국어국문학과");
        Department department3 = createDepartment("영어영문학과");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        Professor professor1 = createProfessor("홍길동", department1);
        Professor professor2 = createProfessor("국어국문학과교수", department2);
        Professor professor3 = createProfessor("영어영문학과교수", department3);
        professorRepository.saveAll(List.of(professor1, professor2, professor3));

        Course course1 = createCourse(department1.getId(), professor1.getId(), "디지털논리회로");
        Course course2 = createCourse(department2.getId(), professor2.getId(), "글쓰기");
        Course course3 = createCourse(department3.getId(), professor3.getId(), "영어쓰기");
        Course course4 = createCourse(department1.getId(), professor1.getId(), "이산수학");
        courseRepository.saveAll(List.of(course1, course2, course3, course4));

        // when
        CoursePagedResponse pagedResponse = courseService.getAllCoursesPagedSearchBy(page, searchType, name);

        // then
        assertThat(pagedResponse.getContent()).hasSize(2)
                .extracting("courseName", "professorName", "departmentName")
                .containsExactlyInAnyOrder(
                        tuple("이산수학", "홍길동", "컴퓨터과학과"),
                        tuple("디지털논리회로", "홍길동", "컴퓨터과학과")
                );
    }

    @DisplayName("강의를 검색하여 페이징 조회한다. searchType = departmentName, name = 컴퓨터과학과")
    @Test
    void getAllCoursesPagedBySearchWithDepartmentName() {
        // given
        final Integer page = 1;
        final String searchType = "departmentName";
        final String name = "컴퓨";

        Department department1 = createDepartment("컴퓨터과학과");
        Department department2 = createDepartment("국어국문학과");
        Department department3 = createDepartment("영어영문학과");
        departmentRepository.saveAll(List.of(department1, department2, department3));

        Professor professor1 = createProfessor("컴퓨터과학과교수", department1);
        Professor professor2 = createProfessor("국어국문학과교수", department2);
        Professor professor3 = createProfessor("영어영문학과교수", department3);
        professorRepository.saveAll(List.of(professor1, professor2, professor3));

        Course course1 = createCourse(department1.getId(), professor1.getId(), "디지털논리회로");
        Course course2 = createCourse(department2.getId(), professor2.getId(), "글쓰기");
        Course course3 = createCourse(department3.getId(), professor3.getId(), "영어쓰기");
        Course course4 = createCourse(department1.getId(), professor1.getId(), "이산수학");
        courseRepository.saveAll(List.of(course1, course2, course3, course4));

        // when
        CoursePagedResponse pagedResponse = courseService.getAllCoursesPagedSearchBy(page, searchType, name);

        // then
        assertThat(pagedResponse.getContent()).hasSize(2)
                .extracting("courseName", "professorName", "departmentName")
                .containsExactlyInAnyOrder(
                        tuple("이산수학", "컴퓨터과학과교수", "컴퓨터과학과"),
                        tuple("디지털논리회로", "컴퓨터과학과교수", "컴퓨터과학과")
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

    private Course createCourse(final Long departmentId, final Long professorId, final String courseName) {
        return Course.builder()
                .departmentId(departmentId)
                .professorId(professorId)
                .courseName(courseName)
                .grade(3)
                .credit(3)
                .classType("classType")
                .classification("classification")
                .semester("semester")
                .build();
    }
}