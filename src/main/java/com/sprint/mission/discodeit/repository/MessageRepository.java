package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    List<Message> findAll();

    Optional<Message> findById(UUID id);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);

    List<Message> findAllByChannelId(UUID channelId);
}
