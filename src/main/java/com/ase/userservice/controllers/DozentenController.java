// java
package com.ase.userservice.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.ase.userservice.authentication.CurrentAuthContext;
import com.ase.userservice.entities.Dozent;
import com.ase.userservice.forms.DozentenDTO;
import com.ase.userservice.repositories.DozentenRepository;

@RestController
@RequestMapping("/dozenten")
public class DozentenController {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${external.users.url:https://sau-portal.de/team-11-api/api/v1/users?userType=lecturer}")
  private String externalUsersUrl;


  @GetMapping
  public List<Dozent> getAllDozenten() {
    String url = externalUsersUrl;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.extractToken());


    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<Dozent[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Dozent[].class);
    Dozent[] dozenten = response.getBody();
    return Arrays.asList(dozenten != null ? dozenten : new Dozent[0]);
  }

  @GetMapping("/names")
  public List<DozentenDTO> getDozentenNames() {
    String url = externalUsersUrl;

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + CurrentAuthContext.extractToken());

    HttpEntity<Void> entity = new HttpEntity<>(headers);
    ResponseEntity<Dozent[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Dozent[].class);
    Dozent[] dozenten = response.getBody();

    return Arrays.stream(dozenten != null ? dozenten : new Dozent[0])
        .map(dozent -> new DozentenDTO(dozent.getFirstName(), dozent.getLastName()))
        .collect(Collectors.toList());
  }

}
