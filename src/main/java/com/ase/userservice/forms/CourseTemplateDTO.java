package com.ase.userservice.forms;

import lombok.Data;

// Course template
@Data
public class CourseTemplateDTO {
  private Long id;
  private String name;
  private String code;
  private Boolean elective;
  private Integer planned_semester;
}
