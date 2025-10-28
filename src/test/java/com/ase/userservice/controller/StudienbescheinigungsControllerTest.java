package com.ase.userservice.controller;

import com.ase.userservice.config.SecurityConfig;
import com.ase.userservice.controllers.StudienbescheinigungController;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.UserRepository;
import com.ase.userservice.services.StudienbescheinigungService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudienbescheinigungController.class)
@Import(SecurityConfig.class)
class StudienbescheinigungsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudienbescheinigungService studienbescheinigungService;

    @MockBean
    private UserRepository userRepository;

    // Erfolgreicher PDF-Download auf Deutsch
    @Test
    void testSendStudienbescheinigung_de() throws Exception {
        User testUser = new User();
        testUser.setMatriculationNumber("123456");

        when(userRepository.findByMatriculationNumber("123456"))
                .thenReturn(Optional.of(testUser));

        byte[] pdfBytes = new byte[]{1, 2, 3, 4};
        when(studienbescheinigungService.generateStudienbescheinigungPdf(testUser))
                .thenReturn(pdfBytes);

        mockMvc.perform(post("/studienbescheinigung")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "de-DE"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(content().bytes(pdfBytes));

        verify(studienbescheinigungService).generateStudienbescheinigungPdf(testUser);
        verify(studienbescheinigungService).sendStudienbescheinigungByEmail(testUser, pdfBytes, false);
    }

    // Erfolgreicher PDF-Download auf Englisch
    @Test
    void testSendStudienbescheinigung_en() throws Exception {
        User testUser = new User();
        testUser.setMatriculationNumber("123456");

        when(userRepository.findByMatriculationNumber("123456"))
                .thenReturn(Optional.of(testUser));

        byte[] pdfBytes = new byte[]{5, 6, 7, 8};
        when(studienbescheinigungService.generateStudienbescheinigungPdfEn(testUser))
                .thenReturn(pdfBytes);

        mockMvc.perform(post("/studienbescheinigung")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(content().bytes(pdfBytes));

        verify(studienbescheinigungService).generateStudienbescheinigungPdfEn(testUser);
        verify(studienbescheinigungService).sendStudienbescheinigungByEmail(testUser, pdfBytes, true);
    }

    // User nicht gefunden → 404
    @Test
    void testSendStudienbescheinigung_userNotFound() throws Exception {
        when(userRepository.findByMatriculationNumber("123456")).thenReturn(Optional.empty());

        mockMvc.perform(post("/studienbescheinigung")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "de-DE"))
                .andExpect(status().isNotFound());

        verify(studienbescheinigungService, never()).generateStudienbescheinigungPdf(any());
        verify(studienbescheinigungService, never()).sendStudienbescheinigungByEmail(any(), any(), anyBoolean());
    }

    // Ungültige Sprache → 500
    @Test
    void testSendStudienbescheinigung_invalidLanguage() throws Exception {
        User testUser = new User();
        testUser.setMatriculationNumber("123456");

        when(userRepository.findByMatriculationNumber("123456")).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/studienbescheinigung")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "fr-FR"))
                .andExpect(status().isInternalServerError());

        verify(studienbescheinigungService, never()).sendStudienbescheinigungByEmail(any(), any(), anyBoolean());
    }
}
