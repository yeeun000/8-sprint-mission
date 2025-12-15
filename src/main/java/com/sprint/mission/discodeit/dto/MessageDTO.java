package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.UUID;

public record MessageDTO(
        UUID id,
        UUID userId,
        UUID channelId,
        String content,
        List<BinaryContentDTO> files
) {
}
