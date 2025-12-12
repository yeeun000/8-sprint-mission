package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository {
    boolean onlineStatus(UUID id);
    void add(UserStatus status);
    void remove(UUID userId);
}
