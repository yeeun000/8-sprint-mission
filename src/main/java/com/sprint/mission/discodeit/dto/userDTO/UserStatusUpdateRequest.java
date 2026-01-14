package com.sprint.mission.discodeit.dto.userDTO;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

}
