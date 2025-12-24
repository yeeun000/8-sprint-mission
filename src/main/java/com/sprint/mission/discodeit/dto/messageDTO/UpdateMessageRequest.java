package com.sprint.mission.discodeit.dto.messageDTO;

import java.util.UUID;

public record UpdateMessageRequest(
        UUID id,
        String newContent
) {
}
