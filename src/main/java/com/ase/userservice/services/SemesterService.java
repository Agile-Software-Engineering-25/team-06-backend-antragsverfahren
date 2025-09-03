package com.ase.userservice.services;

import com.ase.userservice.entities.Semester;
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
}
