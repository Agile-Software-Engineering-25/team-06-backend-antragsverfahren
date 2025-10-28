package com.ase.userservice.controllers;


import java.io.IOException;
import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.entities.Dozent;
import com.ase.userservice.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;

@RestController()
@RequestMapping("/bachelorarbeit")
public class BachelorthesisController {

  private BachelorthesisService bachelorthesisService;

  @Autowired
  public BachelorthesisController(BachelorthesisService bachelorthesisService) {
    this.bachelorthesisService = bachelorthesisService;
  }

  @Autowired
  private RestTemplate restTemplate;


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
      @RequestParam("studiengang") String studiengang,
      @RequestParam("prüfungstermin") String prüfungstermin,
      @RequestParam("thema") String thema,
      @RequestParam("prüfer") String prüfer,
      @RequestParam("expose") MultipartFile exposeFile) {

    String url = "https://sau-portal.de/team-11-api/api/v1/users/" + CurrentAuthContext.getSid();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.extractToken());


    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
    User user = response.getBody();

    bachelorthesisService.generateBachelorthesisPdf(
        user.getFirstName() + " " + user.getLastName(),
        user.getMatriculationNumber(),
        studiengang,
        thema,
        prüfer,
        prüfungstermin
    );

    return new ResponseEntity<>(
        "Bachelorarbeit data and expose file received", HttpStatus.OK
    );
  }
}
