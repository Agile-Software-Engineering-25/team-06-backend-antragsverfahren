package com.ase.userservice.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import com.ase.userservice.database.entities.NachklausurRequest;
import com.ase.userservice.database.repositories.NachklausurRepository;
import com.ase.userservice.forms.StudentDTO;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

@Service
@RequiredArgsConstructor
public class NachklausurService {

  private final NachklausurRepository nachklausurRepository;
  private final EmailService emailService;

  @Async
  public CompletableFuture<Void> createRequest(
      NachklausurRequest nachklausurRequest) {
    nachklausurRepository.saveAndFlush(nachklausurRequest);
    return CompletableFuture.completedFuture(null);
  }

  public NachklausurRequest getRequestByMatrikelnummer(
      String matrikelnummer) {
    return nachklausurRepository.
        getRequestByMatrikelnummer(matrikelnummer);
  }

  public void deleteRequest(
      Long id) {
    nachklausurRepository.deleteById(id);
  }



  public void sendEmail(
      StudentDTO user, byte[] pdfContent, boolean isEnglish) {
    emailService.sendNachklausurByMail(
        user, pdfContent, isEnglish);
  }
  /**
   * Generates a PDF document for a Nachklausur request.
   *
   * @param modul the module name from frontend
   * @param pruefungstermin the exam date from frontend
   * @param firstName the student's first name from API
   * @param lastName the student's last name from API
   * @return the PDF content as byte array
   * @throws RuntimeException if PDF generation fails
   */
  public byte[] generatePdf(
      String modul, String pruefungstermin, String firstName, String lastName, String matriculationNumber, String group) {
    if (modul == null || pruefungstermin == null || firstName == null || lastName == null) {
      throw new RuntimeException("All parameters are required");
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

      //Logo
      String logoPath = "src/main/resources/provadis_logo.jpeg";
      ImageData imageData = ImageDataFactory.create(logoPath);
      Image logo = new Image(imageData)
          .scaleToFit(100, 100)
          .setFixedPosition(pdf.getDefaultPageSize().getWidth() - 120,
          pdf.getDefaultPageSize().getHeight() - 50);
      document.add(logo);

      // Title
      Paragraph title = new Paragraph("Antrag auf Nachklausur")
          .setFontSize(18)
          .setBold()
          .setTextAlignment(TextAlignment.CENTER);
      document.add(title);

      // Add some space
      document.add(new Paragraph(" "));

      // Student information
      String studentInfo = String.format(
          "Name: %s %s", firstName, lastName);
      document.add(new Paragraph(studentInfo).setFontSize(12));

      // Exam information
      String matriculationNumberInfo = String.format(
          "Matrikelnummer: %s", matriculationNumber);
      document.add(new Paragraph(matriculationNumberInfo).setFontSize(12));

      // Exam information
      String groupInfo = String.format(
          "Gruppe: %s", group);
      document.add(new Paragraph(groupInfo).setFontSize(12));

      // Exam information
      String modulInfo = String.format(
          "Modul: %s", modul);
      document.add(new Paragraph(modulInfo).setFontSize(12));


      String examDateInfo = String.format("Prüfungstermin: %s",
          pruefungstermin);
      document.add(new Paragraph(examDateInfo).setFontSize(12));

      // Add some space
      document.add(new Paragraph(" "));

      String footerText = "Erstellt am " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
      Paragraph footer = new Paragraph(footerText)
          .setFontSize(10)
          .setItalic();
      document.add(footer);
      document.close();

      return outputStream.toByteArray();
    }
    catch (Exception e) {
      throw new RuntimeException("Failed to generate Nachklausur PDF", e);
    }
  }
}
