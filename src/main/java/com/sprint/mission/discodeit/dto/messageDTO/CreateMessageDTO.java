package com.sprint.mission.discodeit.dto.messageDTO;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;

import java.util.List;
import java.util.UUID;

public record CreateMessageDTO(
        UUID userId,
        UUID channelId,
        String content,
        List<BinaryContentDTO> files
) {
}
