package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    log.debug("public 채널 생성 요청 - channelName: {}, channelDescription: {}",
        publicChannelDTO.name(),
        publicChannelDTO.description());

    Channel channel = Channel.createPublicChannel(publicChannelDTO.name(),
        publicChannelDTO.description());
    channelRepository.save(channel);

    log.info("채널 생성 완료 - id: {}", channel.getId());

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest privateChannelDTO) {

    log.debug("private 채널 생성 요청 - participantIds: {}",
        privateChannelDTO.participantIds());

    Channel channel = Channel.createPrivateChannel();
    channelRepository.save(channel);

    privateChannelDTO.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> {
                log.warn("유저 찾기 실패 - userId: {}", userId);
                return new UserNotFoundException(userId);
              });
          return new ReadStatus(user, channel, Instant.now());
        })
        .forEach(readStatusRepository::save);

    log.info("채널 생성 완료 - id: {}", channel.getId());

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> userIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus -> ReadStatus.getChannel().getId())
        .toList();
    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, userIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {

    log.debug("채널 삭제 요청 - id: {}", id);

    if (!channelRepository.existsById(id)) {
      log.warn("채널 삭제 실패 - id: {}", id);
      throw new ChannelNotFoundException(id);
    }
    messageRepository.deleteAllByChannelId(id);
    readStatusRepository.deleteAllByChannelId(id);
    channelRepository.deleteById(id);

    log.info("채널 삭제 완료 - id: {}", id);

  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, PublicChannelUpdateRequest publicChannelUpdateRequest) {

    log.debug("채널 수정 요청 - id: {}, channelName: {}, channelDescription: {}",
        id,
        publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());

    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("채널 찾기 실패 - id: {}", id);
          return new ChannelNotFoundException(id);
        });

    if (channel.getType().equals(Channel.ChannelType.PRIVATE)) {
      log.warn("private 채널 수정 불가 - id: {}", id);
      throw new PrivateChannelUpdateException(id);
    }
    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());

    log.info("채널 수정 완료 - id: {}", id);

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID id) {
    return channelRepository.findById(id)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new ChannelNotFoundException(id));
  }
}