package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserMapper(BinaryContentMapper binaryContentMapper) {
    this.binaryContentMapper = binaryContentMapper;
  }

  public UserDto toDto(User user) {

    Boolean online = false;
    if (user.getStatus() != null) {
      online = user.getStatus().isOnline();
    }

    return new UserDto(user.getId(), user.getUsername(), user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()), online);
  }

}
