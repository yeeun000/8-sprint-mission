package com.sprint.mission.discodeit.dto.messageDTO;

import java.util.UUID;

public record CreateMessageDTO(
        String content,
        UUID channelId,
        UUID userId
) {
}
