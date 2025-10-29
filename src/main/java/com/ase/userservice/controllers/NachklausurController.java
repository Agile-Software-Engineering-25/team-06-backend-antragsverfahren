package com.ase.userservice.controllers;

import com.ase.userservice.forms.StudentDTO;
import com.ase.userservice.services.StammdatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ase.userservice.database.entities.NachklausurRequest;
import com.ase.userservice.services.NachklausurService;
import jakarta.validation.Valid;
import java.util.Objects;


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
      @RequestParam("prüfungstermin") String pruefungstermin) {

    StudentDTO student = stammdatenService.fetchUserInfo();

    if (Objects.equals(student, new StudentDTO())) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to retrieve user data from API");
    }

    // Generate PDF with data from frontend and API
    byte[] pdfBytes = nachklausurService.generateNachklausurPdf(
        modul,
        pruefungstermin,
        student.getFirstName(),
        student.getLastName(),
        student.getMatriculationNumber(),
        student.getCohort()
    );

    nachklausurService.sendEmail(student, pdfBytes, false);
    return ResponseEntity.ok(
        "Nachklausur application processed and PDF sent to Prüfungsamt.");
  }

}
