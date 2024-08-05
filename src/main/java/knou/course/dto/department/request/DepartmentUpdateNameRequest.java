package knou.course.dto.department.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DepartmentUpdateNameRequest {

    @Schema(example = "컴퓨터과학")
    @NotBlank(message = "학과명은 필수입니다.")
    private String departmentName;

    @Builder
    public DepartmentUpdateNameRequest(final String departmentName) {
        this.departmentName = departmentName;
    }
}
