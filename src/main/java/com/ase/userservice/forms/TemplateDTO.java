package com.ase.userservice.forms;

import lombok.Data;

import java.util.List;

// Template object
@Data
public class TemplateDTO {
  private Long id;
  private String name;
  private String degree_type; // For program templates
  private Integer planned_semesters; // For program templates
  private Boolean part_time; // For program templates
  private List<ModuleTemplateDTO> module_templates; // For program templates
}
