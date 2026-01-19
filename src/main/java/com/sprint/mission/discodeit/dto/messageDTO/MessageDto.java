package com.sprint.mission.discodeit.dto.messageDTO;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {

}