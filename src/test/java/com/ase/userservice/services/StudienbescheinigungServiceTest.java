package com.ase.userservice.services;

import com.ase.userservice.entities.User;
import jakarta.mail.MessagingException;
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
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Max");
        testUser.setLastName("Mustermann");
        testUser.setDateOfBirth(LocalDate.of(1995, 5, 15));
        testUser.setMatriculationNumber("12345678");
        testUser.setStudyProgram("Computer Science");
        testUser.setDegree("Bachelor of Science");
        testUser.setCurrentSemester(5);
        testUser.setStandardStudyDuration(6);
        testUser.setStudyStartSemester("winter semester 2022/2023");
        testUser.setUniversitySemester(5);
        testUser.setLeaveOfAbsenceSemesters(0);
        testUser.setEmail("max.mustermann@example.com");
    }

    @Test
    void testGenerateStudienbescheinigungPdf_Success() {
        // When
        byte[] pdfContent = studienbescheinigungService.generateStudienbescheinigungPdf(testUser);

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
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studienbescheinigungService.generateStudienbescheinigungPdf(null);
        });

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_Success() throws MessagingException {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5}; // Mock PDF content
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        assertDoesNotThrow(() -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(testUser, pdfContent);
        });

        // Then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendStudienbescheinigungByEmail_NullUser() {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(null, pdfContent);
        });

        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_NullPdfContent() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(testUser, null);
        });

        assertEquals("PDF content cannot be null or empty", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_EmptyPdfContent() {
        // Given
        byte[] emptyPdfContent = new byte[0];

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(testUser, emptyPdfContent);
        });

        assertEquals("PDF content cannot be null or empty", exception.getMessage());
    }

    @Test
    void testSendStudienbescheinigungByEmail_MessagingException() throws MessagingException {
        // Given
        byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(RuntimeException.class).when(mailSender).send(mimeMessage);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(testUser, pdfContent);
        });

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testIntegratedPdfGenerationAndEmailSending() throws MessagingException {
        // Given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        byte[] pdfContent = studienbescheinigungService.generateStudienbescheinigungPdf(testUser);

        // Then
        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        // Check PDF header
        String pdfHeader = new String(pdfContent, 0, 4);
        assertEquals("%PDF", pdfHeader);

        // Test email sending with generated PDF
        assertDoesNotThrow(() -> {
            studienbescheinigungService.sendStudienbescheinigungByEmail(testUser, pdfContent);
        });

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
