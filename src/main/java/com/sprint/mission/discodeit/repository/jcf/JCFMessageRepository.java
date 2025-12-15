package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList = new HashMap<>();
    private static JCFMessageRepository instance = new JCFMessageRepository();

    private JCFMessageRepository() {
    }

    public static JCFMessageRepository getInstance() {
        return instance;
    }

    public void add(Message message) {
        messageList.put(message.getId(), message);
    }

    public List<Message> findAll() {
        return messageList.values().stream().toList();
    }

    public Message findId(UUID messageId) {
        boolean find = messageList.containsKey(messageId);
        if (find)
            return messageList.get(messageId);
        else return null;
    }

    public void remove(UUID messageId) {
        messageList.remove(messageId);
    }

    public Instant last(UUID channelId){
        Instant a=Instant.now();
        return a;
    }

}
