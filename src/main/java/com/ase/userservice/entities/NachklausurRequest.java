package com.ase.userservice.entities;

import lombok.Data;

@Data
public class NachklausurRequest {
  private String name;
  private String matrikelnummer;
  private String modul;
  private String pruefungstermin;

  public NachklausurRequest(String name,
      String matriculationNumber,
      String modul,
      String pruefungstermin) {
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

  public String getPruefungstermin() {
    return pruefungstermin;
  }
}
