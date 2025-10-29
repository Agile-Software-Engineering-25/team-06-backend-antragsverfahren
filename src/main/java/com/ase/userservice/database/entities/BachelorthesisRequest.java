package com.ase.userservice.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Entity
@Table(name = "bachelorthesisrequests")
@Getter
@ToString
public class BachelorthesisRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private final String matrikelnummer;

  @Column(nullable = false)
  private final String lastName;

  @Column(nullable = false)
  private final String firstName;

  @Column(nullable = false)
  private final String studiengang;

  @Column(nullable = false)
  private final String thema;

  @Column(nullable = false)
  private final String pruefer;

  @Column(nullable = false)
  private final String pruefungstermin;

  @Column(nullable = false, length = 10485760)
  private final byte[] expose;

  // No-args constructor for JPA
  public BachelorthesisRequest() {
    this.id = null;
    this.matrikelnummer = null;
    this.lastName = null;
    this.firstName = null;
    this.studiengang = null;
    this.thema = null;
    this.pruefer = null;
    this.pruefungstermin = null;
    this.expose = null;
  }

  public BachelorthesisRequest(String matrikelnummer, String lastName, String firstName,
                               String studiengang, String thema,
                               String pruefer, String pruefungstermin,
                               byte[] expose) {
    this.matrikelnummer = matrikelnummer;
    this.lastName = lastName;
    this.firstName = firstName;
    this.studiengang = studiengang;
    this.thema = thema;
    this.pruefer = pruefer;
    this.pruefungstermin = pruefungstermin;
    this.expose = expose;
  }
}
