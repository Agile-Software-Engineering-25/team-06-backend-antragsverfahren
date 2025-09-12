package com.ase.userservice.controllers;

import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/bachelorarbeit")
public class BachelorthesisController {

  private BachelorthesisService bachelorthesisService;

  @Autowired
  public BachelorthesisController(BachelorthesisService bachelorthesisService) {
    this.bachelorthesisService = bachelorthesisService;
  }

  @GetMapping("/{matrikelnummer}")
  public ResponseEntity<BachelorthesisRequest> getBachelorthesisRequestByMatrikelnummer(@PathVariable String matrikelnummer) {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.getBachelorthesisRequestByMatrikelnummer(matrikelnummer);
    return new ResponseEntity<>(bachelorthesisRequest, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<String> createBachelorthesisRequest(@RequestBody BachelorthesisRequest bachelorthesis){
    BachelorthesisRequest request = new BachelorthesisRequest(
        bachelorthesis.getMatrikelnummer(),
        bachelorthesis.getThema(),
        bachelorthesis.getName(),
        bachelorthesis.getStudiengang(),
        bachelorthesis.getExaminer(),
        bachelorthesis.getPrüfungstermin()
    );
    return new ResponseEntity<>("Got Bachelorarbeit data", HttpStatus.OK);
  }
}
