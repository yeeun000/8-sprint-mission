package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.List;
import java.util.UUID;

public class FileUserStatusRepository extends FileRepository<UserStatus> implements UserStatusRepository {

    private UserRepository userRepository;

    private FileUserStatusRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/UserStatus.ser");
    }


    public boolean onlineStatus(UUID id) {
        User findId = userRepository.findId(id);
        UserStatus status = new UserStatus(findId.getId());
        return status.accessTime();
    }

    public void add(UserStatus status) {
        getFile().put(status.getId(), status);
    }

    public UserStatus find(UUID id) {
        return getFile().get(id);
    }

    public List<UserStatus> findAll() {
        return getFile().values().stream().toList();
    }

    public void remove(UUID userId) {
        getFile().remove(userId);
    }
}
