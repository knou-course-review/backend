package knou.course.service.user;

import jakarta.validation.Valid;
import knou.course.domain.mail.MailHistory;
import knou.course.domain.mail.MailHistoryRepository;
import knou.course.domain.user.User;
import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.*;
import knou.course.dto.user.response.UserListResponse;
import knou.course.dto.user.response.UserPagedResponse;
import knou.course.dto.user.response.UserResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import knou.course.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static knou.course.exception.ErrorCode.*;

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

        MailHistory mailHistory = mailHistoryRepository.findTop1ByEmailOrderByIdDesc(request.getEmail())
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
            throw new AppException(ALREADY_EXIST_EMAIL, ALREADY_EXIST_EMAIL.getMessage());
        }
    }

    public UserResponse findUsername(final EmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        MailHistory mailHistory = mailHistoryRepository.findTop1ByEmailOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage()));

        if (!mailHistory.isConfirm()) {
            throw new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
        }

        return UserResponse.of(user);
    }

    public UserResponse findPassword(final UserFindPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        if (!user.getUsername().equals(request.getUsername())) {
            throw new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage());
        }

        MailHistory mailHistory = mailHistoryRepository.findTop1ByEmailOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage()));

        if (!mailHistory.isConfirm()) {
            throw new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
        }

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse changePassword(final UserChangePassword request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        MailHistory mailHistory = mailHistoryRepository.findTop1ByEmailOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage()));

        if (!mailHistory.isConfirm()) {
            throw new AppException(NOT_FOUND_EMAIL_AUTHENTICATION, NOT_FOUND_EMAIL_AUTHENTICATION.getMessage());
        }

        if (!request.getPassword().equals(request.getRePassword())) {
            throw new AppException(NOT_MATCH_PASSWORD, NOT_MATCH_PASSWORD.getMessage());
        }

        user.changePassword(passwordEncoder.encode(request.getPassword()));
        return UserResponse.of(user);
    }

    public UserPagedResponse getAllUsersPaged(Integer page) {
        if (page < 1) {
            page = 1;
        }

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").ascending());
        Page<User> users = userRepository.findAll(pageRequest);

        List<UserListResponse> result = users.getContent()
                .stream()
                .map(UserListResponse::of)
                .toList();

        return UserPagedResponse.of(result, users);
    }

    @Transactional
    public UserResponse updateUserStatus(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        user.changeUserStatus();
        return UserResponse.of(user);
    }

    public UserResponse getLoggedInUser(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse modifyPassword(final UserModifyPasswordRequest request, final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(NOT_FOUND_USER, NOT_FOUND_USER.getMessage()));

        validateNowPassword(request, user);
        validatePasswordMatch(request);

        user.changePassword(passwordEncoder.encode(request.getPassword()));
        return UserResponse.of(user);
    }

    private void validatePasswordMatch(final UserModifyPasswordRequest request) {
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new AppException(NOT_MATCH_PASSWORD, NOT_MATCH_PASSWORD.getMessage());
        }
    }

    private void validateNowPassword(final UserModifyPasswordRequest request, final User user) {
        if (!passwordEncoder.matches(request.getNowPassword(), user.getPassword())) {
            throw new AppException(NOT_MATCH_NOW_PASSWORD, NOT_MATCH_NOW_PASSWORD.getMessage());
        }
    }
}
