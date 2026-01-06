package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel create(PublicChannelCreateRequest dto);

  Channel create(PrivateChannelCreateRequest dto);

  List<ChannelDto> findAllByUserId(UUID userId);

  void delete(UUID id);

  Channel update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);

  ChannelDto find(UUID id);
}

