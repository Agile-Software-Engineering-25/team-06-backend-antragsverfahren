package com.ase.userservice.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ase.userservice.entities.Dozent;
import com.ase.userservice.repositories.DozentenRepository;

@RestController
@RequestMapping("/dozenten")
public class DozentenController {

  @Autowired
  private DozentenRepository dozentenRepository;

  @GetMapping
  public List<Dozent> getAllDozenten() {
    return dozentenRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Dozent> getDozentById(@PathVariable Long id) {
    return dozentenRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Dozent createDozent(@RequestBody Dozent dozent) {
    return dozentenRepository.save(dozent);
  }
}

