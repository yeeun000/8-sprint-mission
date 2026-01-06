package com.sprint.mission.discodeit.dto.readStatusDTO;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
