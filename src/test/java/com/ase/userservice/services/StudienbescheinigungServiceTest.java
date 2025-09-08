package com.ase.userservice.services;

import com.ase.userservice.entities.User;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudienbescheinigungServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private StudienbescheinigungService studienbescheinigungService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
            "Max",
            "Mustermann",
            LocalDate.of(1995, 5, 15),
            "12345678",
            "Computer Science",
            "Bachelor of Science",
            5,
            6,
            "winter semester 2022/2023",
            "summer semester 2025/2026",
            5,
            0,
            "max.mustermann@example.com"
        );
        testUser.setId(1L);
    }

    @Test
    void testGenerateStudienbescheinigungPdf_Success() {
        // When
        byte[] pdfContent = studienbescheinigungService
                .generateStudienbescheinigungPdf(testUser);

        // Then
        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        // Check PDF header (PDF files start with %PDF)
        String pdfHeader = new String(pdfContent, 0, 4);
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateStudienbescheinigungPdf_NullUser() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studienbescheinigungService
                        .generateStudienbescheinigungPdf(null));

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_Success() {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5}; // Mock PDF content
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        assertDoesNotThrow(() -> studienbescheinigungService
                .sendStudienbescheinigungByEmail(testUser, pdfContent, false));

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendStudienbescheinigungByEmail_NullUser() {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studienbescheinigungService
                        .sendStudienbescheinigungByEmail(null, pdfContent, false));

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_NullPdfContent() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studienbescheinigungService
                        .sendStudienbescheinigungByEmail(testUser, null, false));

        assertEquals("PDF content cannot be null or empty",
                exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_EmptyPdfContent() {
        // Given
        byte[] emptyPdfContent = new byte[0];

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> studienbescheinigungService
                        .sendStudienbescheinigungByEmail(testUser, emptyPdfContent, false));

        assertEquals("PDF content cannot be null or empty",
                exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_MessagingException() {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(RuntimeException.class).when(mailSender).send(mimeMessage);

        // When & Then
        assertThrows(RuntimeException.class, () -> studienbescheinigungService
                .sendStudienbescheinigungByEmail(testUser, pdfContent, false));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendStudienbescheinigungByEmail_EnglishLanguage() {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5}; // Mock PDF content
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        assertDoesNotThrow(() -> studienbescheinigungService
                .sendStudienbescheinigungByEmail(testUser, pdfContent, true));

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testIntegratedPdfGenerationAndEmailSending() {
        // Given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        byte[] pdfContent = studienbescheinigungService
                .generateStudienbescheinigungPdf(testUser);

        // Then
        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        // Check PDF header
        String pdfHeader = new String(pdfContent, 0, 4);
        assertEquals("%PDF", pdfHeader);

        // Test email sending with generated PDF
        assertDoesNotThrow(() -> studienbescheinigungService
                .sendStudienbescheinigungByEmail(testUser, pdfContent, false));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
