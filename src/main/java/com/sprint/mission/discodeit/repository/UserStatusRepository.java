package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusRepository {
    boolean onlineStatus(UUID id);

    void add(UserStatus status);

    UserStatus find(UUID id);
    List<UserStatus> findAll();

    void remove(UUID userId);
}
