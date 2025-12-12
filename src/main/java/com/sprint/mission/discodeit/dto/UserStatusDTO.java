package com.sprint.mission.discodeit.dto;

import java.util.Optional;
import java.util.UUID;

public record UserStatusDTO(
        UUID id,
        String name,
        boolean online) {
}
