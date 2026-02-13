package com.sprint.mission.discodeit.dto.channelDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(

    @NotNull(message = "참가자가 비어 있으면 안됩니다.")
    @Size(min = 1)
    List<UUID> participantIds
) {

}
