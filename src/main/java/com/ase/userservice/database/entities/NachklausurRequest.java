package com.ase.userservice.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Entity
@Table(name = "nachklausurrequests")
@Getter
@ToString
public class NachklausurRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private final String lastName;

  @Column(nullable = false)
  private final String firstName;

  @Column(nullable = false)
  private final String matrikelnummer;

  @Column(nullable = false)
  private final String modul;

  @Column(nullable = false)
  private final String pruefungstermin;

  protected NachklausurRequest() {
    this.lastName = null;
    this.firstName = null;
    this.matrikelnummer = null;
    this.modul = null;
    this.pruefungstermin = null;
  }

  public NachklausurRequest(
      String lastName,
      String firstName,
      String matrikelnummer,
      String modul,
      String pruefungstermin
  ) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.matrikelnummer = matrikelnummer;
    this.modul = modul;
    this.pruefungstermin = pruefungstermin;
  }

}
