package com.ase.userservice.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "semesters")
public class Semester {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String semesterCode; // e.g., "2025S", "2025W"

  @Column(nullable = false)
  private String semesterName;

  @Column(nullable = false)
  private LocalDate semesterStart;

  @Column(nullable = false)
  private LocalDate semesterEnd;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private SemesterType semesterType;

  @Column(nullable = false, name = "semester_year")
  private Integer year;

  @Column(nullable = false)
  private String degree; // Degree column

  // One-to-many relationship with User (one semester can have many students)
  @OneToMany(mappedBy = "currentSemesterEntity")
  private Set<User> students = new HashSet<>();

  // Constructors
  public Semester() {}

  public Semester(String semesterCode,
      String semesterName, LocalDate semesterStart, LocalDate semesterEnd,
      SemesterType semesterType, Integer year, String degree) {
    this.semesterCode = semesterCode;
    this.semesterName = semesterName;
    this.semesterStart = semesterStart;
    this.semesterEnd = semesterEnd;
    this.semesterType = semesterType;
    this.year = year;
    this.degree = degree;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSemesterCode() {
    return semesterCode;
  }

  public void setSemesterCode(String semesterCode) {
    this.semesterCode = semesterCode;
  }

  public String getSemesterName() {
    return semesterName;
  }

  public void setSemesterName(String semesterName) {
    this.semesterName = semesterName;
  }

  public LocalDate getSemesterStart() {
    return semesterStart;
  }

  public void setSemesterStart(LocalDate semesterStart) {
    this.semesterStart = semesterStart;
  }

  public LocalDate getSemesterEnd() {
    return semesterEnd;
  }

  public void setSemesterEnd(LocalDate semesterEnd) {
    this.semesterEnd = semesterEnd;
  }

  public SemesterType getSemesterType() {
    return semesterType;
  }

  public void setSemesterType(SemesterType semesterType) {
    this.semesterType = semesterType;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public Set<User> getStudents() {
    return students;
  }

  public void setStudents(Set<User> students) {
    this.students = students;
  }

  // Enum for semester types
  public enum SemesterType {
    SUMMER, WINTER
  }
}
