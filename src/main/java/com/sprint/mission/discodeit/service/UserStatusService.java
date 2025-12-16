package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDTO.UserStateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {
    void create(UserStateDTO userStatusDTO);

    UserStatus find(UUID id);

    void findAll();

    void update(UserStateDTO userStateDTO);

    void updateByUserId(UUID userId);

    void delete(UUID id);

}
