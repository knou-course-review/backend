package knou.course.controller.course;

import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.course.request.CourseCreateRequest;
import knou.course.dto.course.request.CourseUpdateRequest;
import knou.course.dto.course.response.CourseListResponse;
import knou.course.dto.course.response.CourseResponse;
import knou.course.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/api/v1/course")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        return ApiResponse.ok(courseService.createCourse(request));
    }

    @GetMapping("/api/v1/courses")
    public ApiResponse<List<CourseListResponse>> getAllCourses() {
        return ApiResponse.ok(courseService.getAllCourses());
    }

    @PutMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CourseResponse> updateCourse(@PathVariable Long courseId,
                                                    @Valid @RequestBody CourseUpdateRequest request) {
        return ApiResponse.ok(courseService.updateCourse(courseId, request));
    }

    @DeleteMapping("/api/v1/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

}
