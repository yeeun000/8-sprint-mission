package com.sprint.mission.discodeit.auth;

import static com.sprint.mission.discodeit.entity.Role.ADMIN;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void run(String... args) throws Exception {
    if (!userRepository.existsByEmail("admin@admin.com")) {
      User admin = new User("admin", "admin@admin.com", passwordEncoder.encode("admin"), null);
      admin.setRole(ADMIN);
      UserStatus userStatus = new UserStatus(admin, Instant.now());
      admin.setStatus(userStatus);
      userRepository.save(admin);
    }
  }
}
