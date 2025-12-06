package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserRepository {
    void addUser(User user);
    void removeUser(User user);
    List<User> findAll();
    void updateName(User user, String name);
    void updateNickname(User user, String nickname);
    void updateEmail(User user, String email);

    User readId(UUID id);
}
