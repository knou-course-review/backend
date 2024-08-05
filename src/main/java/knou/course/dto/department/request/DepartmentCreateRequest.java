package knou.course.dto.department.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import knou.course.domain.department.Department;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentCreateRequest {

    @Schema(example = "컴퓨터과학, 국어국문학과")
    @NotBlank(message = "학과명은 필수입니다.")
    private String departmentName;

    @Builder
    public DepartmentCreateRequest(final String departmentName) {
        this.departmentName = departmentName;
    }

    public Department toEntity() {
        return Department.builder()
                .departmentName(departmentName)
                .build();
    }
}
