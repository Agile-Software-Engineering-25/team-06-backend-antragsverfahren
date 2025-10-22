// java
package com.ase.userservice.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class DocumentForms {

  @Getter
  @Setter
  public static class NachklausurForm {

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    @NotBlank
    @jakarta.validation.constraints.Pattern(
        regexp = "^[A-Z]\\d{3}$",
        message = "Matrikelnummer muss ein Großbuchstabe gefolgt von drei Ziffern sein"
    )
    private String matrikelnummer;

    @NotBlank
    @Size(min = 2, max = 100)
    private String modul;

    @NotNull(message = "Prüfungstermin darf nicht leer sein")
    @Future(message = "Prüfungstermin muss in der Zukunft liegen")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate pruefungstermin;
  }

  @Getter
  @Setter
  public static class BachelorarbeitForm {

    @NotBlank
    @Size(min = 2, max = 30)
    private String name;

    @NotBlank
    @jakarta.validation.constraints.Pattern(
        regexp = "^[A-Z]\\d{3}$",
        message = "Matrikelnummer muss ein Großbuchstabe gefolgt von drei Ziffern sein"
    )
    private String matrikelnummer;

    @NotBlank
    @Size(min = 2, max = 100)
    private String modul;

    @NotNull(message = "Prüfungstermin darf nicht leer sein")
    @Future(message = "Prüfungstermin muss in der Zukunft liegen")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate pruefungstermin;

    @NotBlank
    @Size(min = 5, max = 200)
    private String thema;

    @NotBlank
    @Size(min = 2, max = 100)
    private String examiner;
  }
}
