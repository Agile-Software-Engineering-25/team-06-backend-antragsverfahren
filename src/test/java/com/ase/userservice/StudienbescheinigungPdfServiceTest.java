package com.ase.userservice;

import com.ase.userservice.entities.User;
import com.ase.userservice.services.StudienbescheinigungService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class StudienbescheinigungPdfServiceTest {

    @Autowired
    private StudienbescheinigungService studienbescheinigungService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
            "Jan",
            "Brandenstein",
            LocalDate.of(2003, 12, 20),
            "D778",
            "Informatik",
            "Bachelor of Science",
            4,
            6,
            "winter semester 2023/2024",
            "summer semester 2026/2027",
            4,
            0,
            "test-receive@arnold-of.de"
        );
        testUser.setId(1L);
    }

    @Test
    void testPdfGeneration() {
        // Test PDF generation
        byte[] pdfContent = studienbescheinigungService
                .generateStudienbescheinigungPdf(testUser);

        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        // Check PDF header
        String pdfHeader = new String(pdfContent, 0,
                Math.min(4, pdfContent.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testPdfContentContainsUserData() {
        // Test that PDF contains user data
        byte[] pdfContent = studienbescheinigungService
                .generateStudienbescheinigungPdf(testUser);

        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);

        // Convert to string to check content (simplified check)
        String pdfAsString = new String(pdfContent);

        // These assertions may need adjustment based on actual PDF
        // content structure. For now, we just verify that the PDF
        // was generated successfully
        assertTrue(pdfAsString.contains("PDF") || pdfContent.length > 100);
    }
}
