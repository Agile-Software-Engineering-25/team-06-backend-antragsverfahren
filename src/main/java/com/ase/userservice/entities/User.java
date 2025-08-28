package com.ase.userservice.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String matriculationNumber;

    @Column(nullable = false)
    private String studyProgram;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private Integer currentSemester;

    @Column(nullable = false)
    private Integer standardStudyDuration;

    @Column(nullable = false)
    private String studyStartSemester;

    @Column(nullable = false)
    private Integer universitySemester;

    @Column(nullable = false)
    private Integer leaveOfAbsenceSemesters;

    @Column(nullable = false)
    private String email;

    // Many-to-one relationship with Semester (a student belongs to one semester)
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester currentSemesterEntity;

    // Constructors
    public User() {}

    public User(String firstName, String lastName, LocalDate dateOfBirth, String matriculationNumber,
                String studyProgram, String degree, Integer currentSemester, Integer standardStudyDuration,
                String studyStartSemester, Integer universitySemester, Integer leaveOfAbsenceSemesters, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.matriculationNumber = matriculationNumber;
        this.studyProgram = studyProgram;
        this.degree = degree;
        this.currentSemester = currentSemester;
        this.standardStudyDuration = standardStudyDuration;
        this.studyStartSemester = studyStartSemester;
        this.universitySemester = universitySemester;
        this.leaveOfAbsenceSemesters = leaveOfAbsenceSemesters;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getMatriculationNumber() { return matriculationNumber; }
    public void setMatriculationNumber(String matriculationNumber) { this.matriculationNumber = matriculationNumber; }

    public String getStudyProgram() { return studyProgram; }
    public void setStudyProgram(String studyProgram) { this.studyProgram = studyProgram; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public Integer getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(Integer currentSemester) { this.currentSemester = currentSemester; }

    public Integer getStandardStudyDuration() { return standardStudyDuration; }
    public void setStandardStudyDuration(Integer standardStudyDuration) { this.standardStudyDuration = standardStudyDuration; }

    public String getStudyStartSemester() { return studyStartSemester; }
    public void setStudyStartSemester(String studyStartSemester) { this.studyStartSemester = studyStartSemester; }

    public Integer getUniversitySemester() { return universitySemester; }
    public void setUniversitySemester(Integer universitySemester) { this.universitySemester = universitySemester; }

    public Integer getLeaveOfAbsenceSemesters() { return leaveOfAbsenceSemesters; }
    public void setLeaveOfAbsenceSemesters(Integer leaveOfAbsenceSemesters) { this.leaveOfAbsenceSemesters = leaveOfAbsenceSemesters; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Semester getCurrentSemesterEntity() { return currentSemesterEntity; }
    public void setCurrentSemesterEntity(Semester currentSemesterEntity) { this.currentSemesterEntity = currentSemesterEntity; }
}
