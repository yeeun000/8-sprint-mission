package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDTO.FindChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.UpdateChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelDTO dto);

    Channel create(PrivateChannelDTO dto);

    List<FindChannelDTO> findAllByUserId(UUID userId);

    void delete(UUID channelId);

    Channel update(UpdateChannelDTO updateChannelDTO);

    FindChannelDTO findId(UUID channelId);
}

