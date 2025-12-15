package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {
    void create(UserStatusDTO userStatusDTO);
    UserStatus find(UUID id);
    void findAll();
    void update(UserStatusUpdateDTO userStatusUpdateDTO);
    void updateByUserId(UUID userId);
    void delete(UUID id);

}
