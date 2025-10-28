package com.ase.userservice.services;

import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class NachklausurService {

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
  public byte[] generateNachklausurPdf(
      String modul, String pruefungstermin, String firstName, String lastName) {
    if (modul == null || pruefungstermin == null || firstName == null || lastName == null) {
      throw new RuntimeException("All parameters are required");
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

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
      String modulInfo = String.format(
          "Modul: %s", modul);
      document.add(new Paragraph(modulInfo).setFontSize(12));

      String examDateInfo = String.format("Pruefungstermin: %s",
          pruefungstermin);
      document.add(new Paragraph(examDateInfo).setFontSize(12));

      // Add some space
      document.add(new Paragraph(" "));

      // Footer note
      String footerText = "Dieser Antrag wurde maschinell erzeugt. "
          + "Bitte ausdrucken, ausfuellen, unterschreiben "
          + "und beim Pruefungsamt einreichen.";
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
