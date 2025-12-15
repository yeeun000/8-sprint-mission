package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.UUID;

public record PublicChannelDTO(
        UUID id,
        String name,
        String description,
        Channel.ChannelType PUBLIC
        ) {
}
