package com.sprint.mission.discodeit.dto.userDTO;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(

    @NotNull(message = "유저 아이디는 필수입니다.")
    UUID userId,
    Instant lastActiveAt
) {

}
