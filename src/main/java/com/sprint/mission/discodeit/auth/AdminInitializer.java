package com.sprint.mission.discodeit.auth;

import static com.sprint.mission.discodeit.entity.Role.ADMIN;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

  @Value("${discodeit.admin.username}")
  private String username;
  @Value("${discodeit.admin.email}")
  private String email;
  @Value("${discodeit.admin.password}")
  private String password;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void run(String... args) throws Exception {
    if (!userRepository.existsByRole(ADMIN)) {
      User admin = new User(username, email, passwordEncoder.encode(password), null);
      admin.setRole(ADMIN);
      userRepository.save(admin);
    }
  }
}
