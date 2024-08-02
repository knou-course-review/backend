package knou.course.dto.professor.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfessorUpdateRequest {

    @NotBlank(message = "교수명은 필수입니다.")
    private String professorName;

    @NotBlank(message = "학과명은 필수입니다.")
    private String departmentName;

    @Builder
    public ProfessorUpdateRequest(final String professorName, final String departmentName) {
        this.professorName = professorName;
        this.departmentName = departmentName;
    }
}
