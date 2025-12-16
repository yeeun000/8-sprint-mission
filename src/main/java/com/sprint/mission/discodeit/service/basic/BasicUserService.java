package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.ProfileDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.FindUserDTO;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {

    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private BinaryContentRepository binaryContentRepository;

    public BasicUserService(UserRepository userRepository, UserStatusRepository userStatusRepository, BinaryContentRepository binaryContentRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public User create(CreateUserDTO userDTO) {
        if (userRepository.existsName(userDTO.name())) {
            throw new IllegalArgumentException(userDTO.name() + "이 이미 있습니다.");
        }
        if (userRepository.existsEmail(userDTO.email())) {
            throw new IllegalArgumentException(userDTO.email() + "이 이미 있습니다.");
        }

        User user = new User(userDTO.name(), userDTO.password(), userDTO.email(), userDTO.profileImage());
        userRepository.add(user);

        UserStatus status = new UserStatus(user.getId());
        userStatusRepository.add(status);

        if (userDTO.profileImage().isPresent()) {
            BinaryContent content = new BinaryContent(user.getId(), userDTO.profileImage().get().fileName(), userDTO.profileImage().get().filePath(), true);
            binaryContentRepository.add(content);
        }
        return user;
    }

    @Override
    public List<FindUserDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<FindUserDTO> userStatus = new ArrayList<>();

        for (User user : users) {
            boolean online = userStatusRepository.onlineStatus(user.getId());
            FindUserDTO findUserDTO = new FindUserDTO(user.getId(), user.getName(), online);
            userStatus.add(findUserDTO);
        }
        return userStatus;
    }

    @Override
    public void delete(UUID userId) {
        if (userRepository.findId(userId) == null)
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        binaryContentRepository.removeProfile(userId);
        userStatusRepository.remove(userId);
        userRepository.remove(userId);
    }

    @Override
    public User update(UpdateUserDTO updateUserDTO) {
        User user = userRepository.findId(updateUserDTO.id());
        if (user == null)
            throw new NoSuchElementException(updateUserDTO.id() + "를 찾을 수 없습니다.");

        if (updateUserDTO.profileImage().isPresent()) {
            binaryContentRepository.removeProfile(user.getId());
            BinaryContent content = new BinaryContent(user.getId(), updateUserDTO.profileImage().get().fileName(), updateUserDTO.profileImage().get().filePath(), true);
            binaryContentRepository.add(content);
            ProfileDTO dto = new ProfileDTO(user.getId(), updateUserDTO.profileImage().get().fileName(), updateUserDTO.profileImage().get().filePath());
            user.setProfileImage(dto);
        } else {
            if (user.getProfileImage() != null) {
                user.setProfileImage(null);
                binaryContentRepository.removeProfile(user.getId());
            }
        }
        user.update(updateUserDTO.name(), updateUserDTO.password(), updateUserDTO.email());
        return user;
    }

    @Override
    public FindUserDTO findId(UUID userId) {
        User user = userRepository.findId(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        }

        boolean online = userStatusRepository.onlineStatus(user.getId());
        FindUserDTO dto = new FindUserDTO(user.getId(), user.getName(), online);
        return dto;
    }

}
