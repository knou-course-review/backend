package knou.course.service.user;

import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
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

import static knou.course.exception.ErrorCode.NOT_FOUND_EMAIL_AUTHENTICATION;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailHistoryRepository mailHistoryRepository;

    @Transactional
    public UserResponse singUp(UserCreateRequest request) {
        checkUsernameDuplication(request.getUsername());
        checkEmailDuplication(request.getEmail());

        MailHistory mailHistory = mailHistoryRepository.findTop1ByEmailOrderById(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage()));

        if (!mailHistory.isConfirm()) {
            throw new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
        }

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
