package com.ase.userservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dozenten")
public class Dozent {

  @Id
  private String id;

  private String dateOfBirth;
  private String address;
  private String phoneNumber;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String employeeNumber;
  private String department;
  private String officeNumber;
  private String workingTimeModel;
  private String fieldChair;
  private String title;
  private String employmentStatus;

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

  public String getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getOfficeNumber() {
    return officeNumber;
  }

  public void setOfficeNumber(String officeNumber) {
    this.officeNumber = officeNumber;
  }

  public String getWorkingTimeModel() {
    return workingTimeModel;
  }

  public void setWorkingTimeModel(String workingTimeModel) {
    this.workingTimeModel = workingTimeModel;
  }

  public String getFieldChair() {
    return fieldChair;
  }

  public void setFieldChair(String fieldChair) {
    this.fieldChair = fieldChair;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEmploymentStatus() {
    return employmentStatus;
  }

  public void setEmploymentStatus(String employmentStatus) {
    this.employmentStatus = employmentStatus;
  }

}
