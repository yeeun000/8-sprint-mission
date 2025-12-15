package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record BinaryContentDTO(
        UUID id,
        UUID channelId,
        UUID messageId,
        String fileName,
        String filePath
) {
}
