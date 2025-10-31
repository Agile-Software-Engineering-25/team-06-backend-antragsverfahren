package com.ase.userservice.controllers;


import com.ase.userservice.database.entities.NachklausurRequest;
import com.ase.userservice.forms.StudentDTO;
import com.ase.userservice.services.StammdatenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.ase.userservice.services.NachklausurService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/nachklausur")
public class  NachklausurController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private NachklausurService nachklausurService;

  @Autowired
  private StammdatenService stammdatenService;





  @GetMapping("/{matrikelnummer}")
  public ResponseEntity<String> getNachklausurRequestByMatrikelnummer(
      @PathVariable String matrikelnummer) throws JsonProcessingException {
    NachklausurRequest[] nachklausurRequest = nachklausurService.
        getRequestByMatrikelnummer(matrikelnummer);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(nachklausurRequest);
    return new ResponseEntity<>(json, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteNachklausurRequestById(
      @PathVariable Long id) {
    nachklausurService.deleteRequest(id);
    return new ResponseEntity<>("NachklausurRequest deleted.", HttpStatus.OK);
  }


  @PostMapping()
  public ResponseEntity<String> nachklausur(
      @RequestParam("modul") String modul,
      @RequestParam("prüfungstermin") String pruefungstermin) throws ExecutionException, InterruptedException {

    if(pruefungstermin == null || pruefungstermin.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Send prüfungstermin cannot be empty!");
    } else if(pruefungstermin.length() > 10) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Send prüfungstermin is too long!");
    } else if(!LocalDate.parse(pruefungstermin, DateTimeFormatter.ofPattern("dd-MM-yyyy")).isAfter(LocalDate.now())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Send prüfungstermin is invalid!");
    }

    if(modul == null || modul.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Send modul cannot be empty!");
    } else if(modul.length() > 100) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Send modul is too long!");
    }

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
