package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {

    private JCFUserRepository userRepository = JCFUserRepository.getInstance();
    private Map<UUID, UserStatus> statusList = new HashMap<>();

    public JCFUserStatusRepository() {
    }

    public boolean onlineStatus(UUID id) {
        User findId = userRepository.findId(id);
        UserStatus status = new UserStatus(findId.getId());
        return status.accessTime();
    }

    public void add(UserStatus status) {
        statusList.put(status.getId(), status);
    }

    public UserStatus find(UUID id){
       return  statusList.get(id);
    }

    public List<UserStatus> findAll(){
        return statusList.values().stream().toList();
    }

    public void remove(UUID userId) {
        statusList.remove(userId);
    }


}
