package com.ase.userservice.controller;

import com.ase.userservice.config.SecurityConfig;
import com.ase.userservice.controllers.DozentenController;
import com.ase.userservice.entities.Dozent;
import com.ase.userservice.repositories.DozentenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DozentenController.class)
@Import(SecurityConfig.class)
class DozentenControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DozentenRepository dozentenRepository;

  // GET /dozenten
  @Test
  void testGetAllDozenten() throws Exception {
    Dozent d1 = new Dozent();
    d1.setId(1L);
    d1.setVorname("Max");
    d1.setNachname("Mustermann");
    d1.setTitel("Prof.");

    Dozent d2 = new Dozent();
    d2.setId(2L);
    d2.setVorname("Erika");
    d2.setNachname("Musterfrau");
    d2.setTitel(null);

    when(dozentenRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

    mockMvc.perform(get("/dozenten"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].vorname").value("Max"))
        .andExpect(jsonPath("$[0].titel").value("Prof."))
        .andExpect(jsonPath("$[1].vorname").value("Erika"))
        .andExpect(jsonPath("$[1].titel").doesNotExist()); // falls null

    verify(dozentenRepository).findAll();
  }

  // GET /dozenten/{id} - found
  @Test
  void testGetDozentById_found() throws Exception {
    Dozent d = new Dozent();
    d.setId(1L);
    d.setVorname("Anna");
    d.setNachname("Schmidt");
    d.setTitel("Dr.");

    when(dozentenRepository.findById(1L)).thenReturn(Optional.of(d));

    mockMvc.perform(get("/dozenten/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.vorname").value("Anna"))
        .andExpect(jsonPath("$.nachname").value("Schmidt"))
        .andExpect(jsonPath("$.titel").value("Dr."));

    verify(dozentenRepository).findById(1L);
  }

  // GET /dozenten/{id} - not found
  @Test
  void testGetDozentById_notFound() throws Exception {
    when(dozentenRepository.findById(99L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/dozenten/99"))
        .andExpect(status().isNotFound());

    verify(dozentenRepository).findById(99L);
  }

  // POST /dozenten
  @Test
  void testCreateDozent() throws Exception {
    Dozent d = new Dozent();
    d.setId(1L);
    d.setVorname("Lena");
    d.setNachname("Meier");
    d.setTitel("Prof.");

    when(dozentenRepository.save(any(Dozent.class))).thenReturn(d);

    String body = """
            {
              "vorname": "Lena",
              "nachname": "Meier",
              "titel": "Prof."
            }
            """;

    mockMvc.perform(post("/dozenten")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.vorname").value("Lena"))
        .andExpect(jsonPath("$.nachname").value("Meier"))
        .andExpect(jsonPath("$.titel").value("Prof."));

    verify(dozentenRepository).save(any(Dozent.class));
  }
}


