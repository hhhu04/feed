package cat.feed.dto;

import cat.feed.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    private String address;

    private String title;

    private String message;

    public void setInfo(MailDto email, JavaMailSender mailSender, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getAddress());
        message.setFrom(from);
        message.setSubject("요청한 계정의 비밀번호");
        message.setText("111111");

        mailSender.send(message);
    }
}
