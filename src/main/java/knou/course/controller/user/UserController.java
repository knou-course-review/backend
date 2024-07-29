package knou.course.controller.user;

import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.user.request.EmailRequest;
import knou.course.dto.user.request.UserCreateRequest;
import knou.course.dto.user.request.UsernameRequest;
import knou.course.dto.user.response.UserResponse;
import knou.course.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> singUp(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok(userService.singUp(request));
    }

    @PostMapping("/duplicate-username")
    public void duplicateUsername(@Valid @RequestBody UsernameRequest request) {
        userService.checkUsernameDuplication(request.getUsername());
    }

    @PostMapping("/duplicate-email")
    public void duplicateEmail(@Valid @RequestBody EmailRequest request) {
        userService.checkEmailDuplication(request.getEmail());
    }
}
