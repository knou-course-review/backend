package knou.course.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailClient {

    private final JavaMailSender mailSender;

    public boolean sendMail(String to, String subject, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(code);
        mailSender.send(message);

        return true;
    }

    public int createRandomNumber() {
        return 100000 + (int) (Math.random() * 900000);
    }
}
