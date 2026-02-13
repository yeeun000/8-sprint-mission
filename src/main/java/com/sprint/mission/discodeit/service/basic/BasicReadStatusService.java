package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest readStatusRequest) {
    User user = userRepository.findById(readStatusRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(readStatusRequest.userId()));
    Channel channel = channelRepository.findById(readStatusRequest.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(readStatusRequest.channelId()));

    ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(user.getId(),
            channel.getId())
        .orElseGet(() -> {
          Instant lastReadAt = readStatusRequest.lastReadAt();
          return readStatusRepository.save(new ReadStatus(user, channel, lastReadAt));
        });

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID id) {
    return readStatusRepository.findById(id)
        .map(readStatusMapper::toDto)
        .orElseThrow(() -> new ReadStatusNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId)
        .stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest updateReadStatusRequest) {
    Instant lastReadAt = updateReadStatusRequest.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    readStatus.update(lastReadAt);
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!readStatusRepository.existsById(id)) {
      throw new ReadStatusNotFoundException(id);
    }
    readStatusRepository.deleteById(id);
  }
}
