package com.ase.userservice.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ase.userservice.entities.Semester;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
  Optional<Semester> findBySemesterCode(String semesterCode);
}
