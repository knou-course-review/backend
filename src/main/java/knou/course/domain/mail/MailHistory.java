package knou.course.domain.mail;

import jakarta.persistence.*;
import knou.course.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "mail_history", // 테이블 이름을 명시적으로 지정 (필요 시)
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "code"})
        }
)
@Entity
public class MailHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private int code;

    private boolean confirm;

    private LocalDateTime registeredDateTime;

    @Builder
    public MailHistory(final String email, final int code, final boolean confirm, final LocalDateTime registeredDateTime) {
        this.email = email;
        this.code = code;
        this.confirm = confirm;
        this.registeredDateTime = registeredDateTime;
    }

    public void updateConfirm(final boolean confirm) {
        this.confirm = confirm;
    }
}
