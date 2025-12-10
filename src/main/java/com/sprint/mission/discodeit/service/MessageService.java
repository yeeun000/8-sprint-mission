package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID channelId, UUID authorId);
    List<Message> findAll();
    void delete(UUID messageId);
    Message update(UUID messageId, String content);
    Message findId(UUID messageId);
}
