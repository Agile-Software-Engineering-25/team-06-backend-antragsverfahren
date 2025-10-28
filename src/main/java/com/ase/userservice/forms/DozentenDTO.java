package com.ase.userservice.forms;

public class DozentenDTO {

  private String firstName;
  private String lastName;

  public DozentenDTO() {
  }

  public DozentenDTO(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
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
}

