package com.ase.userservice.services;

import com.ase.userservice.entities.NachklausurRequest;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class NachklausurService {

  /**
   * Generates a PDF document for a Nachklausur request.
   *
   * @param nachklausurRequest the
   * Nachklausur request for which to generate the PDF
   * @return the PDF content as byte array
   * @throws RuntimeException if request is null or PDF generation fails
   */
  public byte[] generateNachklausurPdf(
      NachklausurRequest nachklausurRequest) {
    if (nachklausurRequest == null) {
      throw new RuntimeException("NachklausurRequest cannot be null");
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
          "Name: %s", nachklausurRequest.getName());
      document.add(new Paragraph(studentInfo).setFontSize(12));

      String matriculationInfo = String.format("Matrikelnummer: %s",
          nachklausurRequest.getMatrikelnummer());
      document.add(new Paragraph(matriculationInfo).setFontSize(12));

      // Exam information
      String modulInfo = String.format(
          "Modul: %s", nachklausurRequest.getModul());
      document.add(new Paragraph(modulInfo).setFontSize(12));

      String examDateInfo = String.format("Pruefungstermin: %s",
          nachklausurRequest.getPruefungstermin());
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
