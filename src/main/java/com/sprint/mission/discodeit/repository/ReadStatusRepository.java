package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    void add(ReadStatus readStatus);

    void remove(UUID id);

    ReadStatus find(UUID id);

    List<ReadStatus> findAll(UUID userId);

    boolean exists(UUID userId, UUID channelID);
}
