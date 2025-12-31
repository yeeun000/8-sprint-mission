package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

  User create(CreateUserRequest createUserRequest, BinaryContentDTO binaryContentDTO);

  User create(CreateUserRequest createUserRequest);

  List<UserDto> findAll();

  void delete(UUID id);

  User update(UUID userId, UpdateUserRequest updateUserRequest, BinaryContentDTO binaryContentDTO);

  UserDto findId(UUID id);

}
