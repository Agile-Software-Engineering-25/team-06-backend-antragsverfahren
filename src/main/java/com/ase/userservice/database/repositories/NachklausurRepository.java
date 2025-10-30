package com.ase.userservice.database.repositories;

import com.ase.userservice.database.entities.BachelorthesisRequest;
import com.ase.userservice.database.entities.NachklausurRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NachklausurRepository extends
    JpaRepository<NachklausurRequest, Long> {

  NachklausurRequest getRequestByMatrikelnummer(
      String matrikelnummer);

}
