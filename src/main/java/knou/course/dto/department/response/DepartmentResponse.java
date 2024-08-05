package knou.course.dto.department.response;

import io.swagger.v3.oas.annotations.media.Schema;
import knou.course.domain.department.Department;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "컴퓨터과학")
    private String departmentName;

    @Builder
    public DepartmentResponse(final Long id, final String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public static DepartmentResponse of(final Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .build();
    }
}
