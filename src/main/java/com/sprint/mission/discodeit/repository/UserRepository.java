package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    void deleteById(UUID id);

    boolean existsName(String name);

    boolean existsEmail(String email);

}
