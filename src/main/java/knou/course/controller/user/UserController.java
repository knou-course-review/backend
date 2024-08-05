package knou.course.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.user.request.*;
import knou.course.dto.user.response.UserResponse;
import knou.course.service.user.UserService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static knou.course.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_EMAIL_AUTHENTICATION, ALREADY_EXIST_USERNAME, ALREADY_EXIST_EMAIL})
    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> singUp(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok(userService.singUp(request));
    }

    @Operation(summary = "아이디 중복검사", description = "아이디 중복검사입니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, ALREADY_EXIST_USERNAME})
    @PostMapping("/duplicate-username")
    public void duplicateUsername(@Valid @RequestBody UsernameRequest request) {
        userService.checkUsernameDuplication(request.getUsername());
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복검사입니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, ALREADY_EXIST_EMAIL})
    @PostMapping("/duplicate-email")
    public void duplicateEmail(@Valid @RequestBody EmailRequest request) {
        userService.checkEmailDuplication(request.getEmail());
    }

    @Operation(summary = "아이디 찾기", description = "가입한 이메일로 아이디를 찾습니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_USER})
    @PostMapping("/find-username")
    public ApiResponse<UserResponse> findUsername(@Valid @RequestBody EmailRequest request) {
        return ApiResponse.ok(userService.findUsername(request));
    }

    @Operation(summary = "비밀번호 재설정을 위한 아이디와 이메일 인증 확인", description = "가입한 이메일과 아이디로 이메일 인증이 되었는지 확인한다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_USER})
    @PostMapping("/find-password")
    public ApiResponse<UserResponse> findPassword(@Valid @RequestBody UserFindPasswordRequest request) {
        return ApiResponse.ok(userService.findPassword(request));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_USER})
    @PutMapping("/change-password")
    public ApiResponse<UserResponse> changePassword(@Valid @RequestBody UserChangePassword request) {
        return ApiResponse.ok(userService.changePassword(request));
    }
}
