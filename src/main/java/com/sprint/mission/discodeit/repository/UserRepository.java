package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserRepository {

    void add(User user);

    List<User> findAll();

    User findId(UUID userId);

    void remove(UUID userId);

    boolean existsName(String name);

    boolean existsEmail(String email);

    User findName(String name);
}
