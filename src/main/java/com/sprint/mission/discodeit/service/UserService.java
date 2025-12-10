package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(String username, String nickName, String email);
    List<User> findAll();
    void delete(UUID userId);
    User update(UUID userId, String username, String nickName, String email);
    User findId(UUID userId);
}
