package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindChannelDTO;
import com.sprint.mission.discodeit.dto.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.PublicChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createPublic(PublicChannelDTO dto);

    Channel createPrivate(PrivateChannelDTO dto);

    List<FindChannelDTO> findAllByUserId(UUID userId);

    void delete(UUID channelId);

    Channel update(PublicChannelDTO publicChannelDTO);

    FindChannelDTO findId(UUID channelId);
}

