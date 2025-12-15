package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private Map<UUID, ReadStatus> readStatusList = new HashMap<>();

    public void add(ReadStatus readStatus) {
        readStatusList.put(readStatus.getId(), readStatus);
    }

    public ReadStatus find(UUID id) {
        return readStatusList.get(id);
    }

    public List<ReadStatus> findALL(UUID userId) {
        List<ReadStatus> findUser = new ArrayList<>();
        for (ReadStatus re : readStatusList.values()) {
            if (re.getUserId().equals(userId)) {
                findUser.add(re);
            }
        }
        return findUser;
    }

    public void remove(UUID id) {
        readStatusList.remove(id);
    }

    public boolean exists(UUID userId, UUID channelId) {
        for (ReadStatus read : readStatusList.values()) {
            if (read.getUserId().equals(userId) && read.getChannelId().equals(channelId))
                return true;
        }
        return false;
    }


}
