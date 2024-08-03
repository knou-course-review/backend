package knou.course.service.course;

import jakarta.validation.Valid;
import knou.course.domain.course.Course;
import knou.course.domain.course.CourseRepository;
import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.professor.ProfessorRepository;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
