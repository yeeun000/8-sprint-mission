package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
@Tag(name = "login", description = "login API")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);
    return ResponseEntity.ok(user);
  }
}
