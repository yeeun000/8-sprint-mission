package com.sprint.mission.discodeit.dto.messageDTO;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(

    String content,

    @NotNull(message = "채널 아이디는 필수입니다.")
    UUID channelId,
    UUID authorId
) {

}
