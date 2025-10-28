package com.ase.userservice.authentication;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


public class CurrentAuthContext {

  public static Map<String, Object> extractClaim() {
    return getToken().getClaims();
  }

  public static String extractToken() {
    return getToken().getTokenValue();
  }

  public static Jwt getToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    return ((Jwt) principal);
  }


  public static String getSid() {
    return (String) extractClaim().get("sid");
  }
}
