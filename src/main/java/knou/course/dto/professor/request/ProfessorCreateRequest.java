package knou.course.dto.professor.request;

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

    @NotBlank(message = "교수명은 필수입니다.")
    private String professorName;

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
