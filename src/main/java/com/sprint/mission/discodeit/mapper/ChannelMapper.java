package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper {

  @Mapping(target= "participants",source="participants")
  @Mapping(target = "lastMessageAt",source = "lastMessageAt")
  ChannelDto toDto(Channel channel, List<UserDto> participants, Instant lastMessageAt);


}
