package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDTO.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStateDTO userStatusDTO);

  UserStatus find(UUID id);

  void findAll();

  UserStatus update(UUID id, UserStateDTO userStateDTO);

  UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest request);

  void delete(UUID id);

}
