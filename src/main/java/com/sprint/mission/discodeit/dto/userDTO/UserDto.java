package com.sprint.mission.discodeit.dto.userDTO;

import java.util.UUID;


public record UserDto(
    UUID id,
    String username,
    String email,
    boolean online,
    String profile
) {

}
