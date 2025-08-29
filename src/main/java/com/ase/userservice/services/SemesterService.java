package com.ase.userservice.services;

import com.ase.userservice.entities.Semester;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.SemesterRepository;
import com.ase.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    public Optional<Semester> getSemesterById(Long id) {
        return semesterRepository.findById(id);
    }

    public Optional<Semester> getSemesterByCode(String semesterCode) {
        return semesterRepository.findBySemesterCode(semesterCode);
    }

    public Optional<Semester> getSemesterByType(Semester.SemesterType semesterType) {
        return semesterRepository.findBySemesterType(semesterType);
    }

    public List<Semester> getSemestersByDegree(String degree) {
        return semesterRepository.findByDegree(degree);
    }

    public List<Semester> getSemestersByDegreeAndType(String degree, Semester.SemesterType semesterType) {
        return semesterRepository.findByDegreeAndSemesterType(degree, semesterType);
    }

    public Semester saveSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    public void deleteSemester(Long id) {
        semesterRepository.deleteById(id);
    }

    public void assignStudentToSemester(Long userId, Long semesterId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Semester> semesterOpt = semesterRepository.findById(semesterId);

        if (userOpt.isPresent() && semesterOpt.isPresent()) {
            User user = userOpt.get();
            Semester semester = semesterOpt.get();
            user.setCurrentSemesterEntity(semester);
            userRepository.save(user);
        }
    }

    public void removeStudentFromSemester(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setCurrentSemesterEntity(null);
            userRepository.save(user);
        }
    }
}
