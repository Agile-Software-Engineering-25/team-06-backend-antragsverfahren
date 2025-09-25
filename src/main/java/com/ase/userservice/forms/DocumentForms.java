package com.ase.userservice.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DocumentForms {

    public static class NachklausurForm {

        @NotBlank
        @Size(min = 2, max = 30)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NotBlank
        @Size(min = 4, max = 100)
        private String matrikelnummer;

        public String getMatrikelnummer() {
            return matrikelnummer;
        }

        public void setMatrikelnummer(String matrikelnummer) {
            this.matrikelnummer = matrikelnummer;
        }

        @NotBlank
        @Size(min = 2, max = 100)
        private String modul;

        public String getModul() {
            return modul;
        }

        public void setModul(String modul) {
            this.modul = modul;
        }

        @NotBlank
        @Size(min = 2, max = 100)
        private String prüfungstermin;

        public String getPrüfungstermin() {
            return prüfungstermin;
        }

        public void setPrüfungstermin(String prüfungstermin) {
            this.prüfungstermin = prüfungstermin;
        }


    }

    public static class BachelorarbeitForm {

        @NotNull
        @Size(min = 2, max = 30)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NotNull
        @Size(min = 4, max = 4)
        private String matrikelnummer;

        public String getMatrikelnummer() {
            return matrikelnummer;
        }

        public void setMatrikelnummer(String matrikelnummer) {
            this.matrikelnummer = matrikelnummer;
        }

        @NotNull
        @Size(min = 2, max = 100)
        private String modul;

        public String getModul() {
            return modul;
        }

        public void setModul(String modul) {
            this.modul = modul;
        }

        @NotNull
        @Size(min = 2, max = 100)
        private String prüfungstermin;

        public String getPrüfungstermin() {
            return prüfungstermin;
        }

        public void setPrüfungstermin(String prüfungstermin) {
            this.prüfungstermin = prüfungstermin;
        }

        private String thema;

        @NotNull
        @Size(min = 5, max = 200)
        public String getThema() {
            return thema;
        }

        public void setThema(String thema) {
            this.thema = thema;
        }

        @NotNull
        @Size(min = 2, max = 100)
        private String firstExaminer;

        public String getFirstExaminer() {
            return firstExaminer;
        }

        public void setFirstExaminer(String firstExaminer) {
            this.firstExaminer = firstExaminer;
        }

        @NotNull
        @Size(min = 2, max = 100)
        private String secondExaminer;

        public String getSecondExaminer() {
            return secondExaminer;
        }

        public void setSecondExaminer(String secondExaminer) {
            this.secondExaminer = secondExaminer;
        }
    }
}
