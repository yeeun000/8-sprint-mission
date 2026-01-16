package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDTO.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest userStatusDTO) {
    User user = userRepository.findById(userStatusDTO.userId())
        .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다."));
    if (userStatusRepository.findByUserId(userStatusDTO.userId()).isPresent()) {
      throw new IllegalArgumentException(" 이미 유저가 있습니다.");
    }

    Instant lastActiveAt = Instant.now();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto find(UUID id) {
    return userStatusMapper.toDto(userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" UserStatus를 찾을 수 없습니다.")));
  }

  @Override
  public void findAll() {
    userStatusRepository.findAll();
  }

  @Override
  public UserStatusDto update(UUID id, UserStatusCreateRequest userStateDTO) {
    Instant newLastActiveAt = userStateDTO.lastActiveAt();
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" UserStatus를 찾을 수 없습니다."));
    status.update(newLastActiveAt);
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException(" UserStatus를 찾을 수 없습니다."));
    status.update(newLastActiveAt);
    return userStatusMapper.toDto(userStatusRepository.save(status));
  }

  @Override
  public void delete(UUID id) {
    find(id);
    userStatusRepository.deleteById(id);
  }

}
