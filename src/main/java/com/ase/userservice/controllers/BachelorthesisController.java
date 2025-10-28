package com.ase.userservice.controllers;


import java.io.IOException;
import java.util.stream.Collectors;
import com.ase.userservice.forms.DocumentForms;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;

@RestController()
@RequestMapping("/bachelorarbeit")
public class BachelorthesisController {

  private BachelorthesisService bachelorthesisService;

  @Autowired
  public BachelorthesisController(BachelorthesisService bachelorthesisService) {
    this.bachelorthesisService = bachelorthesisService;
  }

  @GetMapping("/{matrikelnummer}")
  public ResponseEntity<BachelorthesisRequest>
      getBachelorthesisRequestByMatrikelnummer(
      @PathVariable String matrikelnummer) {
    BachelorthesisRequest bachelorthesisRequest = bachelorthesisService.
        getBachelorthesisRequestByMatrikelnummer(matrikelnummer);
    return new ResponseEntity<>(bachelorthesisRequest, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> createBachelorthesisRequest(
      @Valid @ModelAttribute DocumentForms.BachelorarbeitForm form,
      BindingResult bindingResult,
      @RequestParam("expose") MultipartFile exposeFile) {

    if (bindingResult.hasErrors()) {
      String errors = bindingResult.getAllErrors()
          .stream()
          .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
          .collect(Collectors.joining(", "));
      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    if (exposeFile == null || exposeFile.isEmpty()) {
      return new ResponseEntity<>("Expose file is required", HttpStatus.BAD_REQUEST);
    }

    // Save the expose file to disk
    try {
      byte[] exposeBytes = exposeFile.getBytes();
      // Create the request object with exposeDocument
      BachelorthesisRequest request = new BachelorthesisRequest(
          form.getMatrikelnummer(),
          form.getName(),
          form.getModul(),
          form.getThema(),
          form.getExaminer(),
          form.getPruefungstermin(),
          exposeBytes
      );
      // Save to DB
      // bachelorthesisService.createBachelorthesisRequest(request);
    }
    catch (IOException e) {
      return new ResponseEntity<>(
          "Failed to save expose file", HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

    return new ResponseEntity<>(
        "Bachelorarbeit data and expose file received", HttpStatus.OK
    );
  }
}
