package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDTO.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest userStatusDTO);

  UserStatusDto find(UUID id);

  void findAll();

  UserStatusDto update(UUID id, UserStatusCreateRequest userStateDTO);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID id);

}
