package com.ase.userservice;

import com.ase.userservice.controllers.DocumentController;
import com.ase.userservice.entities.User;
import com.ase.userservice.services.StudienbescheinigungService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class StudienbescheinigungIntegrationTest {

    @Autowired
    private DocumentController documentController;

    @Autowired
    private StudienbescheinigungService studienbescheinigungService;

    @Test
    public void testSendStudienbescheinigungPdf() {
        // Test PDF generation endpoint
        ResponseEntity<byte[]> response = documentController
                .sendStudienbescheinigung("de");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Verify PDF content type
        assertNotNull(response.getHeaders().getContentType());
        String expectedContentType = "application/pdf";
        String actualContentType = response.getHeaders().getContentType()
                .getType() + "/" + response.getHeaders().getContentType()
                .getSubtype();
        assertEquals(expectedContentType, actualContentType);

        // Verify Content-Disposition header (check if it contains the filename)
        String contentDisposition = response.getHeaders()
                .getFirst("Content-Disposition");
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.contains("studienbescheinigung.pdf"));
    }

    @Test
    public void testPdfContentValidation() {
        // Generate PDF directly from service
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Max");
        testUser.setLastName("Mustermann");
        testUser.setMatriculationNumber("TEST123");
        testUser.setEmail("max.mustermann@example.com");
        testUser.setDateOfBirth(java.time.LocalDate.of(1995, 5, 15));
        testUser.setStudyProgram("Computer Science");
        testUser.setDegree("Bachelor of Science");
        testUser.setCurrentSemester(5);
        testUser.setStandardStudyDuration(6);
        testUser.setStudyStartSemester("winter semester 2022/2023");
        testUser.setUniversitySemester(5);
        testUser.setLeaveOfAbsenceSemesters(0);

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
    public void testStudienbescheinigungEndpointReturnsValidPdf() {
        // Test that the endpoint returns a valid PDF file
        ResponseEntity<byte[]> response = documentController
                .sendStudienbescheinigung("de");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);

        // Check PDF header
        byte[] pdfContent = response.getBody();
        String pdfHeader = new String(pdfContent, 0,
                Math.min(4, pdfContent.length));
        assertEquals("%PDF", pdfHeader);
    }
}
