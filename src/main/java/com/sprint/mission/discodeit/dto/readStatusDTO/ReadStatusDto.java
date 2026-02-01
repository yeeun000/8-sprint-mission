package com.sprint.mission.discodeit.dto.readStatusDTO;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
