package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDTO.CreateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.FindUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(CreateUserDTO userCreateDTO);

    List<FindUserDTO> findAll();

    void delete(UUID userId);

    User update(UpdateUserDTO updateUserDTO);

    FindUserDTO findId(UUID userId);
}
