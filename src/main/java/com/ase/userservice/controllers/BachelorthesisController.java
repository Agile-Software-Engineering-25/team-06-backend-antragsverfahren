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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/bachelorform")
public class BachelorthesisController {

  private BachelorthesisService bachelorthesisService;

  @Autowired
  public BachelorthesisController(BachelorthesisService bachelorthesisService) {
    this.bachelorthesisService = bachelorthesisService;
  }

  @GetMapping("/{matriculationNumber}")
  public ResponseEntity<BachelorthesisRequest> getBachelorthesisRequestBymatriculationNumber(@PathVariable String matriculationNumber) {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.getBachelorthesisRequestByMatriculationNumber(matriculationNumber);
    return new ResponseEntity<>(bachelorthesisRequest, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<BachelorthesisRequest> createBachelorthesisRequest(@RequestBody BachelorthesisRequest bachelorthesis){
    bachelorthesisService.createBachelorthesisRequest(bachelorthesis);
    return new ResponseEntity<>(bachelorthesis, HttpStatus.CREATED);
  }
}
