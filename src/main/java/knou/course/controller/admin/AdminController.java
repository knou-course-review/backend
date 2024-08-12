package knou.course.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import knou.course.dto.ApiResponse;
import knou.course.dto.user.response.UserPagedResponse;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.ErrorCode;
import knou.course.service.user.UserService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Controller - 어드민 전용 컨트롤러", description = "회원 목록 조회, 회원 정지 기능")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @Operation(summary = "유저 페이징 조회 - size 10 고정", description = "유저를 페이징 조회합니다. <br> 유저 정보는 data.content로 접근해주세요.")
    @GetMapping("/api/v1/admin/users")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, ErrorCode.INVALID_INPUT_VALUE})
    public ApiResponse<UserPagedResponse> getAllUsersPaged(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ApiResponse.ok(userService.getAllUsersPaged(page));
    }

    @Operation(summary = "유저 상태 변경", description = "유저 계정 상태를 ACTIVE, INACTIVE로 변경합니다.")
    @PutMapping("/api/v1/admin/user/{userId}")
    @ApiErrorCodeExamples({ErrorCode.NOT_ADMIN, ErrorCode.INVALID_INPUT_VALUE})
    public ApiResponse<UserResponse> updateUserStatus(@PathVariable Long userId) {
        return ApiResponse.ok(userService.updateUserStatus(userId));
    }


}
