package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void add(Message message);

    List<Message> findAll();

    Message findId(UUID messageId);

    void remove(UUID messageId);

    Instant last(UUID channelId);
}
