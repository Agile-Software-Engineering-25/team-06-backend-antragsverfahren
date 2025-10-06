package com.ase.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bachelorthesisrequests")
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
  private String pruefer;

  @Column(nullable = false)
  private String pruefungstermin;

  @Column(nullable = false, length = 10485760)
  private byte[] expose;

  // Constructors
  public BachelorthesisRequest() {}

  public BachelorthesisRequest(String matrikelnummer, String name,
      String studiengang,
      String thema, String pruefer,
      String pruefungstermin, byte[] expose) {
    this.matrikelnummer = matrikelnummer;
    this.name = name;
    this.studiengang = studiengang;
    this.thema = thema;
    this.pruefer = pruefer;
    this.pruefungstermin = pruefungstermin;
    this.expose = expose;
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

  public String getPruefer() {
    return pruefer;
  }

  public String getPruefungstermin() {
    return pruefungstermin;
  }

  public byte[] getExpose() {
    return expose;
  }

  public void setExpose(byte[] exposeDocument) {
    this.expose = exposeDocument;
  }

}
