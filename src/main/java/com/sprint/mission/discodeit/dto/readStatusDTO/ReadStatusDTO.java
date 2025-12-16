package com.sprint.mission.discodeit.dto.readStatusDTO;

import java.util.UUID;

public record ReadStatusDTO(
        UUID channelId,
        UUID userId
) {
}
