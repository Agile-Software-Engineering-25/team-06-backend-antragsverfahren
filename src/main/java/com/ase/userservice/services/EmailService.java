package com.ase.userservice.services;


import java.io.UnsupportedEncodingException;
import com.ase.userservice.forms.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.fromAddress:}")
  private final String fromAddress;

  @Value("${spring.mail.fromName:}")
  private final String fromName;



  public void sendBachelorthesisApplicationByMail(
      StudentDTO user, byte[] pdfContent, boolean isEnglish) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    if (pdfContent == null || pdfContent.length == 0) {
      throw new RuntimeException("PDF content cannot be null or empty");
    }

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      InternetAddress fromAddress = new InternetAddress(
          this.fromAddress, fromName);
      helper.setFrom(fromAddress);
      helper.setTo("test-receive@arnold-of.de");

      String subject;
      String emailText;
      String attachmentName;

      if (isEnglish) {
        subject = "Bachelorthesis application - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Attached is a Bachelorthesis application as "
            + "a PDF document from " + user.getFirstName()
            + " " + user.getLastName() + ".\n\n";

        attachmentName = "Certificate_of_Enrollment_"
            + user.getMatriculationNumber() + ".pdf";
      }
      else {
        subject = "Bachelorarbeitsantrag - " + user.getFirstName()
            + " " + user.getLastName() + " (" + user.getMatriculationNumber() + ")";

        emailText = "Im Anhang befindet sich ein Bachelorthesis Antrag als "
            + "PDF von " + user.getFirstName()
            + " " + user.getLastName() + ".\n\n";

        attachmentName = "Bachelorthesis_Antrag_"
            + user.getMatriculationNumber() + ".pdf";
      }

      helper.setSubject(subject);
      helper.setText(emailText);

      ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
      helper.addAttachment(attachmentName, pdfResource);
      mailSender.send(message);
    }
    catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(
          "Failed to set email sender name", e);
    }
  }

  public void sendNachklausurByMail(
      StudentDTO user, byte[] pdfContent, boolean isEnglish) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    if (pdfContent == null || pdfContent.length == 0) {
      throw new RuntimeException("PDF content cannot be null or empty");
    }

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      InternetAddress fromAddress = new InternetAddress(
          this.fromAddress, fromName);
      helper.setFrom(fromAddress);
      helper.setTo("test-receive@arnold-of.de");

      String subject;
      String emailText;
      String attachmentName;

      if (isEnglish) {
        subject = "Bachelorthesis application - " + user.getFirstName()
            + " " + user.getLastName();

        emailText = "Attached is a Bachelorthesis application as "
            + "a PDF document from " + user.getFirstName()
            + " " + user.getLastName() + ".\n\n";

        attachmentName = "Certificate_of_Enrollment_"
            + user.getMatriculationNumber() + ".pdf";
      }
      else {
        subject = "Nachklausurantrag - " + user.getFirstName()
            + " " + user.getLastName() + " (" + user.getMatriculationNumber() + ")";

        emailText = "Im Anhang befindet sich ein Nachklausurantrag als "
            + "PDF von " + user.getFirstName()
            + " " + user.getLastName() + ".\n\n";

        attachmentName = "Nachklausurantrag_"
            + user.getMatriculationNumber() + ".pdf";
      }

      helper.setSubject(subject);
      helper.setText(emailText);

      ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
      helper.addAttachment(attachmentName, pdfResource);
      mailSender.send(message);
    }
    catch (MessagingException e) {
      throw new RuntimeException("Failed to send email", e);
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(
          "Failed to set email sender name", e);
    }
  }
}
