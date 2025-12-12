package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User create(UserDTO userCreateDTO);
    List<UserStatusDTO> findAll();
    void delete(UUID userId);
    User update(UserDTO userDTO);
    UserStatusDTO findId(UUID userId);
}
