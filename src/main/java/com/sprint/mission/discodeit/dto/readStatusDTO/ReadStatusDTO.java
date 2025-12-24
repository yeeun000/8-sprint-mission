package com.sprint.mission.discodeit.dto.readStatusDTO;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDTO(
        UUID channelId,
        UUID userId,
        Instant lastRead
) {
}
