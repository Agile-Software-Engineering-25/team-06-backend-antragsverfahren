package com.ase.userservice.controllers;

import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/bachelorarbeit")
public class BachelorthesisController {

  private BachelorthesisService bachelorthesisService;

  @Autowired
  public BachelorthesisController(BachelorthesisService bachelorthesisService) {
    this.bachelorthesisService = bachelorthesisService;
  }

  @GetMapping("/{matrikelnummer}")
  public ResponseEntity<BachelorthesisRequest>
      getBachelorthesisRequestByMatrikelnummer(
      @PathVariable String matrikelnummer) {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.
        getBachelorthesisRequestByMatrikelnummer(matrikelnummer);
    return new ResponseEntity<>(bachelorthesisRequest, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<String> createBachelorthesisRequest(
      @RequestParam("name") String name,
      @RequestParam("matrikelnummer") String matrikelnummer,
      @RequestParam("studiengang") String studiengang,
      @RequestParam("prüfungstermin") String prüfungstermin,
      @RequestParam("thema") String thema,
      @RequestParam("prüfer") String prüfer,
      @RequestParam("expose") MultipartFile exposeFile) {
    // Save the expose file to disk
    try {
      byte[] exposeBytes = exposeFile.getBytes();
      // Create the request object with exposeDocument
      BachelorthesisRequest request = new BachelorthesisRequest(
          matrikelnummer,
          name,
          studiengang,
          thema,
          prüfer,
          prüfungstermin,
          exposeBytes
      );
      // Save to DB
      bachelorthesisService.createBachelorthesisRequest(request);
    }
    catch (IOException e) {
      return new ResponseEntity<>(
          "Failed to save expose file", HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

    return new ResponseEntity<>(
        "Bachelorarbeit data and expose file received", HttpStatus.OK
    );
  }
}
