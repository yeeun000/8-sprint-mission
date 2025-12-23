package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User create(CreateUserDTO userCreateDTO, BinaryContentDTO binaryContentDTO);

    List<UserDTO> findAll();

    void delete(UUID id);

    User update(UpdateUserDTO updateUserDTO, BinaryContentDTO binaryContentDTO);

    UserDTO findId(UUID id);
}
