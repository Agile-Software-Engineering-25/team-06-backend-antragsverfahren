package com.ase.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DocumentController {

  @PostMapping("/studienbescheinigung")
  public ResponseEntity<String> studienbescheinigung() {
    return ResponseEntity.ok("hello world");
  }
}
