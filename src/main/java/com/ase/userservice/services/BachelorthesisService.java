package com.ase.userservice.services;

import com.ase.userservice.entities.BachelorthesisRequest;
import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.BachelorthesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class BachelorthesisService {

  private final BachelorthesisRepository bachelorthesisRepository;

  @Autowired
  public BachelorthesisService(BachelorthesisRepository bachelorthesisRepository) {
    this.bachelorthesisRepository = bachelorthesisRepository;
  }

  public void createBachelorthesisRequest(BachelorthesisRequest bachelorthesisRequest) {
    bachelorthesisRepository.saveAndFlush(bachelorthesisRequest);
  }

  public BachelorthesisRequest getBachelorthesisRequestByMatrikelnummer(String matrikelnummer) {
    return bachelorthesisRepository.getBachelorthesisRequestByMatrikelnummer(matrikelnummer);
  }

  public void sendBachelorthesisApplicationByEmail(User user, byte[] pdfContent, boolean isEnglish) {
    EmailService.sendBachelorthesisApplicationByMail(user, pdfContent, isEnglish);
  }
}
