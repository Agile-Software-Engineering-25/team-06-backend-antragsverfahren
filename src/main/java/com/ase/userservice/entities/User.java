package com.ase.userservice.entities;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private LocalDate dateOfBirth;

  @Column
  private String address;

  @Column
  private String phoneNumber;

  @Column
  private String photoUrl;

  @Column(nullable = false, unique = true)
  private String matriculationNumber;

  @Column(nullable = false)
  private String degreeProgram;

  @Column(nullable = false)
  private Integer semester;

  @Column(nullable = false)
  private String studyStatus;

  @Column
  private String cohort;

  // Many-to-one relationship with Semester (a student belongs to one semester)
  @ManyToOne
  @JoinColumn(name = "semester_id")
  private Semester currentSemesterEntity;

  // Constructors
  public User() {}

  public User(String username, String firstName, String lastName, String email,
      LocalDate dateOfBirth, String address, String phoneNumber, String photoUrl,
      String matriculationNumber, String degreeProgram, Integer semester,
      String studyStatus, String cohort) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.photoUrl = photoUrl;
    this.matriculationNumber = matriculationNumber;
    this.degreeProgram = degreeProgram;
    this.semester = semester;
    this.studyStatus = studyStatus;
    this.cohort = cohort;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getMatriculationNumber() {
    return matriculationNumber;
  }

  public void setMatriculationNumber(String matriculationNumber) {
    this.matriculationNumber = matriculationNumber;
  }

  public String getDegreeProgram() {
    return degreeProgram;
  }

  public void setDegreeProgram(String degreeProgram) {
    this.degreeProgram = degreeProgram;
  }

  public Integer getSemester() {
    return semester;
  }

  public void setSemester(Integer semester) {
    this.semester = semester;
  }

  public String getStudyStatus() {
    return studyStatus;
  }

  public void setStudyStatus(String studyStatus) {
    this.studyStatus = studyStatus;
  }

  public String getCohort() {
    return cohort;
  }

  public void setCohort(String cohort) {
    this.cohort = cohort;
  }

  public Semester getCurrentSemesterEntity() {
    return currentSemesterEntity;
  }

  public void setCurrentSemesterEntity(Semester currentSemesterEntity) {
    this.currentSemesterEntity = currentSemesterEntity;
  }
}
