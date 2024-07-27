package knou.course.domain.mail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailHistoryRepository extends JpaRepository<MailHistory, Long> {

    Optional<MailHistory> findByEmailAndCode(String email, int code);
}
