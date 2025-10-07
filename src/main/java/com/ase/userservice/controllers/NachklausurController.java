package com.ase.userservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ase.userservice.entities.NachklausurRequest;
import com.ase.userservice.services.NachklausurService;
import jakarta.validation.Valid;


@RestController
public class NachklausurController {

  private final NachklausurService nachklausurService;

  public NachklausurController(NachklausurService nachklausurService) {
    this.nachklausurService = nachklausurService;
  }

  /**
   * Processes a Nachklausur application and generates PDF (stored server-side).
   *
   * @requestbody NachklausurRequest containing application details
   * @return ResponseEntity with success message
   */
  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur(
      @RequestBody @Valid NachklausurRequest nachklausurRequest,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Form has errors: " + bindingResult.getAllErrors());
    }

    NachklausurRequest request = new NachklausurRequest(
        nachklausurRequest.getName(),
        nachklausurRequest.getMatrikelnummer(),
        nachklausurRequest.getModul(),
        nachklausurRequest.getPruefungstermin()
    );

    // Generate PDF
    nachklausurService.generateNachklausurPdf(request);

    return ResponseEntity.ok(
        "Nachklausur application processed and PDF sent to Prüfungsamt.");
  }

}
