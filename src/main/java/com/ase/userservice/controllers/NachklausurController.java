package com.ase.userservice.controllers;

import com.ase.userservice.entities.NachklausurRequest;
import com.ase.userservice.services.NachklausurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
   * @param name the student's name
   * @param matriculationNumber the student's matriculation number
   * @param modul the module/course name
   * @param prüfungstermin the exam date
   * @return ResponseEntity with success message
   */
  @PostMapping("/nachklausur")
  public ResponseEntity<String> nachklausur(@RequestBody NachklausurRequest nachklausurRequest) {
    System.out.println("Received Nachklausur request: " + nachklausurRequest);
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
