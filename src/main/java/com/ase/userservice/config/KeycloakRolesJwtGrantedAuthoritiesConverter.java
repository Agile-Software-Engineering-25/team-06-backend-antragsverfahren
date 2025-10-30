package com.ase.userservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakRolesJwtGrantedAuthoritiesConverter
    implements Converter<Jwt, Collection<GrantedAuthority>> {

  private final JwtGrantedAuthoritiesConverter defaultConverter
      = new JwtGrantedAuthoritiesConverter();

  @Override
  public Collection<GrantedAuthority> convert(final Jwt jwt) {
    return Stream.concat(
            defaultConverter.convert(jwt).stream(),
            extractRoles(jwt).stream()
        )
        .collect(Collectors.toSet());
  }

  private Collection<? extends GrantedAuthority> extractRoles(final Jwt jwt) {
    return Optional.ofNullable(
            jwt.getClaimAsMap("realm_access")
        )
        .map(client -> client.get("roles"))
        .map(Collection.class::cast)
        .map(roles -> ((Collection<String>) roles).stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet())
        ).orElse(Collections.emptySet());
  }
}


