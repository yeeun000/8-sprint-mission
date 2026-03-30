package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.auth.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.handler.SpaCsrfTokenRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler loginSuccessHandler,
      LoginFailureHandler loginFailureHandler) throws Exception {

    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .anyRequest().authenticated()
        );

    SecurityFilterChain chain = http.build();
    log.info("=== Spring Security Filter List ===");
    chain.getFilters().forEach(filter ->
        log.info(filter.getClass().getSimpleName())
    );
    return chain;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
