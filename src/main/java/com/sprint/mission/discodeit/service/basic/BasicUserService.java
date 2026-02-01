package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest createUserRequest) {
    if (userRepository.existsByUsername(createUserRequest.username())) {
      throw new IllegalArgumentException(createUserRequest.username());
    }
    if (userRepository.existsByEmail(createUserRequest.email())) {
      throw new IllegalArgumentException(createUserRequest.email());
    }

    User user = User.create(createUserRequest.username(), createUserRequest.email(),
        createUserRequest.password());
    userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);
    userStatusRepository.save(userStatus);

    return toUserDto(user);
  }

  @Override
  @Transactional
  public UserDto create(UserCreateRequest createUserRequest,
      BinaryContentCreateRequest binaryContentDTO) {
    if (userRepository.existsByUsername(createUserRequest.username())) {
      throw new IllegalArgumentException(createUserRequest.username());
    }
    if (userRepository.existsByEmail(createUserRequest.email())) {
      throw new IllegalArgumentException(createUserRequest.email());
    }

    BinaryContent profile = null;
    if (binaryContentDTO != null) {
      profile = new BinaryContent(
          binaryContentDTO.fileName(),
          (long) binaryContentDTO.bytes().length,
          binaryContentDTO.contentType()
      );
      binaryContentStorage.put(profile.getId(), binaryContentDTO.bytes());
      profile = binaryContentRepository.save(profile);

    }
    User user = User.createProfile(
        createUserRequest.username(),
        createUserRequest.email(),
        createUserRequest.password(),
        profile
    );

    User savedUser = userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(savedUser, now);
    userStatusRepository.save(userStatus);
    userRepository.flush();
    return toUserDto(savedUser);
  }


  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(this::toUserDto).toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    Optional.ofNullable(user.getProfile())
        .map(BinaryContent::getId)
        .ifPresent(binaryContentRepository::deleteById);

    userStatusRepository.deleteByUserId(id);
    userRepository.deleteById(id);
  }


  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest updateUserRequest,
      BinaryContentCreateRequest binaryContentDTO) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

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
      throw new IllegalArgumentException("이미 있는 이메일입니다.");
    }
    if (!newName.equals(user.getUsername()) && userRepository.existsByUsername(newName)) {
      throw new IllegalArgumentException("이미 있는 유저 이름입니다.");
    }

    BinaryContent profile = user.getProfile();
    BinaryContent oldProfile = user.getProfile();
    if (binaryContentDTO != null) {
      BinaryContent content = new BinaryContent(
          binaryContentDTO.fileName(),
          (long) binaryContentDTO.bytes().length,
          binaryContentDTO.contentType()
      );

      binaryContentStorage.put(content.getId(), binaryContentDTO.bytes());
      profile = binaryContentRepository.save(content);
      if (oldProfile != null) {
        binaryContentRepository.deleteById(oldProfile.getId());
      }
    }

    user.update(newName, newEmail, newPassword, profile);
    return toUserDto(userRepository.save(user));
  }


  @Override
  @Transactional(readOnly = true)
  public UserDto findId(UUID id) {
    return userRepository.findById(id)
        .map(this::toUserDto)
        .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다. "));
  }

  public UserDto toUserDto(User user) {

    Boolean online = false;
    if (user.getStatus() != null) {
      online = user.getStatus().isOnline();
    }

    BinaryContentDto profile = null;
    if (user.getProfile() != null) {
      profile = binaryContentMapper.toDto(user.getProfile());
    }

    return userMapper.toDto(
        user,
        profile,
        online
    );
  }

}
