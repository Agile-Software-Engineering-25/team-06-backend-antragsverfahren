package com.ase.userservice.repositories;

import com.ase.userservice.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findBySemesterCode(String semesterCode);
    Optional<Semester> findBySemesterType(Semester.SemesterType semesterType);
    List<Semester> findByDegree(String degree);
    List<Semester> findByDegreeAndSemesterType(String degree, Semester.SemesterType semesterType);
}
