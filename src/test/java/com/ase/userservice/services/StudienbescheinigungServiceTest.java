package com.ase.userservice.services;

import com.ase.userservice.entities.User;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class StudienbescheinigungServiceTest {

  private static final int BIRTH_YEAR = 1995;
  private static final int BIRTH_MONTH = 5;
  private static final int BIRTH_DAY = 15;
  private static final int CURRENT_SEMESTER = 5;
  private static final int STANDARD_STUDY_DURATION = 6;
  private static final int UNIVERSITY_SEMESTER = 5;
  private static final int LEAVE_OF_ABSENCE = 0;

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
        LocalDate.of(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY),
        "12345678",
        "Computer Science",
        "Bachelor of Science",
        CURRENT_SEMESTER,
        STANDARD_STUDY_DURATION,
        "winter semester 2022/2023",
        "summer semester 2025/2026",
        UNIVERSITY_SEMESTER,
        LEAVE_OF_ABSENCE,
        "test-receive@arnold-of.de"
    );
    testUser.setId(1L);
  }

  @Test
  void testGenerateStudienbescheinigungPdfSuccess() {
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
  void testGenerateStudienbescheinigungPdfNullUser() {
    // When & Then
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> studienbescheinigungService
        .generateStudienbescheinigungPdf(null));

    assertEquals("User cannot be null", exception.getMessage());
  }

//  @Test
//  void testSendStudienbescheinigungByEmail_Success() {
//      // Given
//      byte[] pdfContent = new byte[]{1, 2, 3, 4, 5}; // Mock PDF content
//      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//      // When
//      assertDoesNotThrow(() -> studienbescheinigungService
//              .sendStudienbescheinigungByEmail(testUser, pdfContent, false));
//
//      // Then
//      verify(mailSender, times(1)).createMimeMessage();
//      verify(mailSender, times(1)).send(mimeMessage);
//  }
//
//  @Test
//  void testSendStudienbescheinigungByEmail_NullUser() {
//      // Given
//      byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};
//
//      // When & Then
//      RuntimeException exception = assertThrows(RuntimeException.class,
//              () -> studienbescheinigungService
//                      .sendStudienbescheinigungByEmail(
//                      null, pdfContent, false));
//
//      assertEquals("User cannot be null", exception.getMessage());
//  }
//
//  @Test
//  void testSendStudienbescheinigungByEmail_NullPdfContent() {
//      // When & Then
//      RuntimeException exception = assertThrows(RuntimeException.class,
//              () -> studienbescheinigungService
//                      .sendStudienbescheinigungByEmail(
//                      testUser, null, false));
//
//      assertEquals("PDF content cannot be null or empty",
//              exception.getMessage());
//  }
//
//  @Test
//  void testSendStudienbescheinigungByEmail_EmptyPdfContent() {
//      // Given
//      byte[] emptyPdfContent = new byte[0];
//
//      // When & Then
//      RuntimeException exception = assertThrows(RuntimeException.class,
//              () -> studienbescheinigungService
//                      .sendStudienbescheinigungByEmail(
//                      testUser, emptyPdfContent, false));
//
//      assertEquals("PDF content cannot be null or empty",
//              exception.getMessage());
//  }
//
//  @Test
//  void testSendStudienbescheinigungByEmail_MessagingException() {
//      // Given
//      byte[] pdfContent = new byte[]{1, 2, 3, 4, 5};
//      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//      doThrow(RuntimeException.class).when(mailSender).send(mimeMessage);
//
//      // When & Then
//      assertThrows(RuntimeException.class, () -> studienbescheinigungService
//              .sendStudienbescheinigungByEmail(testUser, pdfContent, false));
//
//      verify(mailSender, times(1)).createMimeMessage();
//      verify(mailSender, times(1)).send(mimeMessage);
//  }
//
//  @Test
//  void testSendStudienbescheinigungByEmail_EnglishLanguage() {
//      // Given
//      byte[] pdfContent = new byte[]{1, 2, 3, 4, 5}; // Mock PDF content
//      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//      // When
//      assertDoesNotThrow(() -> studienbescheinigungService
//              .sendStudienbescheinigungByEmail(testUser, pdfContent, true));
//
//      // Then
//      verify(mailSender, times(1)).createMimeMessage();
//      verify(mailSender, times(1)).send(mimeMessage);
//  }
//
//  @Test
//  void testIntegratedPdfGenerationAndEmailSending() {
//      // Given
//      when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//      // When
//      byte[] pdfContent = studienbescheinigungService
//              .generateStudienbescheinigungPdf(testUser);
//
//      // Then
//      assertNotNull(pdfContent);
//      assertTrue(pdfContent.length > 0);
//
//      // Check PDF header
//      String pdfHeader = new String(pdfContent, 0, 4);
//      assertEquals("%PDF", pdfHeader);
//
//      // Test email sending with generated PDF
//      assertDoesNotThrow(() -> studienbescheinigungService
//              .sendStudienbescheinigungByEmail(testUser, pdfContent, false));
//
//      verify(mailSender, times(1)).createMimeMessage();
//      verify(mailSender, times(1)).send(mimeMessage);
//  }
}
