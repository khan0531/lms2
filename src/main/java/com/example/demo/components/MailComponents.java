package com.example.demo.components;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponents {

  private final JavaMailSender javaMailSender;

  public void sendMailTest() {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo("rudgksdl94@gmail.com");
    message.setSubject("테스트 메일입니다.");
    message.setText("테스트 메일 내용입니다.");


    javaMailSender.send(message);
  }

  public boolean sendMail(String mail, String subject, String text) {
    boolean result = false;
    MimeMessagePreparator message = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(mail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
      }
    };
    try {
      javaMailSender.send(message);
      result = true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }
}
