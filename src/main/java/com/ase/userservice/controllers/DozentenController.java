// java
package com.ase.userservice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.ase.userservice.forms.LecturerDTO;
import com.ase.userservice.forms.StudentDTO;
import com.ase.userservice.services.StammdatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/dozenten")
public class DozentenController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private StammdatenService stammdatenService;


  @GetMapping
  public ResponseEntity<?> getAllDozenten() {
    try {
      return ResponseEntity.ok(stammdatenService.fetchAllDozenten());
    } catch (RestClientException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
          .body("API failed to return lecturer information!");
    }
  }

  @GetMapping("/names")
  public ResponseEntity<?> getDozentenNames() {
    try {
      List<String> lecturerNames = stammdatenService.fetchAllDozenten()
          .stream()
          .map(d -> d.getFirstName() + " " + d.getLastName())
          .toList();
      return ResponseEntity.ok(lecturerNames);
    } catch (RestClientException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
          .body("API failed to return lecturer information!");
    }
  }

}
