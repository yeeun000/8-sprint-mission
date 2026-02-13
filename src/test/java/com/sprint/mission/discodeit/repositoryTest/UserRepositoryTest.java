package com.sprint.mission.discodeit.repositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("유저 조회할 때 프로필도 같이 조회한다.")
  void findAllProfile_success() {
    User user = new User("username", "test", "password", null);
    em.persist(user);

    UserStatus status = new UserStatus(user, Instant.now());
    em.persist(status);

    em.flush();
    em.clear();

    List<User> users = userRepository.findAllWithProfileAndStatus();

    assertThat(users).hasSize(1);
    assertThat(users.get(0).getStatus()).isNotNull();
  }

  @Test
  @DisplayName("없는 이메일을 조회했을 경우 false를 반환한다.")
  void findByEmail_false() {
    boolean exists = userRepository.existsByEmail("test");

    assertThat(exists).isFalse();
  }

}
