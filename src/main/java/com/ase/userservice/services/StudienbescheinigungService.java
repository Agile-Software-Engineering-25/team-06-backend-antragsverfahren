package com.ase.userservice.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.ase.userservice.forms.StudentDTO;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
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

  public byte[] generatePdf(
      String semesterName,
      LocalDate semesterStart,
      LocalDate semesterEnd,
      StudentDTO student,
      String degree,
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
        || degree == null
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
    int tableWidthPercent = 80;

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

      document.setMargins(72,72,72,72);

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
              .setTextAlignment(TextAlignment.LEFT)
              .setHorizontalAlignment(HorizontalAlignment.CENTER)
              .setWidth(UnitValue.createPercentValue(tableWidthPercent))
      );


      float[] columnWidths = {1, 2}; // ratio between left and right column widths
      Table table = new Table(columnWidths);
      table.setWidth(UnitValue.createPercentValue(tableWidthPercent));
      table.setHorizontalAlignment(HorizontalAlignment.CENTER);

      // Study program info
      String studyProgramInfo = String.format(isEnglish ? "Study programme: " : "Studiengang: ");
      addRow(table, studyProgramInfo, student.getDegreeProgram());


      // Degree info
      String degreeInfo = String.format(isEnglish ? "Final degree: " : "Abschluss: ");
      addRow(table, degreeInfo, degree);

      // Semester info
      String semesterInfo = String.format(isEnglish ? "Number of semesters in programme: " : "Fachsemester: ");
      addRow(table, semesterInfo, student.getSemester().toString());

      // durationInfo
      String durationInfo = String.format(isEnglish ? "Regular enrollment duration: " : "Regelstudienzeit: ");
      addRow(table, durationInfo, regularEnrollmentDuration.toString());

      // Enrollment start info
      String startInfo = String.format(isEnglish ? "Start of enrollment: " : "Beginn des Studiums: ");
      addRow(table, startInfo, enrollmentStart.format(formatter));

      // Enrollment end info
      String endInfo = String.format(isEnglish ? "Projected end of enrollment: " : "Voraussichtliches Ende des Studiums: ");
      addRow(table, endInfo, enrollmentEnd.format(formatter));

      // University semester info
      String universitySemesterInfo = String.format(
          isEnglish ? "Number of university semesters: " : "Hochschulsemester: "
      );
      addRow(table, universitySemesterInfo, universitySemester.toString());

      // Vacation semester info
      String vacationInfo = String.format(isEnglish ? "Thereof vacation semesters: " : "Davon Urlaubssemester: ");
      addRow(table, vacationInfo, vacationSemester.toString());

      document.add(table);

      // Add some space
      document.add(new Paragraph(" "));
      document.add(new Paragraph(" "));

      // Add date and city
      document.add(
          new Paragraph("Frankfurt am Main, " + LocalDate.now().format(formatter))
              .setTextAlignment(TextAlignment.LEFT)
              .setHorizontalAlignment(HorizontalAlignment.CENTER)
              .setWidth(UnitValue.createPercentValue(tableWidthPercent))
      );

      // Footer note
      String footer = isEnglish ?
          "Automatically generated confirmation, valid without signature. " +
              "Additions and changes need explicit confirmation." :
          "Diese Bescheinigung wurde maschinell erzeugt und ist ohne Unterschrift gültig." +
              " Zusätze und Änderungen bedürfen der ausdrücklichen Bestätigung.";
      PdfPage page = document.getPdfDocument().getLastPage();
      Rectangle pageSize = page.getPageSize();
      Rectangle footerArea = new Rectangle(
          document.getLeftMargin(),
          pageSize.getBottom() + 20, // distance from bottom
          pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin(),
          50 // height of footer area
      );

      PdfCanvas pdfCanvas = new PdfCanvas(page);
      Canvas canvas = new Canvas(pdfCanvas, footerArea);


      canvas.add(new Paragraph(footer)
          .setFontSize(10)
          .setItalic()
          .setTextAlignment(TextAlignment.CENTER)
      );

      canvas.close();
      document.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error generating PDF", e);
    }
    return outputStream.toByteArray();
  }

  private void addRow(Table table, String label, String value) {
    // Left cell: label
    Cell labelCell = new Cell()
        .add(new Paragraph(label)
            .setFontSize(12)
            .setTextAlignment(TextAlignment.LEFT))
        .setBorder(Border.NO_BORDER)
        .setPaddingBottom(5);

    // Right cell: value
    Cell valueCell = new Cell()
        .add(new Paragraph(value != null ? value : "")
            .setFontSize(12)
            .setTextAlignment(TextAlignment.RIGHT))
        .setBorder(Border.NO_BORDER)
        .setPaddingBottom(5);

    table.addCell(labelCell);
    table.addCell(valueCell);
  }


}
