package com.ase.userservice.repositories;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ase.userservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByMatriculationNumber(String matriculationNumber);
}
