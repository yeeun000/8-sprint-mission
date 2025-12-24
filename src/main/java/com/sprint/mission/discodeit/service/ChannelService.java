package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelDTO dto);

    Channel create(PrivateChannelDTO dto);

    List<ChannelDTO> findAllByUserId(UUID userId);

    void delete(UUID id);

    Channel update(UUID id, PublicChannelDTO publicChannelDTO);

    ChannelDTO find(UUID id);
}

