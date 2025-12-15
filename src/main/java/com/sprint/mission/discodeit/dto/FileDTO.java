package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record FileDTO(
        UUID id,
        String fileName,
        String filePath
) {
}
