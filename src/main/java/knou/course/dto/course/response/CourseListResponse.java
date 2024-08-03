package knou.course.dto.course.response;

import knou.course.domain.course.Course;
import knou.course.domain.department.Department;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CourseListResponse {

    private Long id;

    private String courseName;

    private String departmentName;

    private String professorName;

    @Builder
    public CourseListResponse(final Long id, final String courseName, final String departmentName, final String professorName) {
        this.id = id;
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.professorName = professorName;
    }

    public static CourseListResponse of(Course course, final Map<Long, String> professorNameMap, final Map<Long, String> departmentNameMap) {
        return CourseListResponse.builder()
                .courseName(course.getCourseName())
                .id(course.getId())
                .professorName(professorNameMap.get(course.getProfessorId()))
                .departmentName(departmentNameMap.get(course.getDepartmentId()))
                .build();
    }
}
