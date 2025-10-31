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
import static java.lang.System.in;
import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.fromAddress:}")
  private String fromAddress;

  @Value("${spring.mail.fromName:}")
  private String fromName;



  public void sendBachelorthesisApplicationByMail(
      StudentDTO student, byte[][] pdfContents, boolean isEnglish) {
    if (student == null) {
      throw new RuntimeException("Student cannot be null");
    }
    if (pdfContents == null || pdfContents.length == 0) {
      throw new RuntimeException("No PDF attached!");
    }
    if ( pdfContents.length == 1) {
      throw new RuntimeException("Expose missing!");
    }
    if ( pdfContents.length > 2) {
      throw new RuntimeException("Too many pdf attachments!");
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
      String[] attachmentName;

      if (isEnglish) {
        subject = "Bachelorthesis application - " + student.getFirstName()
            + " " + student.getLastName();

        emailText = "Attached is a Bachelorthesis application as "
            + "a PDF document from " + student.getFirstName()
            + " " + student.getLastName() + ".\n\n";

        attachmentName = new String[]{
            "Bachelorthesis_Application_" + student.getMatriculationNumber() + ".pdf",
            "Bachelorthesis_Expose_" + student.getMatriculationNumber() + ".pdf"
        };
      }
      else {
        subject = "Bachelorarbeitsantrag - " + student.getFirstName()
            + " " + student.getLastName() + " (" + student.getMatriculationNumber() + ")";

        emailText = "Im Anhang befindet sich ein Bachelorthesis Antrag als "
            + "PDF von " + student.getFirstName()
            + " " + student.getLastName() + ".\n\n";

        attachmentName = new String[]{
            "Bachelorthesis_Antrag_" + student.getMatriculationNumber() + ".pdf",
            "Bachelorthesis_Expose_" + student.getMatriculationNumber() + ".pdf"
        };
      }

      helper.setSubject(subject);
      helper.setText(emailText);


      int index = 0;
      for(byte[] pdf : pdfContents) {

        if (pdf == null || pdf.length == 0) {
          throw new RuntimeException("Content of PDF " + index + " cannot be null or empty");
        }

        ByteArrayResource pdfResource = new ByteArrayResource(pdf);
        helper.addAttachment(attachmentName[index++], pdfResource);
      }
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
