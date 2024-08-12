package knou.course.controller.admin;

import knou.course.dto.ApiResponse;
import knou.course.dto.user.response.UserPagedResponse;
import knou.course.dto.user.response.UserResponse;
import knou.course.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final UserService userService;

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/api/v1/admin/users")
    public ApiResponse<UserPagedResponse> getAllUsersPaged(@RequestParam(value = "page", defaultValue = "1") Integer page) {
        return ApiResponse.ok(userService.getAllUsersPaged(page));
    }

    @PutMapping("/api/v1/admin/user/{userId}")
    public ApiResponse<UserResponse> updateUserStatus(@PathVariable Long userId) {
        return ApiResponse.ok(userService.updateUserStatus(userId));
    }


}
