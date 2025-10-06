package com.ase.userservice.repositories;

import com.ase.userservice.entities.BachelorthesisRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BachelorthesisRepository extends
    JpaRepository<BachelorthesisRequest, Long> {

  BachelorthesisRequest getBachelorthesisRequestByMatrikelnummer(
      String matrikelnummer);

}
