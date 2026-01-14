package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDTO.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreateRequest userStatusDTO);

  UserStatus find(UUID id);

  void findAll();

  UserStatus update(UUID id, UserStatusCreateRequest userStateDTO);

  UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID id);

}
