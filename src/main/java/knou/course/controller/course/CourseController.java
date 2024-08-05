package knou.course.controller.course;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.request.CourseUpdateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CourseResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.course.CourseService;
import knou.course.swagger.ApiErrorCodeExample;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "강의 등록", description = "강의를 등록합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, ErrorCode.INVALID_INPUT_VALUE})
    @PostMapping("/api/v1/course")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return ApiResponse.ok(courseService.createCourse(request));
    }

    @Operation(summary = "강의 전체 조회", description = "강의를 조회합니다.")
    @GetMapping("/api/v1/courses")
    public ApiResponse<List<CourseListResponse>> getAllCourses() {
        return ApiResponse.ok(courseService.getAllCourses());
    }

    @Operation(summary = "강의 수정", description = "강의를 수정합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, ErrorCode.NOT_FOUND_COURSE, ErrorCode.INVALID_INPUT_VALUE})
    @PutMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId,
                                                    @Valid @RequestBody CourseUpdateRequest request) {
        return ApiResponse.ok(courseService.updateCourse(courseId, request));
    }

    @Operation(summary = "강의 삭제", description = "강의를 삭제합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, ErrorCode.NOT_FOUND_COURSE})
    @DeleteMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

}
