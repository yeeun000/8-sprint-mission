package com.sprint.mission.discodeit.dto.readStatusDTO;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

}
