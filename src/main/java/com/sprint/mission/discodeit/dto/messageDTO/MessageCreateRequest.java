package com.sprint.mission.discodeit.dto.messageDTO;

import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
