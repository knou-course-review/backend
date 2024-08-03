package knou.course.dto.course.response;

import knou.course.domain.course.Course;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CourseOneResponse {

    private Long id;

    private String courseName;

    private String departmentName;

    private String professorName;

    private int grade;

    private int credit;

    private String classType; // 수업유형(출석, 비출석)

    private String classification;

    private String semester;

    @Builder
    public CourseOneResponse(final Long id, final String courseName, final String departmentName, final String professorName, final int grade, final int credit, final String classType, final String classification, final String semester) {
        this.id = id;
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.professorName = professorName;
        this.grade = grade;
        this.credit = credit;
        this.classType = classType;
        this.classification = classification;
        this.semester = semester;
    }

    public static CourseOneResponse of(Course course, final String professorName, final String departmentName) {
        return CourseOneResponse.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .departmentName(departmentName)
                .professorName(professorName)
                .grade(course.getGrade())
                .credit(course.getCredit())
                .classType(course.getClassType())
                .classification(course.getClassification())
                .semester(course.getSemester())
                .build();
    }
}
