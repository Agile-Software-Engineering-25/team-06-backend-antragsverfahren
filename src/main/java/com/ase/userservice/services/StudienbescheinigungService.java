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

    @Value("${semester.validity.summer:gueltig fuer Summer Semester 2025 "
            + "(10.06.2025-24.11.2025)}")
    private String summerSemesterValidity;

    @Value("${semester.validity.winter:gueltig fuer Winter Semester 2025/2026 "
            + "(01.10.2025-31.03.2026)}")
    private String winterSemesterValidity;

    /**
     * Determines the current semester validity text based on the current date.
     *
     * @return the validity text for the current semester
     */
    private String getCurrentSemesterValidityText() {
        LocalDate now = LocalDate.now();

        // Handle null values (can happen in unit tests)
        String summer = summerSemesterValidity != null ? summerSemesterValidity
                : "gueltig fuer Summer Semester 2025 (10.06.2025-24.11.2025)";
        String winter = winterSemesterValidity != null ? winterSemesterValidity
                : "gueltig fuer Winter Semester 2025/2026 (01.10.2025-31.03.2026)";

        // Summer semester: April 1 - September 30
        // Winter semester: October 1 - March 31 (next year)
        if (now.getMonth().getValue() >= Month.APRIL.getValue()
                && now.getMonth().getValue() <= Month.SEPTEMBER.getValue()) {
            return summer;
        } else {
            return winter;
        }
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

            String studentInfo = String.format("%s %s, geboren am %s, "
                    + "Matrikelnummer %s", user.getFirstName(),
                    user.getLastName(), birthDate, user.getMatriculationNumber());
            document.add(new Paragraph(studentInfo).setFontSize(12));

            String studyProgramInfo = String.format("Studiengang %s",
                    user.getStudyProgram());
            document.add(new Paragraph(studyProgramInfo).setFontSize(12));

            String degreeInfo = String.format("Abschluss %s", user.getDegree());
            document.add(new Paragraph(degreeInfo).setFontSize(12));

            String semesterInfo = String.format("Fachsemester %d",
                    user.getCurrentSemester());
            document.add(new Paragraph(semesterInfo).setFontSize(12));

            String durationInfo = String.format("Regelstudienzeit %d",
                    user.getStandardStudyDuration());
            document.add(new Paragraph(durationInfo).setFontSize(12));

            String startInfo = String.format("Beginn des Studiums %s",
                    user.getStudyStartSemester());
            document.add(new Paragraph(startInfo).setFontSize(12));

            String universitySemesterInfo = String.format("Hochschulsemester %d",
                    user.getUniversitySemester());
            document.add(new Paragraph(universitySemesterInfo).setFontSize(12));

            String leaveInfo = String.format("davon Urlaubssemester %d",
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

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return outputStream.toByteArray();
    }

    /**
     * Sends a student certificate by email.
     *
     * @param user the user to send the email to
     * @param pdfContent the PDF content to attach
     * @throws RuntimeException if user or pdfContent is null/empty
     */
    public void sendStudienbescheinigungByEmail(User user, byte[] pdfContent) {
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

            String subject = "Studienbescheinigung - " + user.getFirstName()
                    + " " + user.getLastName();
            helper.setSubject(subject);

            String emailText = "Liebe/r " + user.getFirstName() + " "
                    + user.getLastName() + ",\n\n"
                    + "anbei erhalten Sie Ihre Studienbescheinigung als "
                    + "PDF-Dokument.\n\n"
                    + "Mit freundlichen Grüßen\n"
                    + "Ihr Studierendensekretariat";
            helper.setText(emailText);

            ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
            String attachmentName = "Studienbescheinigung_"
                    + user.getMatriculationNumber() + ".pdf";
            helper.addAttachment(attachmentName, pdfResource);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
