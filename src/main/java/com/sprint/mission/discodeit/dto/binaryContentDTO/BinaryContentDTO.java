package com.sprint.mission.discodeit.dto.binaryContentDTO;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        UUID userId,
        UUID channelId,
        UUID messageId,
        String fileName,
        String filePath
) {
}
