package com.ase.userservice.controller;

import com.ase.userservice.config.SecurityConfig;
import com.ase.userservice.controllers.BachelorthesisController;
import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.services.BachelorthesisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BachelorthesisController.class)
@Import(SecurityConfig.class)
class BachelorthesisControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BachelorthesisService bachelorthesisService;

  // GET /bachelorarbeit/{matrikelnummer}
  @Test
  void testGetBachelorthesisRequestByMatrikelnummer() throws Exception {
    byte[] exposeBytes = new byte[]{1, 2, 3};

    BachelorthesisRequest request = new BachelorthesisRequest(
        "123456",
        "Max Mustermann",
        "Informatik",
        "KI im Gesundheitswesen",
        "Dr. Schmidt",
        "2025-12-15",
        exposeBytes
    );

    when(bachelorthesisService.getBachelorthesisRequestByMatrikelnummer("123456"))
        .thenReturn(request);

    mockMvc.perform(get("/bachelorarbeit/123456"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.matrikelnummer").value("123456"))
        .andExpect(jsonPath("$.name").value("Max Mustermann"))
        .andExpect(jsonPath("$.studiengang").value("Informatik"))
        .andExpect(jsonPath("$.thema").value("KI im Gesundheitswesen"))
        .andExpect(jsonPath("$.pruefer").value("Dr. Schmidt"))
        .andExpect(jsonPath("$.pruefungstermin").value("2025-12-15"))
        .andExpect(jsonPath("$.expose").isNotEmpty());

    verify(bachelorthesisService).getBachelorthesisRequestByMatrikelnummer("123456");
  }

  // POST /bachelorarbeit (Multipart)
  @Test
  void testCreateBachelorthesisRequest() throws Exception {
    MockMultipartFile exposeFile = new MockMultipartFile(
        "expose",
        "expose.pdf",
        MediaType.APPLICATION_PDF_VALUE,
        "Fake PDF Content".getBytes()
    );

    mockMvc.perform(multipart("/bachelorarbeit")
            .file(exposeFile)
            .param("name", "Max Mustermann")
            .param("matrikelnummer", "123456")
            .param("studiengang", "Informatik")
            .param("prüfungstermin", "2025-12-15")
            .param("thema", "KI im Gesundheitswesen")
            .param("prüfer", "Dr. Schmidt")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("Bachelorarbeit data and expose file received"));

    verify(bachelorthesisService).createBachelorthesisRequest(any(BachelorthesisRequest.class));
  }
}



