package knou.course.dto.course.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CourseUpdateRequest {

    @Schema(example = "이산수학")
    @NotBlank(message = "강의명은 필수입니다.")
    private String courseName;

    @Schema(example = "13")
    private Long departmentId;

    @Schema(example = "13")
    private Long professorId;

    @Schema(example = "3")
    @Positive(message = "학년은 양수여야 합니다.")
    @Max(value = 4, message = "학년은 최대 4학년입니다.")
    private int grade;

    @Schema(example = "3")
    @Positive(message = "학점은 양수여야 합니다.")
    @Max(value = 3, message = "학점은 최대 3입니다.")
    private int credit;

    @Schema(example = "출석, 비출석")
    @NotBlank(message = "수업유형은 필수입니다.")
    private String classType;

    @Schema(example = "전공, 교양, 일반")
    @NotBlank(message = "교과구분은 필수입니다.")
    private String classification;

    @Schema(example = "1학기, 2학기")
    @NotBlank(message = "학기는 필수입니다.")
    private String semester;

    @Builder
    public CourseUpdateRequest(final String courseName, final Long departmentId, final Long professorId, final int grade, final int credit, final String classType, final String classification, final String semester) {
        this.courseName = courseName;
        this.departmentId = departmentId;
        this.professorId = professorId;
        this.grade = grade;
        this.credit = credit;
        this.classType = classType;
        this.classification = classification;
        this.semester = semester;
    }
}
