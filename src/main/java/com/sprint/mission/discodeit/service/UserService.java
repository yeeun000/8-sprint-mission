package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    void create();

    User login();

    void delete(UUID id);

    User readID(UUID id);

    User readName(String name);

    User readNickname(String nickname);

    User readEmail(String email);

    void read(User user);

    void update(User user);
    User create(String name, String email, String nickname);
}
