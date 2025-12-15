package com.sprint.mission.discodeit.dto;

import java.util.Optional;
import java.util.UUID;

public record UserStatusDTO(
        UUID userId,
        boolean online) {
}
