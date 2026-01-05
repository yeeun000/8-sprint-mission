package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

  User create(UserCreateRequest createUserRequest, BinaryContentDTO binaryContentDTO);

  User create(UserCreateRequest createUserRequest);

  List<UserDto> findAll();

  void delete(UUID id);

  User update(UUID userId, UserUpdateRequest updateUserRequest, BinaryContentDTO binaryContentDTO);

  UserDto findId(UUID id);

}
