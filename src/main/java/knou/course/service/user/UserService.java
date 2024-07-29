package knou.course.service.user;

import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.EmailRequest;
import knou.course.dto.user.request.UserCreateRequest;
import knou.course.dto.user.request.UsernameRequest;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse singUp(UserCreateRequest request) {
        User savedUser = userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));

        return UserResponse.of(savedUser);
    }

    public void checkUsernameDuplication(final String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_USERNAME, ErrorCode.ALREADY_EXIST_USERNAME.getMessage());
        }
    }

    public void checkEmailDuplication(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_EMAIL, ErrorCode.ALREADY_EXIST_EMAIL.getMessage());
        }
    }
}
