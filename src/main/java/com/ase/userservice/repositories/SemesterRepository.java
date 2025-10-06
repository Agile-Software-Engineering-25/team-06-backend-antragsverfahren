package com.ase.userservice.repositories;

import java.util.Optional;
import com.ase.userservice.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
  Optional<Semester> findBySemesterCode(String semesterCode);
}
