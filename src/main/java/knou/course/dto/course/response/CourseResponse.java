package knou.course.dto.course.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.course.Course;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CourseResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "이산수학")
    private String courseName;

    @Schema(example = "13")
    private Long departmentId;

    @Schema(example = "13")
    private Long professorId;

    @Schema(example = "3")
    private int grade;

    @Schema(example = "3")
    private int credit;

    @Schema(example = "출석")
    private String classType; // 수업유형(출석, 비출석)

    @Schema(example = "전공")
    private String classification;

    @Schema(example = "1학기")
    private String semester;

    @Builder
    public CourseResponse(final Long id, final String courseName, final Long departmentId, final Long professorId, final int grade, final int credit, final String classType, final String classification, final String semester) {
        this.id = id;
        this.courseName = courseName;
        this.departmentId = departmentId;
        this.professorId = professorId;
        this.grade = grade;
        this.credit = credit;
        this.classType = classType;
        this.classification = classification;
        this.semester = semester;
    }

    public static CourseResponse of(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .departmentId(course.getDepartmentId())
                .professorId(course.getProfessorId())
                .grade(course.getGrade())
                .credit(course.getCredit())
                .classType(course.getClassType())
                .classification(course.getClassification())
                .semester(course.getSemester())
                .build();
    }
}
