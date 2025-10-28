package com.ase.userservice.controller;
import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.config.SecurityConfig;
import com.ase.userservice.controllers.UserController;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserRepository userRepository;

  // GET /users
  @Test
  void testGetAllUsers() throws Exception {
    User u1 = new User();
    u1.setId(1L);
    u1.setFirstName("Alice");
    u1.setLastName("Müller");
    u1.setDateOfBirth(LocalDate.of(2000, 1, 15));
    u1.setMatriculationNumber("12345");
    u1.setStudyProgram("Informatik");
    u1.setDegree("B.Sc.");
    u1.setCurrentSemester(3);
    u1.setStandardStudyDuration(6);
    u1.setStudyStartSemester("WS2020");
    u1.setStudyEndSemester("SS2023");
    u1.setUniversitySemester(3);
    u1.setLeaveOfAbsenceSemesters(0);
    u1.setEmail("alice@example.com");

    User u2 = new User();
    u2.setId(2L);
    u2.setFirstName("Bob");
    u2.setLastName("Schmidt");
    u2.setDateOfBirth(LocalDate.of(1999, 5, 10));
    u2.setMatriculationNumber("67890");
    u2.setStudyProgram("BWL");
    u2.setDegree("B.Sc.");
    u2.setCurrentSemester(5);
    u2.setStandardStudyDuration(6);
    u2.setStudyStartSemester("WS2019");
    u2.setStudyEndSemester("SS2022");
    u2.setUniversitySemester(5);
    u2.setLeaveOfAbsenceSemesters(0);
    u2.setEmail("bob@example.com");

    when(userRepository.findAll()).thenReturn(List.of(u1, u2));

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].firstName").value("Alice"))
        .andExpect(jsonPath("$[1].firstName").value("Bob"));

    verify(userRepository).findAll();
  }

  // GET /users/{matriculationNumber} - found
  @Test
  void testGetUserByMatriculationNumber_found() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setFirstName("Anna");
    user.setLastName("Schneider");
    user.setDateOfBirth(LocalDate.of(2001, 3, 20));
    user.setMatriculationNumber("12345");
    user.setStudyProgram("Informatik");
    user.setDegree("B.Sc.");
    user.setCurrentSemester(2);
    user.setStandardStudyDuration(6);
    user.setStudyStartSemester("WS2020");
    user.setStudyEndSemester("SS2023");
    user.setUniversitySemester(2);
    user.setLeaveOfAbsenceSemesters(0);
    user.setEmail("anna@example.com");

    when(userRepository.findByMatriculationNumber("12345")).thenReturn(Optional.of(user));

    mockMvc.perform(get("/users/12345"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Anna"))
        .andExpect(jsonPath("$.lastName").value("Schneider"))
        .andExpect(jsonPath("$.matriculationNumber").value("12345"))
        .andExpect(jsonPath("$.studyProgram").value("Informatik"));

    verify(userRepository).findByMatriculationNumber("12345");
  }

  // GET /users/{matriculationNumber} - not found
  @Test
  void testGetUserByMatriculationNumber_notFound() throws Exception {
    when(userRepository.findByMatriculationNumber("99999")).thenReturn(Optional.empty());

    mockMvc.perform(get("/users/99999"))
        .andExpect(status().isNotFound());

    verify(userRepository).findByMatriculationNumber("99999");
  }

  // GET /users/claims
  @Test
  void testGetUserClaims() throws Exception {
    Map<String, Object> claims = Map.of("role", "STUDENT", "email", "test@example.com");

    try (MockedStatic<CurrentAuthContext> mockedStatic = mockStatic(CurrentAuthContext.class)) {
      mockedStatic.when(CurrentAuthContext::extractClaim).thenReturn(claims);

      mockMvc.perform(get("/users/claims"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.role").value("STUDENT"))
          .andExpect(jsonPath("$.email").value("test@example.com"));
    }
  }

  // POST /users
  @Test
  void testCreateUser() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setFirstName("Lena");
    user.setLastName("Meier");
    user.setDateOfBirth(LocalDate.of(2000, 7, 5));
    user.setMatriculationNumber("13579");
    user.setStudyProgram("Mathematik");
    user.setDegree("B.Sc.");
    user.setCurrentSemester(4);
    user.setStandardStudyDuration(6);
    user.setStudyStartSemester("WS2019");
    user.setStudyEndSemester("SS2022");
    user.setUniversitySemester(4);
    user.setLeaveOfAbsenceSemesters(0);
    user.setEmail("lena@example.com");

    when(userRepository.save(any(User.class))).thenReturn(user);

    String body = """
            {
              "firstName": "Lena",
              "lastName": "Meier",
              "dateOfBirth": "2000-07-05",
              "matriculationNumber": "13579",
              "studyProgram": "Mathematik",
              "degree": "B.Sc.",
              "currentSemester": 4,
              "standardStudyDuration": 6,
              "studyStartSemester": "WS2019",
              "studyEndSemester": "SS2022",
              "universitySemester": 4,
              "leaveOfAbsenceSemesters": 0,
              "email": "lena@example.com"
            }
            """;

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Lena"))
        .andExpect(jsonPath("$.lastName").value("Meier"))
        .andExpect(jsonPath("$.matriculationNumber").value("13579"))
        .andExpect(jsonPath("$.studyProgram").value("Mathematik"));

    verify(userRepository).save(any(User.class));
  }
}
