package com.ase.userservice.forms;

import java.time.LocalDate;

/**
 * Einfache, zentrale Stelle für Validierungs-Helper.
 * Methoden sind bewusst minimal gehalten und leicht erweiterbar.
 * TODO: Datenbankabfragen einbauen, aktuell keine echte Sperre (geben true zurück).
 */
public final class Validator {

  private Validator() {
    // Utility class
  }

  public static boolean isUserValid(String name, String matrikelnummer) {
    if (isBlank(name) || isBlank(matrikelnummer)) return false;
    // TODO: add check user input with the db
    return true;
  }

  public static boolean existsUserByMatrikelnummer(String matrikelnummer) {
    if (isBlank(matrikelnummer)) return false;
    // TODO: query DB -> return actual existence
    return true;
  }

  public static boolean existsUserByName(String name) {
    if (isBlank(name)) return false;
    // TODO: query DB -> return actual existence
    return true;
  }

  public static boolean isModulValid(String modul) {
    if (isBlank(modul)) return false;
    // TODO: check modul against DB or allowed list
    return true;
  }

  public static boolean isExaminerValid(String examiner) {
    if (isBlank(examiner)) return false;
    // TODO: check examiner in DB
    return true;
  }

  public static boolean isPruefungsterminAllowed(LocalDate date) {
    if (date == null) return false;
    // Optional: keep basic future check, but DB availability check goes here later
    // TODO: check terminslots in DB
    return true;
  }

  private static boolean isBlank(String s) {
    return s == null || s.trim().isEmpty();
  }
}
