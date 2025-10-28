package com.ase.userservice.controller;

import com.ase.userservice.config.SecurityConfig;
import com.ase.userservice.controllers.NachklausurController;
import com.ase.userservice.entities.NachklausurRequest;
import com.ase.userservice.services.NachklausurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NachklausurController.class)
@Import(SecurityConfig.class)
class NachklausurControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NachklausurService nachklausurService;

  // Erfolgreiche Nachklausur-Anfrage
  @Test
  void testNachklausurSuccess() throws Exception {
    String body = """
            {
              "name": "Max Mustermann",
              "matrikelnummer": "123456",
              "modul": "Informatik 101",
              "pruefungstermin": "2025-11-30"
            }
            """;

    mockMvc.perform(post("/nachklausur")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(content().string("Nachklausur application processed and PDF sent to Prüfungsamt."));

    // Prüft, dass der Service aufgerufen wurde
    verify(nachklausurService, times(1)).generateNachklausurPdf(any(NachklausurRequest.class));
  }

  // Validierungsfehler (z.B. fehlendes Feld "name")
  @Test
  void testNachklausurValidationError() throws Exception {
    String body = """
            {
              "matrikelnummer": "123456",
              "modul": "Informatik 101",
              "pruefungstermin": "2025-11-30"
            }
            """;

    mockMvc.perform(post("/nachklausur")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Form has errors")));

    // Service darf nicht aufgerufen werden
    verify(nachklausurService, never()).generateNachklausurPdf(any());
  }
}

