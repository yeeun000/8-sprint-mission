package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest createUserRequest, BinaryContentCreateRequest binaryContentDTO);

  UserDto create(UserCreateRequest createUserRequest);

  List<UserDto> findAll();

  void delete(UUID id);

  UserDto update(UUID userId, UserUpdateRequest updateUserRequest,
      BinaryContentCreateRequest binaryContentDTO);

  UserDto findId(UUID id);

}
