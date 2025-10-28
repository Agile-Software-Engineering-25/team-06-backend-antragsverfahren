package com.ase.userservice.entities;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NachklausurRequest {
  private String name;
  private String matrikelnummer;
  private String modul;
  private LocalDate pruefungstermin;

  public NachklausurRequest(String name,
      String matriculationNumber,
      String modul,
      LocalDate pruefungstermin) {
    this.name = name;
    this.matrikelnummer = matriculationNumber;
    this.modul = modul;
    this.pruefungstermin = pruefungstermin;
  }

  public String getName() {
    return name;
  }

  public String getMatrikelnummer() {
    return matrikelnummer;
  }

  public String getModul() {
    return modul;
  }

  public LocalDate getPruefungstermin() {
    return pruefungstermin;
  }
}
