package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDTO(
        UUID id,
        Instant lastRead
) {
}
