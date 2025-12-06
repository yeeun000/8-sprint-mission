package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Scanner;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    private static JCFMessageService instance;
    private final Scanner sc;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private JCFMessageService() {
        this.sc = new Scanner(System.in);
        this.channelRepository = JCFChannelRepository.getInstance();
        this.userRepository = JCFUserRepository.getInstance();
        this.messageRepository = JCFMessageRepository.getInstance();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }



    public Message create(String content, UUID channelId, UUID authorId) {
        Channel channel = channelRepository.readId(channelId);
        User author = userRepository.readId(authorId);

        Message message = new Message(content, author, channel);
        messageRepository.addMessage(channel, message);
        return message;
    }
}
