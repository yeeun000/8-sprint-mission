package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public User create(CreateUserDTO userDTO, Optional<BinaryContentDTO> binaryContentDTO) {
        if (userRepository.existsName(userDTO.name())) {
            throw new IllegalArgumentException(userDTO.name() + "이 이미 있습니다.");
        }
        if (userRepository.existsEmail(userDTO.email())) {
            throw new IllegalArgumentException(userDTO.email() + "이 이미 있습니다.");
        }

        UUID porfileID = binaryContentDTO
                .map(profile -> {
                    String fileName = profile.fileName();
                    String contentType = profile.type();
                    byte[] bytes = profile.bytes();
                    BinaryContent content = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(content).getId();
                }).orElse(null);


        User user = new User(userDTO.name(), userDTO.email(), userDTO.password(),porfileID);
        userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto).toList();
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);

        userStatusRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @Override
    public User update(UpdateUserDTO updateUserDTO, Optional<BinaryContentDTO> binaryContentDTO) {
        User user = userRepository.findById(updateUserDTO.id())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        String newName = updateUserDTO.name();
        String newPassword = updateUserDTO.password();
        String newEmail = updateUserDTO.email();

        if (userRepository.existsEmail(newEmail)) {
            throw new IllegalArgumentException("이미 있는 이메일입니다.");
        }
        if (userRepository.existsName(newName)) {
            throw new IllegalArgumentException("이미 있는 유저 이름입니다.");
        }

        UUID porfileID = binaryContentDTO
                .map(profile -> {
                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(binaryContentRepository::deleteById);
                    String fileName = profile.fileName();
                    String contentType = profile.type();
                    byte[] bytes = profile.bytes();
                    BinaryContent content = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(content).getId();
                }).orElse(null);

        user.update(newName, newEmail,  newPassword, porfileID);
        userRepository.save(user);
        return user;

    }

    @Override
    public UserDTO findId(UUID id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다. "));

    }

    private UserDTO toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(null);

        return new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }

}
