package knou.course.dto.course.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "1")
    private Long id;

    @Schema(example = "이산수학")
    private String courseName;

    @Schema(example = "컴퓨터과학")
    private String departmentName;

    @Schema(example = "홍길동")
    private String professorName;

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
    public CourseListResponse(final Long id, final String courseName, final String departmentName, final String professorName, final Long departmentId, final Long professorId, final int grade, final int credit, final String classType, final String classification, final String semester) {
        this.id = id;
        this.courseName = courseName;
        this.departmentName = departmentName;
        this.professorName = professorName;
        this.departmentId = departmentId;
        this.professorId = professorId;
        this.grade = grade;
        this.credit = credit;
        this.classType = classType;
        this.classification = classification;
        this.semester = semester;
    }

    public static CourseListResponse of(Course course, final Map<Long, String> professorNameMap, final Map<Long, String> departmentNameMap) {
        return CourseListResponse.builder()
                .courseName(course.getCourseName())
                .id(course.getId())
                .professorName(professorNameMap.get(course.getProfessorId()))
                .departmentName(departmentNameMap.get(course.getDepartmentId()))
                .professorId(course.getProfessorId())
                .departmentId(course.getDepartmentId())
                .grade(course.getGrade())
                .credit(course.getCredit())
                .classType(course.getClassType())
                .classification(course.getClassification())
                .semester(course.getSemester())
                .build();
    }
}
