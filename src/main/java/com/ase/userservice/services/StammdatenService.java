package com.ase.userservice.services;

import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.forms.LecturerDTO;
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
  private String externalApiUrl;

  public List<LecturerDTO> fetchAllDozenten() {
    String endpoint = "/users?userType=lecturer";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.getToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<LecturerDTO[]> response = restTemplate.exchange(
        externalApiUrl + endpoint,
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
        externalApiUrl + endpoint,
        HttpMethod.GET,
        entity,
        StudentDTO.class
    );

    StudentDTO studentDTO = response.getBody();
    return studentDTO != null ? studentDTO : new StudentDTO();
  }
}
