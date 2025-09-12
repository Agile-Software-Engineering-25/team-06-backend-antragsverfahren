package com.ase.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")
public class BachelorthesisRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String matrikelnummer;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String studiengang;

  @Column(nullable = false)
  private String thema;

  @Column(nullable = false)
  private String examiner;

  @Column(nullable = false)
  private String prüfungstermin;

  // Constructors
  public BachelorthesisRequest() {}

  public BachelorthesisRequest(String matrikelnummer, String name,
                               String studiengang,
                               String thema, String examiner, String prüfungstermin) {
    this.matrikelnummer = matrikelnummer;
    this.name = name;
    this.studiengang = studiengang;
    this.thema = thema;
    this.examiner = examiner;
    this.prüfungstermin = prüfungstermin;
  }

  public String getMatrikelnummer() {
    return matrikelnummer;
  }

  public String getName() {
    return name;
  }

  public String getStudiengang() {
    return studiengang;
  }


  public String getThema() {
    return thema;
  }

  public String getExaminer() {
    return examiner;
  }

  public String getPrüfungstermin() {
    return prüfungstermin;
  }

}
