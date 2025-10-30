package com.ase.userservice.services;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import com.ase.userservice.forms.StudentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.ase.userservice.database.entities.BachelorthesisRequest;
import com.ase.userservice.database.repositories.BachelorthesisRepository;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

@Service
@RequiredArgsConstructor
public class BachelorthesisService {

  private final BachelorthesisRepository bachelorthesisRepository;
  private final EmailService emailService;

//  @Autowired
//  public BachelorthesisService(
//      BachelorthesisRepository bachelorthesisRepository) {
//    this.bachelorthesisRepository = bachelorthesisRepository;
//  }

  @Async
  public CompletableFuture<Void> createRequest(
      BachelorthesisRequest bachelorthesisRequest) {
    bachelorthesisRepository.saveAndFlush(bachelorthesisRequest);
    return CompletableFuture.completedFuture(null);
  }

  public BachelorthesisRequest getRequestByMatrikelnummer(
      String matrikelnummer) {
    return bachelorthesisRepository.
        getRequestByMatrikelnummer(matrikelnummer);
  }

  public void deleteRequest(
      Long id) {
    bachelorthesisRepository.deleteById(id);
  }


  public void sendEmail(
      StudentDTO user, byte[] pdfContent, boolean isEnglish) {
    emailService.sendBachelorthesisApplicationByMail(
        user, pdfContent, isEnglish);
  }

  /**
   * Generates a PDF document for a bachelor thesis application using parameters.
   *
   * @param name the student's name
   * @param matrikelnummer the student's matriculation number
   * @param studiengang the study program
   * @param thema the thesis topic
   * @param pruefer the examiner/supervisor
   * @param pruefungstermin the exam date
   * @return the PDF content as byte array
   * @throws RuntimeException if any parameter is null or PDF generation fails
   */
  public byte[] generatePdf(
      String name,
      String matrikelnummer,
      String studiengang,
      String thema,
      String pruefer,
      String pruefungstermin) {

    if (name == null || matrikelnummer == null || studiengang == null
        || thema == null || pruefer == null || pruefungstermin == null) {
      throw new RuntimeException("All parameters must be non-null");
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
      Paragraph title = new Paragraph("Antrag auf Zulassung zur Bachelorarbeit")
          .setFontSize(18)
          .setBold()
          .setTextAlignment(TextAlignment.CENTER);
      document.add(title);

      // Add some space
      document.add(new Paragraph(" "));

      // Student information
      String studentInfo = String.format("Name: %s", name);
      document.add(new Paragraph(studentInfo).setFontSize(12));

      String matriculationInfo = String.format("Matrikelnummer: %s", matrikelnummer);
      document.add(new Paragraph(matriculationInfo).setFontSize(12));

      String studyProgramInfo = String.format("Studiengang: %s", studiengang);
      document.add(new Paragraph(studyProgramInfo).setFontSize(12));

      // Add some space
      document.add(new Paragraph(" "));

      // Thesis information
      Paragraph thesisHeader = new Paragraph("Angaben zur Bachelorarbeit")
          .setFontSize(14)
          .setBold();
      document.add(thesisHeader);

      String themaInfo = String.format("Thema: %s", thema);
      document.add(new Paragraph(themaInfo).setFontSize(12));

      String prueferInfo = String.format("Prüfer/Betreuer: %s", pruefer);
      document.add(new Paragraph(prueferInfo).setFontSize(12));

      String terminInfo = String.format("Prüfungstermin: %s", pruefungstermin);
      document.add(new Paragraph(terminInfo).setFontSize(12));

      // Add some space
      document.add(new Paragraph(" "));

      document.close();

      return outputStream.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to generate Bachelorthesis PDF", e);
    }
  }
}
