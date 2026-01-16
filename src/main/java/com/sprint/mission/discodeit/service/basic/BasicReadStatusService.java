package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;


  @Override
  public ReadStatusDto create(ReadStatusCreateRequest readStatusRequest) {
    User user = userRepository.findById(readStatusRequest.userId())
        .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다."));
    Channel channel = channelRepository.findById(readStatusRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."));

    if (readStatusRepository.findAllByUserId(user.getId()).stream()
        .anyMatch(readStatus -> readStatus.getId().equals(channel.getId()))) {
      throw new IllegalArgumentException("이미 있습니다.");
    }

    Instant lastReadAt = readStatusRequest.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    return readStatusMapper.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public ReadStatusDto find(UUID id) {
    return readStatusMapper.toDto(readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" readStatus를 찾을 수 없습니다.")));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId)
        .stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest updateReadStatusRequest) {
    Instant lastReadAt = updateReadStatusRequest.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(" readStatus를 찾을 수 없습니다."));
    readStatus.update(lastReadAt);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public void delete(UUID id) {
    find(id);
    readStatusRepository.deleteById(id);
  }
}
