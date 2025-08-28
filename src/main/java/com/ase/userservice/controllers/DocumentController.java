package com.ase.userservice.controllers;

import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.UserRepository;
import com.ase.userservice.services.StudienbescheinigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    private User getTestUserFromDatabase() {
        // Fetch user with matriculation number "123456" from data.sql
        Optional<User> userOptional = userRepository.findByMatriculationNumber("123456");

        return userOptional.orElse(
            // Fallback: try to get the first user if the specific one is not found
            userRepository.findAll().stream().findFirst().orElse(null)
        );
    }

    /**
     * Generates PDF for test user and returns it as downloadable file.
     *
     * @return ResponseEntity containing the PDF file
     */
    @PostMapping("/studienbescheinigung")
    public ResponseEntity<byte[]> sendStudienbescheinigung() {
        User testUser = getTestUserFromDatabase();

        if (testUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        try {
            byte[] pdfContent = studienbescheinigungService
                    .generateStudienbescheinigungPdf(testUser);
            // studienbescheinigungService
            //         .sendStudienbescheinigungByEmail(testUser, pdfContent);

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
    public ResponseEntity<String> nachklausur() {
        return ResponseEntity.ok("hello world");
    }

    /**
     * Placeholder endpoint for Bachelorarbeit functionality.
     *
     * @return ResponseEntity with hello world message
     */
    @PostMapping("/bachelorarbeit")
    public ResponseEntity<String> bachelorarbeit() {
        return ResponseEntity.ok("hello world");
    }
}
