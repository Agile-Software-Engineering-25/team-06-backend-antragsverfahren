package com.ase.userservice.services;

import com.ase.userservice.entities.Semester;
import com.ase.userservice.entities.User;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;


@Service
public class StudienbescheinigungService {

  private String mailboxName = "test-send";

  /**
   * Generates the semester validity text
   * from the user's current semester entity.
   *
   * @param user the user whose semester information to use
   * @return the validity text for the semester
   * or null if no semester is assigned
   */
  private String getSemesterValidityText(User user) {
    Semester semester = user.getCurrentSemesterEntity();
    if (semester == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String startDate = semester.getSemesterStart().format(formatter);
    String endDate = semester.getSemesterEnd().format(formatter);
    return String.format("gueltig fuer %s (%s-%s)",
        semester.getSemesterName(), startDate, endDate);
  }

  private String getSemesterValidityTextEn(User user) {
    Semester semester = user.getCurrentSemesterEntity();
    if (semester == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String startDate = semester.getSemesterStart().format(formatter);
    String endDate = semester.getSemesterEnd().format(formatter);
    return String.format("valid for %s (%s-%s)",
        semester.getSemesterName(), startDate, endDate);
  }

  /**
   * Generates a PDF document for a student certificate.
   *
   * @param user the user for whom to generate the certificate
   * @return the PDF content as byte array
   * @throws RuntimeException if user is null or PDF generation fails
   */
  public byte[] generateStudienbescheinigungPdf(User user) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);
      // Logo
      String logoPath = "src/main/resources/provadis_logo.jpeg";
      ImageData imageData = ImageDataFactory.create(logoPath);
      Image logo = new Image(imageData)
          .scaleToFit(100, 100)
          .setFixedPosition(pdf.getDefaultPageSize().getWidth() - 120,
          pdf.getDefaultPageSize().getHeight() - 50);
      document.add(logo);
      // Title
      Paragraph title = new Paragraph("Studienbescheinigung")
          .setFontSize(18)
          .setBold()
          .setTextAlignment(TextAlignment.CENTER);
      document.add(title);
      // Current semester info - now from database
      String validityText = getSemesterValidityText(user);
      if (validityText != null && !validityText.trim().isEmpty()) {
        Paragraph semesterInfo = new Paragraph(validityText)
            .setFontSize(12)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(semesterInfo);
      }
      // Add some space
      document.add(new Paragraph(" "));
      // Student information
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      String birthDate = user.getDateOfBirth().format(formatter);
      String studentInfo = String.format("%s %s, geboren am %s, "
          + "Matrikelnummer %s", user.getFirstName(),
          user.getLastName(), birthDate, user.getMatriculationNumber());
      document.add(new Paragraph(studentInfo).setFontSize(12));
      String studyProgramInfo = String.format("Studiengang: %s",
          user.getStudyProgram());
      document.add(new Paragraph(studyProgramInfo).setFontSize(12));
      String degreeInfo = String.format("Abschluss: %s", user.getDegree());
      document.add(new Paragraph(degreeInfo).setFontSize(12));
      String semesterInfo = String.format("Fachsemester: %d",
          user.getCurrentSemester());
      document.add(new Paragraph(semesterInfo).setFontSize(12));
      String durationInfo = String.format("Regelstudienzeit: %d",
          user.getStandardStudyDuration());
      document.add(new Paragraph(durationInfo).setFontSize(12));
      String startInfo = String.format("Beginn des Studiums: %s",
          user.getStudyStartSemester());
      document.add(new Paragraph(startInfo).setFontSize(12));
      String endInfo = String.format("Ende des Studiums: %s",
          user.getStudyEndSemester());
      document.add(new Paragraph(endInfo).setFontSize(12));
      String universitySemesterInfo = String.format("Hochschulsemester: %d",
          user.getUniversitySemester());
      document.add(new Paragraph(universitySemesterInfo).setFontSize(12));
      String leaveInfo = String.format("Davon Urlaubssemester: %d",
          user.getLeaveOfAbsenceSemesters());
      document.add(new Paragraph(leaveInfo).setFontSize(12));
      // Add some space
      document.add(new Paragraph(" "));
      // Footer note
      String footerText = "Diese Bescheinigung wurde maschinell erzeugt "
          + "und ist ohne Unterschrift gueltig. Zusaetze und "
          + "Aenderungen beduerfen der ausdruecklichen Bestaetigung.";
      Paragraph footer = new Paragraph(footerText)
          .setFontSize(10)
          .setItalic();
      document.add(footer);
      document.close();
    }
    catch (Exception e) {
      throw new RuntimeException("Error generating PDF", e);
    }
    return outputStream.toByteArray();
  }

  public byte[] generateStudienbescheinigungPdfEn(User user) {
    if (user == null) {
      throw new RuntimeException("User cannot be null");
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);
      // Title
      Paragraph title = new Paragraph("Confirmation of enrollment")
          .setFontSize(18)
          .setBold()
          .setTextAlignment(TextAlignment.CENTER);
      document.add(title);
      // Current semester info - now from database
      String validityText = getSemesterValidityTextEn(user);
      if (validityText != null && !validityText.trim().isEmpty()) {
        Paragraph semesterInfo = new Paragraph(validityText)
            .setFontSize(12)
            .setTextAlignment(TextAlignment.CENTER);
        document.add(semesterInfo);
      }
      // Add some space
      document.add(new Paragraph(" "));
      // Student information
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
      String birthDate = user.getDateOfBirth().format(formatter);
      String studentInfo = String.format("%s %s, date of birth %s, "
          + "matriculation number %s", user.getFirstName(),
          user.getLastName(), birthDate, user.getMatriculationNumber());
      document.add(new Paragraph(studentInfo).setFontSize(12));
      String studyProgramInfo = String.format("Study programm: %s",
          user.getStudyProgram());
      document.add(new Paragraph(studyProgramInfo).setFontSize(12));
      String degreeInfo = String.format("Final degree: %s", user.getDegree());
      document.add(new Paragraph(degreeInfo).setFontSize(12));
      String semesterInfo = String.format("Number of semesters in programm: %d",
          user.getCurrentSemester());
      document.add(new Paragraph(semesterInfo).setFontSize(12));
      String durationInfo = String.format("Regular study time: %d",
          user.getStandardStudyDuration());
      document.add(new Paragraph(durationInfo).setFontSize(12));
      String startInfo = String.format("Start of study: %s",
          user.getStudyStartSemester());
      document.add(new Paragraph(startInfo).setFontSize(12));
      String endInfo = String.format("End of study: %s",
          user.getStudyEndSemester());
      document.add(new Paragraph(endInfo).setFontSize(12));
      String universitySemesterInfo = String.format(
          "Number of university semesters: %d",
          user.getUniversitySemester());
      document.add(new Paragraph(universitySemesterInfo).setFontSize(12));
      String leaveInfo = String.format("Thereof vacation semesters: %d",
          user.getLeaveOfAbsenceSemesters());
      document.add(new Paragraph(leaveInfo).setFontSize(12));
      // Add some space
      document.add(new Paragraph(" "));
      // Footer note
      String footerText = "Automatically generated confirmation, "
          + "valid without signature. "
          + "Additions and changes need explicit confirmation ";
      Paragraph footer = new Paragraph(footerText)
          .setFontSize(10)
          .setItalic();
      document.add(footer);
      document.close();
    }
    catch (Exception e) {
      throw new RuntimeException("Error generating PDF", e);
    }
    return outputStream.toByteArray();
  }

  /**
   * Sends a student certificate by email.
   *
   * @param user the user to send the email to
   * @param pdfContent the PDF content to attach
   * @param isEnglish whether to send the email
   * in English (true) or German (false)
   * @throws RuntimeException if user or pdfContent is null/empty
   */
  public void sendStudienbescheinigungByEmail(
      User user, byte[] pdfContent, boolean isEnglish) {
    EmailService.sendCertificateOfEnrollmentByMail(
        user, pdfContent, isEnglish);
  }
}
