package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList = new HashMap<>();


    @Override
    public Message save(Message message) {
        messageList.put(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageList.values().stream().toList();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageList.get(id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        messageList.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(message -> deleteById(message.getId()));
    }

}
