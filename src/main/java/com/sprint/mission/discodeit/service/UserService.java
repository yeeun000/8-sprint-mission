package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(CreateUserRequest createUserRequest, BinaryContentDTO binaryContentDTO);

    User create(CreateUserRequest createUserRequest);

    List<UserDTO> findAll();

    void delete(UUID id);

    User update(UpdateUserRequest updateUserRequest, BinaryContentDTO binaryContentDTO);

    UserDTO findId(UUID id);

}
