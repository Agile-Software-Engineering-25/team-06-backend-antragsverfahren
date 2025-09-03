package com.ase.userservice;

import com.ase.userservice.controllers.DocumentController;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.UserRepository;
import com.ase.userservice.services.StudienbescheinigungService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration")
@Import(TestMailConfig.class)
@Transactional
public class StudienbescheinigungIntegrationTest {

  @Autowired
  private DocumentController documentController;

  @Autowired
  private StudienbescheinigungService studienbescheinigungService;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testSendStudienbescheinigungPdfGerman() {
    ResponseEntity<byte[]> response = documentController
        .sendStudienbescheinigung("de");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().length > 0);

    assertNotNull(response.getHeaders().getContentType());
    String expectedContentType = "application/pdf";
    String actualContentType = response.getHeaders().getContentType()
        .getType() + "/" + response.getHeaders().getContentType()
        .getSubtype();
    assertEquals(expectedContentType, actualContentType);

    String contentDisposition = response.getHeaders()
        .getFirst("Content-Disposition");
    assertNotNull(contentDisposition);
    assertTrue(contentDisposition.contains("studienbescheinigung.pdf"));

    // Verify PDF content
    byte[] pdfContent = response.getBody();
    String pdfHeader = new String(pdfContent, 0, Math.min(4, pdfContent.length));
    assertEquals("%PDF", pdfHeader, "Should be valid PDF format");
  }

  @Test
  public void testSendStudienbescheinigungPdfEnglish() {
    ResponseEntity<byte[]> response = documentController
        .sendStudienbescheinigung("en-US");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().length > 0);

    assertNotNull(response.getHeaders().getContentType());
    String expectedContentType = "application/pdf";
    String actualContentType = response.getHeaders().getContentType()
        .getType() + "/" + response.getHeaders().getContentType()
        .getSubtype();
    assertEquals(expectedContentType, actualContentType);

    String contentDisposition = response.getHeaders()
        .getFirst("Content-Disposition");
    assertNotNull(contentDisposition);
    assertTrue(contentDisposition.contains("studienbescheinigung.pdf"));

    // Verify PDF content
    byte[] pdfContent = response.getBody();
    String pdfHeader = new String(pdfContent, 0, Math.min(4, pdfContent.length));
    assertEquals("%PDF", pdfHeader, "Should be valid PDF format");
  }

  @Test
  public void testPdfContentValidation() {
    User testUser = userRepository.findByMatriculationNumber("123456")
        .orElse(null);
    assertNotNull(testUser, "Test user should exist in database");

    // Test German PDF generation
    byte[] pdfContent = studienbescheinigungService
        .generateStudienbescheinigungPdf(testUser);

    assertNotNull(pdfContent, "PDF content should not be null");
    assertTrue(pdfContent.length > 0, "PDF should have content");

    String pdfHeader = new String(pdfContent, 0, Math.min(4, pdfContent.length));
    assertEquals("%PDF", pdfHeader, "Should be valid PDF format");

    // Test English PDF generation
    byte[] pdfContentEn = studienbescheinigungService
        .generateStudienbescheinigungPdfEn(testUser);

    assertNotNull(pdfContentEn, "English PDF content should not be null");
    assertTrue(pdfContentEn.length > 0, "English PDF should have content");

    String pdfHeaderEn = new String(pdfContentEn, 0, Math.min(4, pdfContentEn.length));
    assertEquals("%PDF", pdfHeaderEn, "English PDF should be valid PDF format");
  }

  @Test
  public void testUserExistsInDatabase() {
    User testUser = userRepository.findByMatriculationNumber("123456")
        .orElse(null);
    assertNotNull(testUser, "Test user should exist in database");
    assertEquals("123456", testUser.getMatriculationNumber());
    assertNotNull(testUser.getFirstName());
    assertNotNull(testUser.getLastName());
    assertNotNull(testUser.getEmail());
  }

  @Test
  public void testStudienbescheinigungEndpointWithInvalidLanguage() {
    ResponseEntity<byte[]> response = documentController
        .sendStudienbescheinigung("fr-FR");

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testStudienbescheinigungEndpointWithValidLanguages() {
    // Test that the endpoint returns OK for both supported languages
    ResponseEntity<byte[]> germanResponse = documentController
        .sendStudienbescheinigung("de");
    ResponseEntity<byte[]> englishResponse = documentController
        .sendStudienbescheinigung("en-US");

    // Both should succeed
    assertTrue(germanResponse.getStatusCode() == HttpStatus.OK);
    assertTrue(englishResponse.getStatusCode() == HttpStatus.OK);
  }
}
