package com.sprint.mission.discodeit.dto.channelDTO;

import java.util.List;
import java.util.UUID;

public record PrivateChannelDTO(
        List<UUID> participantIds
) {
}
