package com.ase.userservice;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;


@TestConfiguration
public class TestMailConfig {

  @Bean
  @Primary
  public JavaMailSender javaMailSender() {
    return new JavaMailSender() {
      @Override
      public MimeMessage createMimeMessage() {
          Properties props = new Properties();
          Session session = Session.getDefaultInstance(props, null);
          return new MimeMessage(session);
      }

      @Override
      public MimeMessage createMimeMessage(java.io.InputStream contentStream) {
          Properties props = new Properties();
          Session session = Session.getDefaultInstance(props, null);
          return new MimeMessage(session);
      }

      @Override
      public void send(MimeMessage mimeMessage) {
          // Do nothing - mail sending disabled for tests
      }

      @Override
      public void send(MimeMessage... mimeMessages) {
          // Do nothing - mail sending disabled for tests
      }

      @Override
      public void send(
          org.springframework.mail.SimpleMailMessage simpleMessage) {
          // Do nothing - mail sending disabled for tests
      }

      @Override
      public void send(
          org.springframework.mail.SimpleMailMessage... simpleMessages) {
          // Do nothing - mail sending disabled for tests
      }
    };
  }
}
