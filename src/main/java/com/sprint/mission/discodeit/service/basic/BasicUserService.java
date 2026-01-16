package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public User create(UserCreateRequest createUserRequest) {
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

    return user;
  }

  @Override
  public User create(UserCreateRequest createUserRequest,
      BinaryContentCreateRequest binaryContentDTO) {
    if (userRepository.existsByUsername(createUserRequest.username())) {
      throw new IllegalArgumentException(createUserRequest.username());
    }
    if (userRepository.existsByEmail(createUserRequest.email())) {
      throw new IllegalArgumentException(createUserRequest.email());
    }
    BinaryContent profile = null;
    if (binaryContentDTO != null) {
      String fileName = binaryContentDTO.fileName();
      String contentType = binaryContentDTO.contentType();
      byte[] bytes = binaryContentDTO.bytes();
      profile = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
      profile = binaryContentRepository.save(profile);
    }

    User user = User.createProfile(createUserRequest.username(), createUserRequest.email(),
        createUserRequest.password(), profile);
    userRepository.save(user);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);
    userStatusRepository.save(userStatus);

    return user;
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(this::toDto).toList();
  }

  @Override
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
  public User update(UUID userId, UserUpdateRequest updateUserRequest,
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
          binaryContentDTO.contentType(),
          binaryContentDTO.bytes()
      );
      profile = binaryContentRepository.save(content);
      if (oldProfile != null) {
        binaryContentRepository.deleteById(oldProfile.getId());
      }
    }

    user.update(newName, newEmail, newPassword, profile);
    return userRepository.save(user);
  }


  @Override
  public UserDto findId(UUID id) {
    return userRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다. "));

  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile().getId(),
        online
    );
  }
}
