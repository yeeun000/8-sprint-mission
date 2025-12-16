package com.sprint.mission.discodeit.dto.userDTO;

import java.util.UUID;

public record FindUserDTO(
        UUID id,
        String name,
        boolean online) {
}
