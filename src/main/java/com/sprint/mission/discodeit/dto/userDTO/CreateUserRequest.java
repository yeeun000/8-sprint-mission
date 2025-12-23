package com.sprint.mission.discodeit.dto.userDTO;

public record CreateUserRequest(
        String name,
        String email,
        String password
) {
}
