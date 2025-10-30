package com.ase.userservice.controllers;


import com.ase.userservice.forms.StudentDTO;
import com.ase.userservice.services.StammdatenService;
import com.ase.userservice.services.StudienbescheinigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import java.time.LocalDate;

@RestController
public class StudienbescheinigungController {

  @Autowired
  private StudienbescheinigungService studienbescheinigungService;

  @Autowired
  private StammdatenService stammdatenService;


  /**
   * Generates PDF for test user and returns it as downloadable file.
   *
   * @return ResponseEntity containing the PDF file
   */
  @GetMapping("/studienbescheinigung")
  public ResponseEntity<?> getStudienbescheinigung(
      @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language) {

    Boolean isEnglish = language.equals("en-US");
    StudentDTO student;

    try {
      student = stammdatenService.fetchUserInfo();
      if (student.getId() == null) {
        throw new RestClientException("API call returned no data!");
      }
    } catch (RestClientException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
          .body("API failed to return student information!");
    }

    //TODO: REPLACE MISSING DATA
    String semesterName = isEnglish ? "MISSING_DATA" : "FEHLENDE_DATEN";
    LocalDate semesterStart = LocalDate.of(1970, 1, 1);
    LocalDate semesterEnd = LocalDate.of(1970, 1, 1);
    String degree = "Bachelor of Science";
    Integer regularEnrollmentDuration = 0;
    LocalDate enrollmentStart = LocalDate.of(1970, 1, 1);
    LocalDate enrollmentEnd = LocalDate.of(1970, 1, 1);
    Integer universitySemester = 0;
    Integer vacationSemester = 0;

    try {
      byte[] generatedPdf = studienbescheinigungService.generatePdf(
          semesterName,
          semesterStart,
          semesterEnd,
          student,
          degree,
          regularEnrollmentDuration,
          enrollmentStart,
          enrollmentEnd,
          universitySemester,
          vacationSemester,
          isEnglish
      );

      // Set headers for PDF download
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_PDF);
      headers.setContentDispositionFormData(
          "attachment", isEnglish ? "confirmation_of_enrollment_" + semesterName + ".pdf": "studienbescheinigung" + semesterName + ".pdf");
      headers.setContentLength(generatedPdf.length);

      return ResponseEntity.ok()
          .headers(headers)
          .body(generatedPdf);
    }
    catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(e.getMessage());
    }
  }

}
