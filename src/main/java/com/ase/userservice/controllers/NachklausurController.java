package com.ase.userservice.controllers;

import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.ase.userservice.entities.NachklausurRequest;
import com.ase.userservice.services.NachklausurService;
import jakarta.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
public class  NachklausurController {

  @Autowired
  private RestTemplate restTemplate;

  private final NachklausurService nachklausurService;

  public NachklausurController(NachklausurService nachklausurService) {
    this.nachklausurService = nachklausurService;
  }

  /**
   * Processes a Nachklausur application and generates PDF (stored server-side).
   *
   * @requestbody NachklausurRequest containing application details
   * @return ResponseEntity with success message
   */
  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur(
      @RequestBody @Valid NachklausurRequest nachklausurRequest,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Form has errors: " + bindingResult.getAllErrors());
    }
    String url = "https://sau-portal.de/team-11-api/api/v1/users/" + CurrentAuthContext.getSid();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.extractToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
    User user = response.getBody();

    if (user == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to retrieve user data from API");
    }

    // Generate PDF with data from frontend and API
    byte[] pdfBytes = nachklausurService.generateNachklausurPdf(
        nachklausurRequest.getModul(),
        nachklausurRequest.getPrüfungstermin(),
        user.getFirstName(),
        user.getLastName()
    );


    return ResponseEntity.ok(
        "Nachklausur application processed and PDF sent to Prüfungsamt.");
  }

}
