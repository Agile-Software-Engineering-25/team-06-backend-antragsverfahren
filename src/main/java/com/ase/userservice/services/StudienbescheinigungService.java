package com.ase.userservice.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.ase.userservice.forms.StudentDTO;
import org.springframework.stereotype.Service;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;


@Service
public class StudienbescheinigungService {


  public void sendEmail(
      StudentDTO student, byte[] pdfContent, boolean isEnglish) {
    EmailService.sendCertificateOfEnrollmentByMail(
        student, pdfContent, isEnglish);
  }


  public byte[] generatePdf(
      String semesterName,
      LocalDate semesterStart,
      LocalDate semesterEnd,
      StudentDTO student,
      Integer regularEnrollmentDuration,
      LocalDate enrollmentStart,
      LocalDate enrollmentEnd,
      Integer universitySemester,
      Integer vacationSemester,
      Boolean isEnglish) {

    if (semesterName == null
        || semesterStart == null
        || semesterEnd == null
        || student == null
        || regularEnrollmentDuration == null
        || enrollmentStart == null
        || enrollmentEnd == null
        || universitySemester == null
        || vacationSemester == null
        || isEnglish == null
    ) {
      throw new RuntimeException("Arguments cannot be null!");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
      String title = isEnglish ? "Confirmation of Enrollment" : "Studienbescheinigung";
      document.add(
          new Paragraph(title)
          .setFontSize(18)
          .setBold()
          .setTextAlignment(TextAlignment.CENTER)
      );

      // Validity info
      String validityInfo = String.format(
          isEnglish ? "valid for the %s (%s-%s)" : "gültig für das %s (%s-%s)",
          semesterName, semesterStart.format(formatter), semesterEnd.format(formatter)
      );
      document.add(
          new Paragraph(validityInfo)
          .setFontSize(12)
          .setTextAlignment(TextAlignment.CENTER)
      );

      // Add some space
      document.add(new Paragraph(" "));

      // Student info
      String studentInfo = String.format(
          isEnglish ? "%s %s, date of birth %s, matriculation number %s" : "%s %s, geboren am %s, Matrikelnummer %s",
          student.getFirstName(), student.getLastName(),
          LocalDate.parse(student.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(formatter),
          student.getMatriculationNumber()
      );
      document.add(
          new Paragraph(studentInfo)
              .setFontSize(12)
      );

      // Study program info
      String studyProgramInfo = String.format(
          isEnglish ? "Study programme: %s" : "Studiengang: %s",
          student.getDegreeProgram()
      );
      document.add(
          new Paragraph(studyProgramInfo)
              .setFontSize(12)
      );

      // Degree info
      String degreeInfo = String.format(
          isEnglish ? "Final degree: %s" : "Abschluss: %s",
          student.getDegreeProgram()
      );
      document.add(
          new Paragraph(degreeInfo)
              .setFontSize(12)
      );

      // Semester info
      String semesterInfo = String.format(
          isEnglish ? "Number of semesters in programme: %d" : "Fachsemester: %d",
          student.getDegreeProgram()
      );
      document.add(
          new Paragraph(semesterInfo)
              .setFontSize(12)
      );

      // durationInfo
      String durationInfo = String.format(
          isEnglish ? "Regular enrollment duration: %d" : "Regelstudienzeit: %d",
          regularEnrollmentDuration
      );
      document.add(
          new Paragraph(durationInfo)
              .setFontSize(12)
      );

      // Enrollment start info
      String startInfo = String.format(
          isEnglish ? "Start of enrollment: %s" : "Beginn des Studiums: %s",
          enrollmentStart.format(formatter)
      );
      document.add(
          new Paragraph(startInfo)
              .setFontSize(12)
      );

      // Enrollment end info
      String endInfo = String.format(
          isEnglish ? "Start of enrollment: %s" : "Beginn des Studiums: %s",
          enrollmentEnd.format(formatter)
      );
      document.add(
          new Paragraph(endInfo)
              .setFontSize(12)
      );

      // University semester info
      String universitySemesterInfo = String.format(
          isEnglish ? "Number of university semesters: %d" : "Hochschulsemester: %d",
          universitySemester);
      document.add(
          new Paragraph(universitySemesterInfo)
              .setFontSize(12)
      );

      // Vacation semester info
      String vacationInfo = String.format(
          isEnglish ? "Thereof vacation semesters: %d" : "Davon Urlaubssemester: %d",
          vacationSemester);
      document.add(
          new Paragraph(vacationInfo)
              .setFontSize(12)
      );

      // Add some space
      document.add(new Paragraph(" "));

      // Footer note
      String footer = isEnglish ?
          "Automatically generated confirmation, valid without signature. " +
              "Additions and changes need explicit confirmation." :
          "Diese Bescheinigung wurde maschinell erzeugt und ist ohne Unterschrift gueltig." +
              " Zusaetze und Aenderungen beduerfen der ausdruecklichen Bestaetigung.";
      document.add(new Paragraph(footer)
          .setFontSize(10)
          .setItalic()
      );

      document.add(
          new Paragraph("Frankfurt am Main, " + LocalDate.now().format(formatter))
      );

      document.close();
    }
    catch (Exception e) {
      throw new RuntimeException("Error generating PDF", e);
    }
    return outputStream.toByteArray();
  }
}
