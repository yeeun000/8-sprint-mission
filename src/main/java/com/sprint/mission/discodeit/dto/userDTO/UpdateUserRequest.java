package com.sprint.mission.discodeit.dto.userDTO;

import java.util.UUID;

public record UpdateUserRequest(
    String name,
    String email,
    String password
) {

}
