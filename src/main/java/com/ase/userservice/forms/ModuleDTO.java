package com.ase.userservice.forms;

import lombok.Data;

import java.util.List;

// Module inside the program
@Data
public class ModuleDTO {
  private Long id;
  private ModuleTemplateDTO template;
  private List<CourseDTO> courses;
}
