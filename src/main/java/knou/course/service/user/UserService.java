package knou.course.service.user;

import knou.course.domain.user.UserRepository;
import knou.course.dto.user.request.UserCreateRequest;
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
    public void singUp(UserCreateRequest request) {
        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }
}
