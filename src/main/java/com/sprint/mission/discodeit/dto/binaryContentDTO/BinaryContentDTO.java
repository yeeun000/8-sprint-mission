package com.sprint.mission.discodeit.dto.binaryContentDTO;

public record BinaryContentDTO(
        String fileName,
        String type,
        byte[] bytes
) {
}
