package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = {
        BinaryContentMapper.class,
        UserMapper.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {

  @Mapping(source = "message.id", target = "id")
  @Mapping(source = "message.createdAt", target = "createdAt")
  @Mapping(source = "message.updatedAt", target = "updatedAt")
  @Mapping(source = "message.content", target = "content")
  @Mapping(source = "message.channel.id", target = "channelId")
  MessageDto toDto(Message message, UserDto author, List<BinaryContentDto> attachments);

}
