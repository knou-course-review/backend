package knou.course.controller.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.request.CourseUpdateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CoursePagedResponse;
import knou.course.dto.course.response.CourseResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.course.CourseService;
import knou.course.swagger.ApiErrorCodeExample;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static knou.course.exception.ErrorCode.INVALID_INPUT_VALUE;
import static knou.course.exception.ErrorCode.NOT_FOUND_COURSE;

@Tag(name = "Course Controller - 강의 컨트롤러", description = "강의를 등록, 수정, 삭제, 조회합니다.")
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

    @Operation(summary = "강의 단건 조회", description = "선택한 강의를 조회합니다.")
    @ApiErrorCodeExamples({NOT_FOUND_COURSE, INVALID_INPUT_VALUE})
    @GetMapping("/api/v1/course/{courseId}")
    public ApiResponse<CourseResponse> getCourseById(@PathVariable Long courseId) {
        return ApiResponse.ok(courseService.getCourseById(courseId));
    }

    @Operation(summary = "강의 페이징 조회 - size 10 고정", description = "강의를 페이징 조회합니다. <br> 게시글 정보는 data.content로 접근해주세요.")
    @GetMapping("/api/v2/courses")
    public ApiResponse<CoursePagedResponse> getAllCoursesPaged(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ApiResponse.ok(courseService.getAllCoursesPaged(page));
    }

    @Operation(summary = "강의 수정", description = "강의를 수정합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, NOT_FOUND_COURSE, ErrorCode.INVALID_INPUT_VALUE})
    @PutMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId,
                                                    @Valid @RequestBody CourseUpdateRequest request) {
        return ApiResponse.ok(courseService.updateCourse(courseId, request));
    }

    @Operation(summary = "강의 삭제", description = "강의를 삭제합니다.")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, NOT_FOUND_COURSE})
    @DeleteMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

    @Operation(summary = "강의 검색, 페이징 조회 - size 10 고정", description = "학과명, 교수명, 강의명을 입력하여 강의를 검색합니다. <br> 게시글 정보는 data.content로 접근해주세요.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE})
    @GetMapping("/api/v2/courses/search")
    public ApiResponse<CoursePagedResponse> getAllCoursesPagedSearchBy(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                       @RequestParam(defaultValue = "courseName") String searchType,
                                                                       @RequestParam(required = false) String name) {
        return ApiResponse.ok(courseService.getAllCoursesPagedSearchBy(page, searchType, name));
    }
}
