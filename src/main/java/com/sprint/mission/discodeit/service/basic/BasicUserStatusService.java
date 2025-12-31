package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDTO.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserStateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
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

  @Override
  public UserStatus create(UserStateDTO userStatusDTO) {
    userRepository.findById(userStatusDTO.userId())
        .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다."));
    if (userStatusRepository.findByUserId(userStatusDTO.userId()).isPresent()) {
      throw new IllegalArgumentException(" 이미 유저가 있습니다.");
    }

    Instant lastActiveAt = Instant.now();
    UserStatus userStatus = new UserStatus(userStatusDTO.userId(), lastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" UserStatus를 찾을 수 없습니다."));
  }

  @Override
  public void findAll() {
    userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UUID id, UserStateDTO userStateDTO) {
    Instant newLastActiveAt = userStateDTO.lastActiveAt();
    UserStatus status = find(id);
    status.update(newLastActiveAt);
    return userStatusRepository.save(status);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest request) {
    Instant newLastActiveAt = request.lastActiveAt();
    UserStatus status = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException(" UserStatus를 찾을 수 없습니다."));
    status.update(newLastActiveAt);
    return userStatusRepository.save(status);
  }

  @Override
  public void delete(UUID id) {
    find(id);
    userStatusRepository.deleteById(id);
  }

}
