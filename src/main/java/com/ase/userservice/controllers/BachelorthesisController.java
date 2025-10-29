package com.ase.userservice.controllers;


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
import org.springframework.web.multipart.MultipartFile;
import com.ase.userservice.database.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController()
@RequestMapping("/bachelorarbeit")
public class BachelorthesisController {

  @Autowired
  private BachelorthesisService bachelorthesisService;

  @Autowired
  private StammdatenService stammdatenService;

  @Autowired
  private RestTemplate restTemplate;


  @GetMapping("/{matrikelnummer}")
  public ResponseEntity<String>
      getBachelorthesisRequestByMatrikelnummer(
      @PathVariable String matrikelnummer) throws JsonProcessingException {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.
        getRequestByMatrikelnummer(matrikelnummer);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(bachelorthesisRequest);
    return new ResponseEntity<>(json, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String>
  deleteBachelorthesisRequestById(
      @PathVariable Long id) throws JsonProcessingException {
    bachelorthesisService.deleteRequest(id);
    return new ResponseEntity<>("BachelorthesisRequest deleted.", HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<String> createBachelorthesisRequest(
      @RequestParam("prüfungstermin") String pruefungstermin,
      @RequestParam("thema") String thema,
      @RequestParam("prüfer") String pruefer,
      @RequestParam("expose") MultipartFile exposeFile) throws IOException, ExecutionException, InterruptedException {

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

    if (Objects.equals(student, new StudentDTO())) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to retrieve user data from API!");
    }

    CompletableFuture<Void> createRequest = bachelorthesisService.createRequest(
        new BachelorthesisRequest(
            student.getMatriculationNumber(),
            student.getLastName(),
            student.getFirstName(),
            student.getDegreeProgram(),
            thema,
            pruefer,
            pruefungstermin,
            exposeFile.getBytes()
        )
    );

    byte[] generatedPdf = bachelorthesisService.generatePdf(
        student.getFirstName() + " " + student.getLastName(),
        student.getMatriculationNumber(),
        student.getDegreeProgram(),
        thema,
        pruefer,
        pruefungstermin
    );

    bachelorthesisService.sendEmail(student, generatedPdf, false);

    createRequest.get();
    return new ResponseEntity<>(
        "Bachelorarbeit data and expose file received", HttpStatus.OK
    );
  }


}
