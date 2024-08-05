package knou.course.dto.professor.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import knou.course.domain.department.Department;
import knou.course.domain.professor.Professor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfessorCreateRequest {

    @Schema(example = "홍길동")
    @NotBlank(message = "교수명은 필수입니다.")
    private String professorName;

    @Schema(example = "컴퓨터과학")
    @NotBlank(message = "학과명은 필수입니다.")
    private String departmentName;

    @Builder
    public ProfessorCreateRequest(final String professorName, final String departmentName) {
        this.professorName = professorName;
        this.departmentName = departmentName;
    }

    public Professor toEntity(Department department) {
        return Professor.builder()
                .professorName(professorName)
                .department(department)
                .build();
    }
}
