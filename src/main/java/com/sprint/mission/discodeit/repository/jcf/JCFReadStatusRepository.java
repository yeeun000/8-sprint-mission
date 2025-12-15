package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private Map<UUID, ReadStatus> readStatusList = new HashMap<>();

    public void add(ReadStatus readStatus){
        readStatusList.put(readStatus.getId(), readStatus);
    }

    public void remove(UUID id){
        readStatusList.remove(id);
    }


}
