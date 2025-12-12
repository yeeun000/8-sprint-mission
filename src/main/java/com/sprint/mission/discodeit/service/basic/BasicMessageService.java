package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {

    private static BasicMessageService instance;

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;


    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    public static BasicMessageService getInstance(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        if (instance == null) {
            instance = new BasicMessageService(messageRepository, channelRepository, userRepository);
        }
        return instance;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        messageRepository.add(message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public void delete(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        messageRepository.remove(messageId);
    }

    @Override
    public Message update(UUID messageId, String content) {
        Message message = findId(messageId);
        if (message == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        message.update(content);
        return message;
    }

    @Override
    public Message findId(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        return messageRepository.findId(messageId);
    }
}
