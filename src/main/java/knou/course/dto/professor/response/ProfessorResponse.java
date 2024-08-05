package knou.course.dto.professor.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.department.Department;
import knou.course.domain.professor.Professor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfessorResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "홍길동")
    private String professorName;

    @Schema(example = "컴퓨터과학")
    private String departmentName;

    @Builder
    public ProfessorResponse(final Long id, final String professorName, final String departmentName) {
        this.id = id;
        this.professorName = professorName;
        this.departmentName = departmentName;
    }

    public static ProfessorResponse of(Professor professor, Department department) {
        return ProfessorResponse.builder()
                .id(professor.getId())
                .professorName(professor.getProfessorName())
                .departmentName(department.getDepartmentName())
                .build();
    }

    public static ProfessorResponse of(Professor professor) {
        return ProfessorResponse.builder()
                .id(professor.getId())
                .professorName(professor.getProfessorName())
                .departmentName(professor.getDepartment().getDepartmentName())
                .build();
    }
}
