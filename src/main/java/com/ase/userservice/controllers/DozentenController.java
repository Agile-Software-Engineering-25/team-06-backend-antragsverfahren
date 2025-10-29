// java
package com.ase.userservice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.ase.userservice.forms.LecturerDTO;
import com.ase.userservice.services.StammdatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/dozenten")
public class DozentenController {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private StammdatenService stammdatenService;


  @GetMapping
  public List<LecturerDTO> getAllDozenten() {
    return stammdatenService.fetchAllDozenten();
  }

  @GetMapping("/names")
  public List<String> getDozentenNames() {
    return stammdatenService.fetchAllDozenten().stream()
        .map(d -> d.getFirstName() + " " + d.getLastName())
        .collect(Collectors.toList());
  }

}
