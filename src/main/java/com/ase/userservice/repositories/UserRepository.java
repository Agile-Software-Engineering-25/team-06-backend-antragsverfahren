package com.ase.userservice.repositories;

import com.ase.userservice.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByMatriculationNumber(String matriculationNumber);
}
