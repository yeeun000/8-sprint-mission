package com.sprint.mission.discodeit.dto.messageDTO;

import java.util.UUID;

public record CreateMessageRequest(
        String content,
        UUID channelId,
        UUID userId
) {
}
