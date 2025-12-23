package com.sprint.mission.discodeit.dto.messageDTO;

import java.util.UUID;

public record UpdateMessageDTO(
        UUID id,
        String newContent
) {
}
