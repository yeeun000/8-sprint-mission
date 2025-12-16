package com.sprint.mission.discodeit.dto.channelDTO;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public record UpdateChannelDTO(
        UUID id,
        Channel.ChannelType type,
        String name,
        String description
) {
}
