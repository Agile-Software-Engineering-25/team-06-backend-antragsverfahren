package com.ase.userservice.controllers;

import com.ase.userservice.entities.NachklausurRequest;
import com.ase.userservice.forms.DocumentForms;
import com.ase.userservice.services.NachklausurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NachklausurController {

  @Autowired
  private NachklausurService nachklausurService;

  /**
   * Processes a Nachklausur application and generates PDF (stored server-side).
   *
   * @requestbody NachklausurRequest containing application details
   * @return ResponseEntity with success message
   */
  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur(@RequestBody @Valid NachklausurRequest nachklausurRequest, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Form has errors: " + bindingResult.getAllErrors());
    }

    NachklausurRequest request = new NachklausurRequest(
        nachklausurRequest.getName(),
        nachklausurRequest.getMatrikelnummer(),
        nachklausurRequest.getModul(),
        nachklausurRequest.getPrüfungstermin()
    );

    // Generate PDF
   nachklausurService.generateNachklausurPdf(request);

    return ResponseEntity.ok("Nachklausur application processed and PDF sent to Prüfungsamt.");
  }

}
