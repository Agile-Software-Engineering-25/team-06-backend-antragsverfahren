package com.ase.userservice.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Bean
  @Profile("!dev")
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .oauth2ResourceServer(oauth2 ->
          oauth2.jwt(jwt ->
            jwt.jwtAuthenticationConverter(
                jwtAuthenticationConverter()
            )
          )
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET,"/api/antrag", "/api/antrag/").permitAll()
            .requestMatchers(HttpMethod.GET,"/api/antrag/**").hasRole("Area-2.Team-6.Read.antrag-read")
            .requestMatchers(HttpMethod.HEAD,"/api/antrag/**").hasRole("Area-2.Team-6.Read.antrag-read")
            .requestMatchers(HttpMethod.POST,"/api/antrag/**").hasRole("Area-2.Team-6.Update.antrag-update")
            .requestMatchers(HttpMethod.PUT,"/api/antrag/**").hasRole("Area-2.Team-6.Update.antrag-update")
            .requestMatchers(HttpMethod.PATCH,"/api/antrag/**").hasRole("Area-2.Team-6.Update.antrag-update")
            .requestMatchers(HttpMethod.DELETE,"/api/antrag/**").hasRole("Area-2.Team-6.Delete.antrag-delete")
            .anyRequest().authenticated()
        );
    return http.build();
  }

  @Bean
  @Profile("dev")
  protected SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
    http
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(
                    jwtAuthenticationConverter()
                )
            )
        )
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/", "/h2-console/**").permitAll()
            .anyRequest().hasRole("Area-2.Team-6.Read.antrag-read")
        );
    return http.build();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter =
        new KeycloakRolesJwtGrantedAuthoritiesConverter();
    JwtAuthenticationConverter jwtAuthenticationConverter =
        new JwtAuthenticationConverter();
    jwtAuthenticationConverter
        .setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}

