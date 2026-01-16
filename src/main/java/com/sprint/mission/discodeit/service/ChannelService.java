package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest dto);

  ChannelDto create(PrivateChannelCreateRequest dto);

  List<ChannelDto> findAllByUserId(UUID userId);

  void delete(UUID id);

  ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest);

  ChannelDto find(UUID id);
}

