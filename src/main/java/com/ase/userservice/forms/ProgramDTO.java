package com.ase.userservice.forms;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

// Top-level array element
@Data
public class ProgramDTO {
  private Long id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private TemplateDTO template;
  private List<ModuleDTO> modules;
}

