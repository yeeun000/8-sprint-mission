package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDTO.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateRequest userStatusDTO) {
    User user = userRepository.findById(userStatusDTO.userId())
        .orElseThrow(() -> new UserNotFoundException(userStatusDTO.userId()));

    if (userStatusRepository.findByUserId(userStatusDTO.userId()).isPresent()) {
      throw new UserAlreadyExistsException(user.getId());
    }

    Instant lastActiveAt = userStatusDTO.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto find(UUID id) {
    return userStatusRepository.findById(id)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> new UserStatusNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID id, UserStatusCreateRequest userStateDTO) {
    Instant newLastActiveAt = userStateDTO.lastActiveAt();
    UserStatus status = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(id));
    status.update(newLastActiveAt);
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(userId));
    status.update(newLastActiveAt);
    return userStatusMapper.toDto(status);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!userStatusRepository.existsById(id)) {
      throw new UserStatusNotFoundException(id);
    }
    userStatusRepository.deleteById(id);
  }

}
