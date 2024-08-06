package knou.course.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.request.DepartmentUpdateNameRequest;
import knou.course.dto.department.response.DepartmentResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.department.DepartmentService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static knou.course.exception.ErrorCode.*;

@Tag(name = "Department Controller - 학과 컨트롤러", description = "학과를 등록, 수정, 삭제, 조회합니다.")
@Slf4j
@RequiredArgsConstructor
@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "학과 등록", description = "학과를 등록합니다. ex) 컴퓨터과학, 국어국문학과...")
    @ApiErrorCodeExamples({NOT_ADMIN, INVALID_INPUT_VALUE, ALREADY_EXIST_DEPARTMENT_NAME})
    @PostMapping("/api/v1/department")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        return ApiResponse.ok(departmentService.createDepartment(request));
    }

    @Operation(summary = "학과 전체 조회", description = "학과를 조회합니다.")
    @GetMapping("/api/v1/departments")
    public ApiResponse<List<DepartmentResponse>> getAllDepartments() {
        return ApiResponse.ok(departmentService.getAllDepartments());
    }

    @Operation(summary = "학과 단건 조회", description = "선택한 학과를 조회합니다.")
    @ApiErrorCodeExamples({NOT_FOUND_DEPARTMENT, INVALID_INPUT_VALUE})
    @GetMapping("/api/v1/department/{departmentId}")
    public ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable Long departmentId) {
        return ApiResponse.ok(departmentService.getDepartmentById(departmentId));
    }

    @Operation(summary = "학과 수정", description = "학과를 수정합니다.")
    @ApiErrorCodeExamples({NOT_ADMIN, NOT_FOUND_DEPARTMENT, ALREADY_EXIST_DEPARTMENT_NAME, INVALID_INPUT_VALUE})
    @PutMapping("/api/v1/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentResponse> updateDepartment(@PathVariable Long departmentId,
                                                            @Valid @RequestBody DepartmentUpdateNameRequest request) {
        return ApiResponse.ok(departmentService.updateDepartmentName(departmentId, request));
    }

    @Operation(summary = "학과 삭제", description = "학과를 삭제합니다.")
    @ApiErrorCodeExamples({NOT_ADMIN, NOT_FOUND_DEPARTMENT})
    @DeleteMapping("/api/v1/department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

}
