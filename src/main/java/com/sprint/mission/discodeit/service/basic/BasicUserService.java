package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicUserService implements UserService {

    private static BasicUserService instance;
    private UserRepository userRepository;
    private UserStatusRepository userStatusRepository;
    private BinaryContentRepository binaryContentRepository;

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static BasicUserService getInstance(UserRepository repository) {
        if (instance == null) {
            instance = new BasicUserService(repository);
        }
        return instance;
    }

    @Override
    public User create(UserDTO userDTO) {
        if (userRepository.existsName(userDTO.name())) {
            throw new IllegalArgumentException(userDTO.name() + "이 이미 있습니다.");
        }
        if (userRepository.existsEmail(userDTO.email())) {
            throw new IllegalArgumentException(userDTO.email() + "이 이미 있습니다.");
        }

        User user = new User(userDTO.name(), userDTO.password(), userDTO.email(), userDTO.profileImage());
        if (userDTO.profileImage().isPresent()) {
            BinaryContent content = new BinaryContent(userDTO.id(), userDTO.profileImage().get().fileName(), userDTO.profileImage().get().filePath());
            binaryContentRepository.add(content);
        }
        UserStatus status = new UserStatus(userDTO.id());
        userStatusRepository.add(status);
        userRepository.add(user);

        return user;
    }

    @Override
    public List<UserStatusDTO> findAll() {
        List<User> users = userRepository.findAll();
        List<UserStatusDTO> userStatus = new ArrayList<>();

        for (User user : users) {
            boolean online = userStatusRepository.onlineStatus(user.getId());
            UserStatusDTO userStatusDTO = new UserStatusDTO(user.getId(), user.getName(), online);
            userStatus.add(userStatusDTO);
        }
        return userStatus;
    }

    @Override
    public void delete(UUID userId) {
        if (userRepository.findId(userId) == null)
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        userRepository.remove(userId);
        userStatusRepository.remove(userId);
        binaryContentRepository.remove(userId);
    }

    @Override
    public User update(UserDTO userDTO) {
        User user = userRepository.findId(userDTO.id());
        if (user == null)
            throw new NoSuchElementException(userDTO.id() + "를 찾을 수 없습니다.");
        user.update(userDTO.name(), userDTO.password(), userDTO.email(), userDTO.profileImage());
        return user;
    }

    @Override
    public UserStatusDTO findId(UUID userId) {
        User user = userRepository.findId(userId);

        if (user == null) {
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        }

        boolean online = userStatusRepository.onlineStatus(user.getId());
        UserStatusDTO userStatusDTO = new UserStatusDTO(user.getId(), user.getName(), online);
        return userStatusDTO;
    }

}
