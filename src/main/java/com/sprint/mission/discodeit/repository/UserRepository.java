package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

  User save(User user);

  @EntityGraph(attributePaths = {"status", "profile"})
  List<User> findAll();

  @EntityGraph(attributePaths = {"status", "profile"})
  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  void deleteById(UUID id);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

}
