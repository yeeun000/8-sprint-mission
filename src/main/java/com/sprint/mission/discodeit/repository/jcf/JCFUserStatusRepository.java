package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

    private Map<UUID, UserStatus> statusList = new HashMap<>();

    private UserRepository userRepository;

    public JCFUserStatusRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean onlineStatus(UUID id) {
        User user = userRepository.findId(id);
        UserStatus status = statusList.get(id);
        if (status == null) {
            return false;
        }
        return status.accessTime();
    }

    @Override
    public void add(UserStatus status) {
        statusList.put(status.getId(), status);
    }

    @Override
    public UserStatus find(UUID id) {
        return statusList.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return statusList.values().stream().toList();
    }

    @Override
    public void remove(UUID userId) {
        statusList.remove(userId);
    }


}
