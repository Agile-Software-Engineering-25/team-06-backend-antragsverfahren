package com.ase.userservice.forms;

import lombok.Data;

import java.util.List;

// Module template
@Data
public class ModuleTemplateDTO {
  private Long id;
  private String name;
  private List<CourseTemplateDTO> course_templates;
}
