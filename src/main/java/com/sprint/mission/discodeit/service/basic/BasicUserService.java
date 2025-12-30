package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.EmailRegistException;
import com.sprint.mission.discodeit.controller.MemberRegistException;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
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
    public User create(CreateUserRequest createUserRequest) {
        if (userRepository.existsName(createUserRequest.name())) {
            throw new MemberRegistException(createUserRequest.name());
        }
        if (userRepository.existsEmail(createUserRequest.email())) {
            throw new EmailRegistException(createUserRequest.email());
        }
        User user = User.create(createUserRequest.name(), createUserRequest.email(), createUserRequest.password());
        userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public User create(CreateUserRequest createUserRequest, BinaryContentDTO binaryContentDTO) {
        if (userRepository.existsName(createUserRequest.name())) {
            throw new MemberRegistException(createUserRequest.name());
        }
        if (userRepository.existsEmail(createUserRequest.email())) {
            throw new EmailRegistException(createUserRequest.email());
        }

        UUID porfileID =null;
        if (binaryContentDTO != null) {
            String fileName = binaryContentDTO.fileName();
            String contentType = binaryContentDTO.type();
            byte[] bytes = binaryContentDTO.bytes();
            BinaryContent content = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            porfileID=  binaryContentRepository.save(content).getId();
        }

        User user = User.createProfile(createUserRequest.name(), createUserRequest.email(), createUserRequest.password(), porfileID);
        userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user.getId(), now);
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

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);

        userStatusRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @Override
    public User update(UpdateUserRequest updateUserRequest, BinaryContentDTO binaryContentDTO) {
        User user = userRepository.findById(updateUserRequest.id())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        String newName = updateUserRequest.name();
        String newPassword = updateUserRequest.password();
        String newEmail = updateUserRequest.email();

        if (userRepository.existsEmail(newEmail)) {
            throw new IllegalArgumentException("이미 있는 이메일입니다.");
        }
        if (userRepository.existsName(newName)) {
            throw new IllegalArgumentException("이미 있는 유저 이름입니다.");
        }

        UUID porfileID =null;
        if (binaryContentDTO != null) {
            String fileName = binaryContentDTO.fileName();
            String contentType = binaryContentDTO.type();
            byte[] bytes = binaryContentDTO.bytes();
            BinaryContent content = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
            porfileID=  binaryContentRepository.save(content).getId();
        }

        user.update(newName, newEmail,  newPassword, porfileID);
        userRepository.save(user);
        return user;

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
                user.getName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }

}
