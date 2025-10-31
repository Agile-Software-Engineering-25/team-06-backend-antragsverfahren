package com.ase.userservice.services;

import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.forms.LecturerDTO;
import com.ase.userservice.forms.ProgramDTO;
import com.ase.userservice.forms.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class StammdatenService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${external.users.url:https://sau-portal.de/team-11-api/api/v1}")
  private String externalApiUrlStudents;

  @Value("${external.users.url:https://sau-portal.de/api/masterdata/studies}")
  private String externalApiUrlPrograms;

  public List<LecturerDTO> fetchAllDozenten() {
    String endpoint = "/users?userType=lecturer";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.getToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<LecturerDTO[]> response = restTemplate.exchange(
        externalApiUrlStudents + endpoint,
        HttpMethod.GET,
        entity,
        LecturerDTO[].class
    );

    LecturerDTO[] lecturer = response.getBody();
    return Arrays.asList(lecturer != null ? lecturer : new LecturerDTO[0]);
  }

  public StudentDTO fetchUserInfo() {
    String endpoint = "/users/" + CurrentAuthContext.getSid();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.getToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<StudentDTO> response = restTemplate.exchange(
        externalApiUrlStudents + endpoint,
        HttpMethod.GET,
        entity,
        StudentDTO.class
    );

    StudentDTO studentDTO = response.getBody();
    return studentDTO != null ? studentDTO : new StudentDTO();
  }

  public ProgramDTO fetchProgramInfo(StudentDTO student) {
    String endpoint = "/courseofstudies/";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.getToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<ProgramDTO[]> response = restTemplate.exchange(
        externalApiUrlPrograms + endpoint,
        HttpMethod.GET,
        entity,
        ProgramDTO[].class
    );

    ProgramDTO[] programArray = response.getBody();
    if (programArray == null || programArray.length == 0) {
      throw new RuntimeException("No programs returned from API");
    }

    List<ProgramDTO> programs = Arrays.asList(programArray);

    String studentProgramName = student.getDegreeProgram();
    String studentId = student.getId();

    return programs.stream()
        .filter(p -> p.getName().equalsIgnoreCase(studentProgramName))
        .findFirst()
        .orElseGet(() ->
            programs.stream()
                .filter(pr -> pr.getModules().stream()
                    .flatMap(m -> m.getCourses().stream())
                    .flatMap(c -> c.getStudents().stream())
                    .anyMatch(s -> studentId.equals(s.getExternal_id())))
                .findFirst()
                .orElseGet(() -> {
                  System.err.println("No matching program found for Student " + studentId
                      + ". Falling back to first program.");
                  return programs.getFirst();
                })
        );
  }
}
