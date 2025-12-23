package com.sprint.mission.discodeit.dto.channelDTO;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDTO(
        UUID channelId,
        Channel.ChannelType type,
        String name,
        String description,
        List<UUID> userId,
        Instant lastRead
) {
}
