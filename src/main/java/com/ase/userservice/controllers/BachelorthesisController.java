package com.ase.userservice.controllers;

import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.forms.DocumentForms;
import com.ase.userservice.services.BachelorthesisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

  @GetMapping("/{matriculationNumber}")
  public ResponseEntity<BachelorthesisRequest> getBachelorthesisRequestBymatriculationNumber(@PathVariable String matriculationNumber) {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.getBachelorthesisRequestByMatriculationNumber(matriculationNumber);
    return new ResponseEntity<>(bachelorthesisRequest, HttpStatus.OK);
  }

  @PostMapping()
  public ResponseEntity<String> createBachelorthesisRequest(@RequestBody @Valid BachelorthesisRequest bachelorthesis, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Form has errors: " + bindingResult.getAllErrors());
    }

    bachelorthesisService.createBachelorthesisRequest(bachelorthesis);
    return new ResponseEntity<>("Bachelorthesis request created successfully", HttpStatus.CREATED);
  }

}
