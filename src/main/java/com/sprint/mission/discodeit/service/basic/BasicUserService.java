package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUserNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest createUserRequest,
      BinaryContentCreateRequest binaryContentDTO) {

    log.debug("유저 생성 요청 - name: {}, email: {}",
        createUserRequest.username(),
        createUserRequest.email());

    if (userRepository.existsByUsername(createUserRequest.username())) {
      log.warn("이미 있는 이름 - username: {}", createUserRequest.username());
      throw new DuplicateUserNameException(createUserRequest.username());
    }
    if (userRepository.existsByEmail(createUserRequest.email())) {
      log.warn("이미 있는 이메일 - email: {}", createUserRequest.email());
      throw new DuplicateEmailException(createUserRequest.email());
    }

    BinaryContent profile = null;
    if (binaryContentDTO != null) {
      profile = new BinaryContent(
          binaryContentDTO.fileName(),
          (long) binaryContentDTO.bytes().length,
          binaryContentDTO.contentType()
      );
      profile = binaryContentRepository.save(profile);
      binaryContentStorage.put(profile.getId(), binaryContentDTO.bytes());

    }
    User user = User.createProfile(
        createUserRequest.username(),
        createUserRequest.email(),
        createUserRequest.password(),
        profile
    );
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);
    userRepository.save(user);

    log.info("유저 생성 완료 - id: {}", user.getId());

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {

    log.debug("유저 삭제 요청 - id: {}", id);

    if (!userRepository.existsById(id)) {
      log.warn("유저 삭제 실패 - id: {}", id);
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);

    log.info("유저 삭제 완료 - id: {}", id);
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest updateUserRequest,
      BinaryContentCreateRequest binaryContentDTO) {

    log.debug("유저 수정 요청 - name: {}, email: {}",
        updateUserRequest.newUsername(),
        updateUserRequest.newEmail());

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("유저 찾기 실패 - id: {}", userId);
          return new UserNotFoundException(userId);
        });

    String newName = updateUserRequest.newUsername() != null
        ? updateUserRequest.newUsername()
        : user.getUsername();
    String newEmail = updateUserRequest.newEmail() != null
        ? updateUserRequest.newEmail()
        : user.getEmail();
    String newPassword = updateUserRequest.newPassword() != null
        ? updateUserRequest.newPassword()
        : user.getPassword();

    if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
      log.warn("이미 있는 이메일 - email: {}", newEmail);
      throw new DuplicateEmailException(newEmail);
    }
    if (!newName.equals(user.getUsername()) && userRepository.existsByUsername(newName)) {
      log.warn("이미 있는 이름 - username: {}", newName);
      throw new DuplicateUserNameException(newName);
    }

    BinaryContent profile = user.getProfile();
    BinaryContent oldProfile = user.getProfile();
    if (binaryContentDTO != null) {
      BinaryContent content = new BinaryContent(
          binaryContentDTO.fileName(),
          (long) binaryContentDTO.bytes().length,
          binaryContentDTO.contentType()
      );

      profile = binaryContentRepository.save(content);
      binaryContentStorage.put(content.getId(), binaryContentDTO.bytes());
      if (oldProfile != null) {
        binaryContentRepository.deleteById(oldProfile.getId());
      }
    }

    user.update(newName, newEmail, newPassword, profile);

    log.info("유저 수정 완료 - id: {}", user.getId());

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID id) {
    return userRepository.findById(id)
        .map(userMapper::toDto)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

}
