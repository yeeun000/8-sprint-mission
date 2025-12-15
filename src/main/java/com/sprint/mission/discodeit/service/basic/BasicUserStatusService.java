package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserStatusService implements UserStatusService {

    private UserStatusRepository userStatusRepository;
    private UserRepository userRepository;


    public void create(UserStatusDTO userStatusDTO){
        if(userRepository.findId(userStatusDTO.userId())==null)
            throw new NoSuchElementException(userStatusDTO.userId() + "를 찾을 수 없습니다.");
        if(userStatusRepository.find(userStatusDTO.userId())!=null)
            throw new IllegalArgumentException(userStatusDTO.userId() + "이 이미 있습니다.");
    }

    public UserStatus find(UUID id){
        return userStatusRepository.find(id);
    }

    public void findAll(){
        userStatusRepository.findAll();
    }

    public void update(UserStatusDTO userStatusDTO){
        UserStatus status = find(userStatusDTO.id());
        if (status == null) {
            throw new NoSuchElementException(userStatusDTO.id() + "를 찾을 수 없습니다.");
        }
        status.setLastCome(Instant.now());
        userStatusRepository.add(status);
    }

    public void updateByUserId(UUID userId){
        UserStatus status = find(userId);
        if (status == null) {
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        }
        status.setLastCome(Instant.now());
    }

    public void delete(UUID id){
        userStatusRepository.remove(id);
    }

}
