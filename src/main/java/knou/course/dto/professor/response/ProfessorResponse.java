package knou.course.dto.professor.response;

import knou.course.domain.department.Department;
import knou.course.domain.professor.Professor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfessorResponse {

    private Long id;

    private String professorName;

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
}
