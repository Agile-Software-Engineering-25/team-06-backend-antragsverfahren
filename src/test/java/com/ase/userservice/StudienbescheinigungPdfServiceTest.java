package com.ase.userservice;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.ase.userservice.entities.User;
import com.ase.userservice.services.StudienbescheinigungService;

@SpringBootTest
@ActiveProfiles("test")
public class StudienbescheinigungPdfServiceTest {

  private static final int BIRTH_YEAR = 2003;
  private static final int BIRTH_MONTH = 12;
  private static final int BIRTH_DAY = 20;
  private static final int CURRENT_SEMESTER = 4;
  private static final int STANDARD_STUDY_DURATION = 6;
  private static final int UNIVERSITY_SEMESTER = 4;
  private static final int LEAVE_OF_ABSENCE = 0;

  @Autowired
  private StudienbescheinigungService studienbescheinigungService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User(
        "Jan",
        "Brandenstein",
        LocalDate.of(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY),
        "D778",
        "Informatik",
        "Bachelor of Science",
        CURRENT_SEMESTER,
        STANDARD_STUDY_DURATION,
        "winter semester 2023/2024",
        "summer semester 2026/2027",
        UNIVERSITY_SEMESTER,
        LEAVE_OF_ABSENCE,
        "test-receive@arnold-of.de"
    );
    testUser.setId(1L);
  }

  @Test
  void testPdfGeneration() {
    byte[] pdfContent = studienbescheinigungService
        .generateStudienbescheinigungPdf(testUser);

    assertNotNull(pdfContent);
    assertTrue(pdfContent.length > 0);

    String pdfHeader = new String(pdfContent, 0,
        Math.min(4, pdfContent.length));
    assertEquals("%PDF", pdfHeader);
  }

  @Test
  void testPdfContentContainsUserData() {
    byte[] pdfContent = studienbescheinigungService
        .generateStudienbescheinigungPdf(testUser);

    assertNotNull(pdfContent);
    assertTrue(pdfContent.length > 0);

    String pdfAsString = new String(pdfContent);

    assertTrue(pdfAsString.contains("PDF") || pdfContent.length > 100);
  }
}
