package com.sprint.mission.discodeit.dto.binaryContentDTO;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
