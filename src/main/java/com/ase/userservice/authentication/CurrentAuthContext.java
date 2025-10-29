package com.ase.userservice.authentication;

import java.security.Principal;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


public class CurrentAuthContext {

  public static Map<String, Object> getClaims() {
    return ((Jwt) getAuthentication().getPrincipal()).getClaims();
  }

  public static String getToken() {
    return ((Jwt) getAuthentication().getPrincipal()).getTokenValue();
  }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static String getSid() {
    return (String) getClaims().get("sub");
  }
}
