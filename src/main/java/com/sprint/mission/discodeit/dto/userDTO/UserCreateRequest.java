package com.sprint.mission.discodeit.dto.userDTO;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}
