package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.auth.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.auth.handler.SpaCsrfTokenRequestHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Slf4j
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      SessionRegistry sessionRegistry,
      LoginSuccessHandler loginSuccessHandler,
      LoginFailureHandler loginFailureHandler
  ) throws Exception {

    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
            .accessDeniedHandler((request, response, accessDeniedException) ->
                response.sendError(HttpServletResponse.SC_FORBIDDEN))
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/index.html", "/favicon.ico").permitAll()
            .requestMatchers("/assets/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()

            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()   // 회원가입
            .requestMatchers("/api/auth/login").permitAll()               // 로그인
            .requestMatchers("/api/auth/logout").permitAll()              // 로그아웃
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        ).sessionManagement(management -> management
            .sessionConcurrency(concurrency -> concurrency
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
            )
        ).rememberMe(remember -> remember
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(60)
            .key("remember-key")
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

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchy hierarchy = RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER");
    return hierarchy;
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

}
