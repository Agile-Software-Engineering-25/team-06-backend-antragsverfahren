package com.ase.userservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dozenten")
public class Dozent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nachname;

  @Column(nullable = false)
  private String vorname;

  @Column
  private String titel;

  // Constructors
  public Dozent() {}

  public Dozent(String nachname, String vorname, String titel) {
    this.nachname = nachname;
    this.vorname = vorname;
    this.titel = titel;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNachname() {
    return nachname;
  }

  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  public String getVorname() {
    return vorname;
  }

  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }
}

