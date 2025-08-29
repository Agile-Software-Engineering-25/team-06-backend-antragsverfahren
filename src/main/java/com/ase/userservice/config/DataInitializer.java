package com.ase.userservice.config;

import com.ase.userservice.entities.Semester;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.SemesterRepository;
import com.ase.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (semesterRepository.count() == 0) {
            System.out.println("Initializing semester data...");

            // Create Summer Semester 2025
            Semester summerSemester = new Semester();
            summerSemester.setSemesterCode("2025S");
            summerSemester.setSemesterName("Summer Semester 2025");
            summerSemester.setSemesterStart(LocalDate.of(2025, 6, 10));
            summerSemester.setSemesterEnd(LocalDate.of(2025, 11, 24));
            summerSemester.setSemesterType(Semester.SemesterType.SUMMER);
            summerSemester.setYear(2025);
            summerSemester.setDegree("BACHELOR");

            Semester savedSummerSemester = semesterRepository.save(summerSemester);

            // Create Winter Semester 2025/2026
            Semester winterSemester = new Semester();
            winterSemester.setSemesterCode("2025W");
            winterSemester.setSemesterName("Winter Semester 2025/2026");
            winterSemester.setSemesterStart(LocalDate.of(2025, 10, 1));
            winterSemester.setSemesterEnd(LocalDate.of(2026, 3, 31));
            winterSemester.setSemesterType(Semester.SemesterType.WINTER);
            winterSemester.setYear(2025);
            winterSemester.setDegree("BACHELOR");

            semesterRepository.save(winterSemester);

            System.out.println("Semester data created successfully!");

            // Create test user and assign to summer semester
            if (userRepository.count() == 0) {
                System.out.println("Initializing test user...");

                User testUser = new User();
                testUser.setFirstName("Test");
                testUser.setLastName("Test");
                testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
                testUser.setMatriculationNumber("123456");
                testUser.setStudyProgram("INFORMATIK");
                testUser.setDegree("BACHELOR");
                testUser.setCurrentSemester(1);
                testUser.setStandardStudyDuration(6);
                testUser.setStudyStartSemester("2023-10-01");
                testUser.setUniversitySemester(1);
                testUser.setLeaveOfAbsenceSemesters(0);
                testUser.setEmail("test@test.de");
                testUser.setCurrentSemesterEntity(savedSummerSemester);

                userRepository.save(testUser);
                System.out.println("Test user created and assigned to summer semester!");
            }
        } else {
            System.out.println("Data already exists, skipping initialization.");
        }
    }
}
