package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record ChannelDTO(
        Channel.ChannelType type,
        String name,
        String description,
        List<UUID> users
) {
}
