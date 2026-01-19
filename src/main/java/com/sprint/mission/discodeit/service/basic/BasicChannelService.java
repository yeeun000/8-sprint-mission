package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Override
  @Transactional
  public ChannelDto create(PublicChannelCreateRequest publicChannelDTO) {
    Channel channel = Channel.createPublicChannel(publicChannelDTO.name(),
        publicChannelDTO.description());
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest privateChannelDTO) {
    Channel channel = Channel.createPrivateChannel();
    channelRepository.save(channel);

    privateChannelDTO.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new NoSuchElementException("User를 찾을 수 없습니다."));
          return new ReadStatus(user, channel, Instant.now());
        })
        .forEach(readStatusRepository::save);
    return channelMapper.toDto(channel);
  }


  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> userIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus -> ReadStatus.getChannel().getId())
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(Channel.ChannelType.PUBLIC) || userIds.contains(
                channel.getId())
        )
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));

    messageRepository.deleteAllByChannelId(id);
    readStatusRepository.deleteAllByChannelId(id);
    channelRepository.deleteById(id);
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));

    if (channel.getType().equals(Channel.ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private 채널은 변경할 수 없습니다.");
    }
    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID id) {
    return channelRepository.findById(id)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));
  }

}