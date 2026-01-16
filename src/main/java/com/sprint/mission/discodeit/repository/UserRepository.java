package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

  User save(User user);

  List<User> findAll();

  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  void deleteById(UUID id);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

}
