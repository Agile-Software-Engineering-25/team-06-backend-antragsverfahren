package com.ase.userservice.services;

import com.ase.userservice.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {

  private static final String mailboxAddress = "test-send@arnold-of.de";
  /**
   * Definition of the mail server used for sending emails.
   *
   * @param mailboxAddress the local part of the email address (before the @).
   * @return the object defining the mail sender.
   */
  public static JavaMailSender getJavaMailSender(String mailboxAddress ) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("mail.your-server.de");
    mailSender.setPort(587);

    mailSender.setUsername(mailboxAddress);
    mailSender.setPassword("Team-06-Backend-Antragsverfahren");

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }

  public static void sendBachelorthesisApplicationByMail(User user, byte[] pdfContent, boolean isEnglish) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    if (pdfContent == null || pdfContent.length == 0) {
      throw new RuntimeException("PDF content cannot be null or empty");
    }
    String mailboxAddress = EmailService.mailboxAddress;
    JavaMailSender mailSender = EmailService.getJavaMailSender(mailboxAddress);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);


      InternetAddress fromAddress = new InternetAddress(mailboxAddress, "Hochschulverwaltungssystem");
      helper.setFrom(fromAddress);
      helper.setTo(user.getEmail());

      String subject;
      String emailText;
      String attachmentName;

      if (isEnglish) {
        subject = "Bachelorthesis application - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Dear " + user.getFirstName() + " "
            + user.getLastName() + ",\n\n"
            + "Attached is your student certificate as "
            + "a PDF document.\n\n"
            + "Best regards\n"
            + "Your Student Administration Office";

        attachmentName = "Certificate_of_Enrollment_"
            + user.getMatriculationNumber() + ".pdf";
      } else {
        subject = "Bachelorarbeitsantrag - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Liebe/r " + user.getFirstName() + " "
            + user.getLastName() + ",\n\n"
            + "anbei erhalten Sie Ihre Studienbescheinigung als "
            + "PDF-Dokument.\n\n"
            + "Mit freundlichen Grüßen\n"
            + "Ihr Studierendensekretariat";

        attachmentName = "Studienbescheinigung_"
            + user.getMatriculationNumber() + ".pdf";
      }

      helper.setSubject(subject);
      helper.setText(emailText);

      ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
      helper.addAttachment(attachmentName, pdfResource);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Failed to set email sender name", e);
    }
  }

  public static void sendCertificateOfEnrollmentByMail(User user, byte[] pdfContent, boolean isEnglish) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    if (pdfContent == null || pdfContent.length == 0) {
      throw new RuntimeException("PDF content cannot be null or empty");
    }
    String mailboxAddress = EmailService.mailboxAddress;
    JavaMailSender mailSender = EmailService.getJavaMailSender(mailboxAddress);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);


      InternetAddress fromAddress = new InternetAddress(mailboxAddress, "Hochschulverwaltungssystem");
      helper.setFrom(fromAddress);
      helper.setTo(user.getEmail());

      String subject;
      String emailText;
      String attachmentName;

      if (isEnglish) {
        subject = "Certificate of Enrollment - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Dear " + user.getFirstName() + " "
            + user.getLastName() + ",\n\n"
            + "Attached is your student certificate as "
            + "a PDF document.\n\n"
            + "Best regards\n"
            + "Your Student Administration Office";

        attachmentName = "Certificate_of_Enrollment_"
            + user.getMatriculationNumber() + ".pdf";
      } else {
        subject = "Studienbescheinigung - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Liebe/r " + user.getFirstName() + " "
            + user.getLastName() + ",\n\n"
            + "anbei erhalten Sie Ihre Studienbescheinigung als "
            + "PDF-Dokument.\n\n"
            + "Mit freundlichen Grüßen\n"
            + "Ihr Studierendensekretariat";

        attachmentName = "Studienbescheinigung_"
            + user.getMatriculationNumber() + ".pdf";
      }

      helper.setSubject(subject);
      helper.setText(emailText);

      ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
      helper.addAttachment(attachmentName, pdfResource);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Failed to set email sender name", e);
    }
  }
}
