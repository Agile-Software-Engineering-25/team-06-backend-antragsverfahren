package com.ase.userservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class DocumentController {

  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur() {
    return ResponseEntity.ok("hello world");
  }
}
