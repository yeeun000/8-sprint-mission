package com.sprint.mission.discodeit.dto.binaryContentDTO;

import java.util.UUID;

public record ProfileDTO(
        UUID id,
        String fileName,
        String filePath
) {
}
