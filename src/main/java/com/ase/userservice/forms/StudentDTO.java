package com.ase.userservice.forms;

public class StudentDTO {

  private String id;
  private String dateOfBirth;
  private String address;
  private String phoneNumber;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String matriculationNumber;
  private String degreeProgram;
  private Integer semester;
  private String studyStatus;
  private String cohort;

  // ✅ Default constructor (needed for JSON deserialization)
  public StudentDTO() {}

  public StudentDTO(String id, String dateOfBirth, String address, String phoneNumber,
                    String username, String firstName, String lastName, String email,
                    String matriculationNumber, String degreeProgram, Integer semester,
                    String studyStatus, String cohort) {
    this.id = id;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.matriculationNumber = matriculationNumber;
    this.degreeProgram = degreeProgram;
    this.semester = semester;
    this.studyStatus = studyStatus;
    this.cohort = cohort;
  }

  // ✅ Getters and setters for all fields
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
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
}
