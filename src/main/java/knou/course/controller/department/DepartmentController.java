package knou.course.controller.department;

import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.response.DepartmentResponse;
import knou.course.service.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/api/v1/department")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        return ApiResponse.ok(departmentService.createDepartment(request));
    }
}