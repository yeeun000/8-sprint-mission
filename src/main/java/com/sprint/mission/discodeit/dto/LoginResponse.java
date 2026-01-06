package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record LoginResponse(
    UUID userId,
    String name
) {

}
