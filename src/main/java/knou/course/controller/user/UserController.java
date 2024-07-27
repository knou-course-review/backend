package knou.course.controller.user;

import knou.course.dto.user.request.UserCreateRequest;
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
    public String singUp(@RequestBody UserCreateRequest request) {
        userService.singUp(request);
        return "OK";
    }
}