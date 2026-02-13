package com.sprint.mission.discodeit.dto.readStatusDTO;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(

    @NotNull(message = "유저 아이디는 필수입니다.")
    UUID userId,

    @NotNull(message = "채널 아이디는 필수입니다.")
    UUID channelId,

    @NotNull(message = "마지막 읽은 시간은 필수입니다.")
    Instant lastReadAt
) {

}
