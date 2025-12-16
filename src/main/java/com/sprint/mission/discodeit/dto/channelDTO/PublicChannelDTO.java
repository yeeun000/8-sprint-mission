package com.sprint.mission.discodeit.dto.channelDTO;

import com.sprint.mission.discodeit.entity.Channel;

public record PublicChannelDTO(
        Channel.ChannelType type,
        String name,
        String description
) {
}
