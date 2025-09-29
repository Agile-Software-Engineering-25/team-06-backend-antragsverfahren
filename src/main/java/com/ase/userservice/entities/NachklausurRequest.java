package com.ase.userservice.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NachklausurRequest {
    private String name;
    private String matrikelnummer;
    private String modul;
    private String prüfungstermin;

    public NachklausurRequest(String name, String matriculationNumber, String modul, String prüfungstermin) {
        this.name = name;
        this.matrikelnummer = matriculationNumber;
        this.modul = modul;
        this.prüfungstermin = prüfungstermin;
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

    public String getPrüfungstermin() {
        return prüfungstermin;
    }
}
