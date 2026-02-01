package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChannelMapper {

  @Mapping(source = "channel.id", target = "id")
  @Mapping(source = "channel.type", target = "type")
  @Mapping(source = "channel.name", target = "name")
  @Mapping(source = "channel.description", target = "description")
  ChannelDto toDto(
      Channel channel,
      List<UserDto> participants,
      Instant lastMessageAt
  );
}
