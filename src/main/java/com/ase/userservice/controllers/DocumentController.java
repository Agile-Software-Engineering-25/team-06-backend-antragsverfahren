package com.ase.userservice.controllers;

import com.ase.userservice.entities.User;
import com.ase.userservice.forms.DocumentForms;
import com.ase.userservice.repositories.UserRepository;
import com.ase.userservice.services.StudienbescheinigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.validation.*;
import jakarta.validation.Valid;

@RestController
public class DocumentController {

    @Autowired
    private StudienbescheinigungService studienbescheinigungService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Fetches the test user from the database.
     * Uses the matriculation number from data.sql to find the user.
     *
     * @return the user from database or null if not found
     */
    private User getTestUserFromDatabase(String matriculationNumber) {
      return userRepository.findByMatriculationNumber(matriculationNumber)
          .orElse(null);
    }

    /**
     * Generates PDF for test user and returns it as downloadable file.
     *
     * @return ResponseEntity containing the PDF file
     */
    @PostMapping("/studienbescheinigung")
    public ResponseEntity<byte[]> sendStudienbescheinigung(@RequestHeader(HttpHeaders.ACCEPT_LANGUAGE)String language) {
        User testUser = getTestUserFromDatabase("123456");

        if (testUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        try {
            byte[] pdfContent;
            if ("en-US".equals(language)){
                pdfContent = studienbescheinigungService
                    .generateStudienbescheinigungPdfEn(testUser);
              // Send the PDF via email
              //studienbescheinigungService
              //    .sendStudienbescheinigungByEmail(testUser, pdfContent, true);
            }
            else if (language.contains("de")){
                pdfContent = studienbescheinigungService
                        .generateStudienbescheinigungPdf(testUser);
              // Send the PDF via email
              //studienbescheinigungService
              //    .sendStudienbescheinigungByEmail(testUser, pdfContent, false);
            }
            else {
                throw new Exception();
            }


            // Set headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "studienbescheinigung.pdf");
            headers.setContentLength(pdfContent.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Placeholder endpoint for Nachklausur functionality.
     *
     * @return ResponseEntity with hello world message
     */

    @PostMapping("/nachklausur")
    public ResponseEntity<String> nachklausur(@RequestBody @Valid DocumentForms.NachklausurForm nachklausurForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Form has errors: " + bindingResult.getAllErrors());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * Placeholder endpoint for Bachelorarbeit functionality.
     *
     * @return ResponseEntity with hello world message
     */

    @PostMapping("/bachelorarbeit")
    public ResponseEntity<String> bachelorarbeit(@RequestBody @Valid DocumentForms.BachelorarbeitForm bachelorarbeitForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Form has errors: " + bindingResult.getAllErrors());
		}

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
