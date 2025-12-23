package com.sprint.mission.discodeit.dto.userDTO;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
}
