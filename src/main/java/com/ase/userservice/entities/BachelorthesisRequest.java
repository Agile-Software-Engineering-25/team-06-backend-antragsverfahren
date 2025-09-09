package com.ase.userservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class BachelorthesisRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String matriculationNumber;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String courseOfStudy;

  @Column(nullable = false)
  private String topic;

  @Column(nullable = false)
  private String firstExaminer;

  @Column(nullable = false)
  private String secondExaminer;

  @Column(nullable = false)
  private LocalDate examDate;

  // Constructors
  public BachelorthesisRequest() {}

  public BachelorthesisRequest(String matriculationNumber, String name,
      String courseOfStudy,
      String topic, String firstExaminer, String secondExaminer, LocalDate examDate) {
    this.matriculationNumber = matriculationNumber;
    this.name = name;
    this.courseOfStudy = courseOfStudy;
    this.topic = topic;
    this.firstExaminer = firstExaminer;
    this.secondExaminer = secondExaminer;
    this.examDate = examDate;
  }

  public String getMatriculationNumber() {
    return matriculationNumber;
  }

  public void setMatriculationNumber(String matriculationNumber) {
    this.matriculationNumber = matriculationNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCourseOfStudy() {
    return courseOfStudy;
  }

  public void setCourseOfStudy(String courseOfStudy) {
    this.courseOfStudy = courseOfStudy;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getFirstExaminer() {
    return firstExaminer;
  }

  public void setFirstExaminer(String firstExaminer) {
    this.firstExaminer = firstExaminer;
  }

  public String getSecondExaminer() {
    return secondExaminer;
  }

  public void setSecondExaminer(String secondExaminer) {
    this.secondExaminer = secondExaminer;
  }

  public LocalDate getExamDate() {
    return examDate;
  }

  public void setExamDate(LocalDate examDate) {
    this.examDate = examDate;
  }
}
