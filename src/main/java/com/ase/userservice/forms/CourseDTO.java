package com.ase.userservice.forms;

import lombok.Data;

import java.util.List;

// Course inside a module
@Data
public class CourseDTO {
  private Long id;
  private Integer semester;
  private String exam_type;
  private Integer credit_points;
  private Integer total_units;
  private Long template_id;
  private CourseTemplateDTO template;
  private List<ProgramStudentDTO> students;
  private List<ProgramLecturerDTO> teachers;
}
