package com.ase.userservice.controllers;

import com.ase.userservice.entities.User;
import com.ase.userservice.services.StudienbescheinigungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class DocumentController {

    @Autowired
    private StudienbescheinigungService studienbescheinigungService;

    /**
     * Creates a test user with predefined data.
     *
     * @return a test user instance
     */
    private User createTestUser() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Max");
        testUser.setLastName("Mustermann");
        testUser.setDateOfBirth(LocalDate.of(1995, 5, 15));
        testUser.setMatriculationNumber("TEST123");
        testUser.setStudyProgram("Computer Science");
        testUser.setDegree("Bachelor of Science");
        testUser.setCurrentSemester(5);
        testUser.setStandardStudyDuration(6);
        testUser.setStudyStartSemester("Winter Semester 2022/2023");
        testUser.setUniversitySemester(5);
        testUser.setLeaveOfAbsenceSemesters(0);
        testUser.setEmail("test@test.de");
        return testUser;
    }

    /**
     * Generates PDF for test user and returns it as downloadable file.
     *
     * @return ResponseEntity containing the PDF file
     */
    @PostMapping("/studienbescheinigung")
    public ResponseEntity<byte[]> sendStudienbescheinigung() {
        User testUser = createTestUser();

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
