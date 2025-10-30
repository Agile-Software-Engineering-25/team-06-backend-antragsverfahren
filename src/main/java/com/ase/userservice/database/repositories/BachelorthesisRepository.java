package com.ase.userservice.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ase.userservice.database.entities.BachelorthesisRequest;

@Repository
public interface BachelorthesisRepository extends
    JpaRepository<BachelorthesisRequest, Long> {

  BachelorthesisRequest getRequestByMatrikelnummer(
      String matrikelnummer);

}

