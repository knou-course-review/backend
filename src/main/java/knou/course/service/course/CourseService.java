package knou.course.service.course;

import jakarta.validation.Valid;
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
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static knou.course.exception.ErrorCode.NOT_FOUND_COURSE;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public CourseResponse createCourse(@Valid @RequestBody CourseCreateRequest request) {
        Course savedCourse = courseRepository.save(request.toEntity());

        return CourseResponse.of(savedCourse);
    }

    public List<CourseListResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        Map<Long, String> departmentNameMap = findDepartmentNamesBy(courses);

        Map<Long, String> professorNameMap = findProfessorNamesBy(courses);

        return courses.stream()
                .map(course -> CourseListResponse.of(course, professorNameMap, departmentNameMap))
                .toList();
    }

    public CoursePagedResponse getAllCoursesPaged(Integer page) {
        if (page < 1) {
            page = 1;
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("courseName").ascending());
        Page<Course> courses = courseRepository.findAll(pageRequest);

        Map<Long, String> departmentNameMap = findDepartmentNamesBy(courses.getContent());

        Map<Long, String> professorNameMap = findProfessorNamesBy(courses.getContent());

        List<CourseListResponse> result = courses.getContent()
                .stream()
                .map(course -> CourseListResponse.of(course, professorNameMap, departmentNameMap))
                .toList();

        return CoursePagedResponse.of(result, courses);
    }

    public CourseResponse getCourseById(final Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(NOT_FOUND_COURSE, NOT_FOUND_COURSE.getMessage()));

        return CourseResponse.of(course);
    }

    @Transactional
    public CourseResponse updateCourse(final Long courseId, final CourseUpdateRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(NOT_FOUND_COURSE, NOT_FOUND_COURSE.getMessage()));

        course.updateCourse(request);

        return CourseResponse.of(course);
    }

    @Transactional
    public void deleteCourse(final Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(NOT_FOUND_COURSE, NOT_FOUND_COURSE.getMessage()));

        courseRepository.delete(course);
    }

    private Map<Long, String> findProfessorNamesBy(final List<Course> courses) {
        List<Long> professorIds = courses.stream()
                .map(Course::getProfessorId)
                .toList();
        List<Professor> professors = professorRepository.findAllByIdIn(professorIds);

        return professors.stream()
                .collect(Collectors.toMap(Professor::getId, Professor::getProfessorName));
    }

    private Map<Long, String> findDepartmentNamesBy(final List<Course> courses) {
        List<Long> departmentIds = courses.stream()
                .map(Course::getDepartmentId)
                .toList();
        List<Department> departments = departmentRepository.findAllByIdIn(departmentIds);


        return departments.stream()
                .collect(Collectors.toMap(Department::getId, Department::getDepartmentName));
    }

}
