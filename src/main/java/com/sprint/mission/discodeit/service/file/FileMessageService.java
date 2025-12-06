package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.Scanner;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static FileMessageService instance;
    private final Scanner sc;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    private FileMessageService() {
        this.sc = new Scanner(System.in);
        this.channelRepository = FileChannelRepository.getInstance();
        this.userRepository = FileUserRepository.getInstance();
        this.messageRepository = FileMessageRepository.getInstance();
    }

    public static FileMessageService getInstance() {
        if (instance == null) {
            instance = new FileMessageService();
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
