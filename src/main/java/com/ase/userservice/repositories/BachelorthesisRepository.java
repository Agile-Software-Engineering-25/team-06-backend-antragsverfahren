package com.ase.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ase.userservice.entities.BachelorthesisRequest;

@Repository
public interface BachelorthesisRepository extends
    JpaRepository<BachelorthesisRequest, Long> {

  BachelorthesisRequest getBachelorthesisRequestByMatrikelnummer(
      String matrikelnummer);

}
