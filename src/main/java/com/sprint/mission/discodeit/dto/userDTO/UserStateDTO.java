package com.sprint.mission.discodeit.dto.userDTO;

import java.time.Instant;
import java.util.UUID;

public record UserStateDTO(
        UUID userId,
        Instant lastActiveAt
) {
}
