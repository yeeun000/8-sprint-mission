package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelMapper(MessageRepository messageRepository,
      ReadStatusRepository readStatusRepository, UserMapper userMapper) {
    this.messageRepository = messageRepository;
    this.readStatusRepository = readStatusRepository;
    this.userMapper = userMapper;
  }

  public ChannelDto toDto(Channel channel) {

    Instant lastMessageAt = messageRepository.findLastMessageTime(channel.getId())
        .orElse(null);

    List<UserDto> participants = readStatusRepository.findAllByChannelId(channel.getId())
        .stream()
        .map(readStatus -> userMapper.toDto(readStatus.getUser()))
        .toList();

    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), participants, lastMessageAt);

  }
}
