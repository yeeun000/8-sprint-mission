package com.sprint.mission.discodeit.service.basic;


import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public Channel create(PublicChannelCreateRequest publicChannelDTO) {
    Channel channel = Channel.createPublicChannel(PUBLIC, publicChannelDTO.name(),
        publicChannelDTO.description());
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public Channel create(PrivateChannelCreateRequest privateChannelDTO) {
    Channel channel = Channel.createPrivateChannel(PRIVATE);
    channelRepository.save(channel);

    privateChannelDTO.participantIds().stream()
        .map(userId -> new ReadStatus(userId, channel.getId(), Instant.now()))
        .forEach(readStatusRepository::save);
    return channel;
  }


  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> userIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(Channel.ChannelType.PUBLIC) || userIds.contains(
                channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));

    messageRepository.deleteAllByChannelId(id);
    readStatusRepository.deleteAllByChannelId(id);
    channelRepository.deleteById(id);
  }

  @Override
  public Channel update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));

    if (channel.getType().equals(Channel.ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private 채널은 변경할 수 없습니다.");
    }
    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public ChannelDto find(UUID id) {
    return channelRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));
  }

  public ChannelDto toDto(Channel channel) {

    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(Channel.ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getChannelName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}