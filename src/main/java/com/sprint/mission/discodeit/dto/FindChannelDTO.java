package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record FindChannelDTO(
        UUID channelId,
        Channel.ChannelType type,
        Instant lastRead,
        List<UUID> userId
) {
}
