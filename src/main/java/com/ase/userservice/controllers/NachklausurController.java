package com.ase.userservice.controllers;

import com.ase.userservice.database.entities.BachelorthesisRequest;
import com.ase.userservice.database.entities.NachklausurRequest;
import com.ase.userservice.forms.StudentDTO;
import com.ase.userservice.services.StammdatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.ase.userservice.services.NachklausurService;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
public class  NachklausurController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private NachklausurService nachklausurService;

  @Autowired
  private StammdatenService stammdatenService;

  /**
   * Processes a Nachklausur application and generates PDF (stored server-side).
   *
   * @requestbody NachklausurRequest containing application details
   * @return ResponseEntity with success message
   */
  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur(
      @RequestParam("modul") String modul,
      @RequestParam("prüfungstermin") String pruefungstermin) throws ExecutionException, InterruptedException {

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

    CompletableFuture<Void> createRequest = nachklausurService.createRequest(
        new NachklausurRequest(
            student.getLastName(),
            student.getFirstName(),
            student.getMatriculationNumber(),
            modul,
            pruefungstermin
        )
    );

    // Generate PDF with data from frontend and API
    byte[] pdfBytes = nachklausurService.generatePdf(
        modul,
        pruefungstermin,
        student.getFirstName(),
        student.getLastName(),
        student.getMatriculationNumber(),
        student.getCohort()
    );

    nachklausurService.sendEmail(student, pdfBytes, false);

    createRequest.get();

    return ResponseEntity.ok(
        "Nachklausur application processed and PDF sent to Prüfungsamt.");
  }

}
