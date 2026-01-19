package com.sprint.mission.discodeit.dto.messageDTO;

import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import java.time.Instant;
import java.util.UUID;


public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId
) {

}