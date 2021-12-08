package cat.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
        message.setSubject("메일 테스트");
        message.setText("test");

        mailSender.send(message);
    }
}
