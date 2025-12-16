package com.sprint.mission.discodeit.dto.channelDTO;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelDTO(
        Channel.ChannelType type,
        List<UUID> users
) {
}
