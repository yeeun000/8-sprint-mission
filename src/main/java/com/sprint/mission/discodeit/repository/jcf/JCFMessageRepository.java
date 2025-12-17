package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@Repository
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList = new HashMap<>();


    @Override
    public void add(Message message) {
        messageList.put(message.getId(), message);
    }

    @Override
    public List<Message> findAll() {
        return messageList.values().stream().toList();
    }

    @Override
    public Message findId(UUID messageId) {
        boolean find = messageList.containsKey(messageId);
        if (find)
            return messageList.get(messageId);
        else return null;
    }

    @Override
    public void remove(UUID messageId) {
        messageList.remove(messageId);
    }

    @Override
    public Instant last(UUID channelId) {
        Instant a = Instant.now();
        return a;
    }

}
