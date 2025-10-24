package com.ase.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ase.userservice.entities.Dozent;

@Repository
public interface DozentenRepository extends JpaRepository<Dozent, Long> {
}

