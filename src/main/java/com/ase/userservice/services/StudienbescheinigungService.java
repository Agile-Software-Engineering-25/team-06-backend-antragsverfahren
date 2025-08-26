package com.ase.userservice.services;

import com.ase.userservice.entities.User;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@Service
public class StudienbescheinigungService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${semester.validity.summer:gueltig fuer Summer Semester 2025 (10.06.2025-24.11.2025)}")
    private String summerSemesterValidity;

    @Value("${semester.validity.winter:gueltig fuer Winter Semester 2025/2026 (01.10.2025-31.03.2026)}")
    private String winterSemesterValidity;

    /**
     * Determines the current semester validity text based on the current date
     */
    private String getCurrentSemesterValidityText() {
        LocalDate now = LocalDate.now();

        // Handle null values (can happen in unit tests)
        String summer = summerSemesterValidity != null ? summerSemesterValidity : "gueltig fuer Summer Semester 2025 (10.06.2025-24.11.2025)";
        String winter = winterSemesterValidity != null ? winterSemesterValidity : "gueltig fuer Winter Semester 2025/2026 (01.10.2025-31.03.2026)";

        // Summer semester: April 1 - September 30
        // Winter semester: October 1 - March 31 (next year)
        if (now.getMonth().getValue() >= Month.APRIL.getValue() &&
            now.getMonth().getValue() <= Month.SEPTEMBER.getValue()) {
            return summer;
        } else {
            return winter;
        }
    }

    public byte[] generateStudienbescheinigungPdf(User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            Paragraph title = new Paragraph("Studienbescheinigung")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Current semester info - now configurable with null check
            String validityText = getCurrentSemesterValidityText();
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

            document.add(new Paragraph(String.format("%s %s, geboren am %s, Matrikelnummer %s",
                user.getFirstName(), user.getLastName(), birthDate, user.getMatriculationNumber()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Studiengang %s", user.getStudyProgram()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Abschluss %s", user.getDegree()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Fachsemester %d", user.getCurrentSemester()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Regelstudienzeit %d", user.getStandardStudyDuration()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Beginn des Studiums %s", user.getStudyStartSemester()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("Hochschulsemester %d", user.getUniversitySemester()))
                .setFontSize(12));

            document.add(new Paragraph(String.format("davon Urlaubssemester %d", user.getLeaveOfAbsenceSemesters()))
                .setFontSize(12));

            // Add some space
            document.add(new Paragraph(" "));

            // Footer note
            Paragraph footer = new Paragraph("Diese Bescheinigung wurde maschinell erzeugt und ist ohne Unterschrift gueltig. " +
                "Zusaetze und Aenderungen beduerfen der ausdruecklichen Bestaetigung.")
                .setFontSize(10)
                .setItalic();
            document.add(footer);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return outputStream.toByteArray();
    }

    public void sendStudienbescheinigungByEmail(User user, byte[] pdfContent) throws MessagingException {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }
        if (pdfContent == null || pdfContent.length == 0) {
            throw new RuntimeException("PDF content cannot be null or empty");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Studienbescheinigung - " + user.getFirstName() + " " + user.getLastName());
            helper.setText("Liebe/r " + user.getFirstName() + " " + user.getLastName() + ",\n\n" +
                "anbei erhalten Sie Ihre Studienbescheinigung als PDF-Dokument.\n\n" +
                "Mit freundlichen Grüßen\n" +
                "Ihr Studierendensekretariat");

            ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
            helper.addAttachment("Studienbescheinigung_" + user.getMatriculationNumber() + ".pdf", pdfResource);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
