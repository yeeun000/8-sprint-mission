package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userDTO.UserStateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final  UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    
    @Override
    public void create(UserStateDTO userStatusDTO) {
        if (userRepository.findId(userStatusDTO.userId()) == null)
            throw new NoSuchElementException(userStatusDTO.userId() + "를 찾을 수 없습니다.");
        if (userStatusRepository.find(userStatusDTO.userId()) != null)
            throw new IllegalArgumentException(userStatusDTO.userId() + "이 이미 있습니다.");
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.find(id);
    }

    @Override
    public void findAll() {
        userStatusRepository.findAll();
    }

    @Override
    public void update(UserStateDTO userStateDTO) {
        UserStatus status = find(userStateDTO.id());
        if (status == null) {
            throw new NoSuchElementException(userStateDTO.id() + "를 찾을 수 없습니다.");
        }
        status.setLastCome(Instant.now());
        userStatusRepository.add(status);
    }

    @Override
    public void updateByUserId(UUID userId) {
        UserStatus status = find(userId);
        if (status == null) {
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        }
        status.setLastCome(Instant.now());
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.remove(id);
    }

}
