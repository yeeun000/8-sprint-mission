//package com.sprint.mission.discodeit.service.basic;
//
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
//
//import java.util.Scanner;
//import java.util.UUID;
//
//
//public class BasicMessageService implements MessageService {
//
//    private static BasicMessageService instance;
//    private MessageRepository messageRepository;
//    private UserRepository userRepository;
//    private ChannelRepository channelRepository;
//
//    private BasicMessageService() {
//        this.channelRepository = JCFChannelRepository.getInstance();
//        this.userRepository = JCFUserRepository.getInstance();
//        this.messageRepository = JCFMessageRepository.getInstance();
//    }
//
//    public static BasicMessageService getInstance() {
//        if (instance == null) {
//            instance = new BasicMessageService();
//        }
//        return instance;
//    }
//
//    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
//        this.messageRepository = messageRepository;
//        this.userRepository = userRepository;
//        this.channelRepository = channelRepository;
//    }
//
//    public Message create(String content, UUID channelId, UUID authorId) {
//        Channel channel = channelRepository.readId(channelId);
//        User author = userRepository.readId(authorId);
//
//        Message message = new Message(content, author, channel);
//        messageRepository.addMessage(channel, message);
//        return message;
//    }
//
//    public Channel readId(Long id){
//        for(Channel channel : channelList){
//            if(channel.getId().equals(id)){
//                return channel;
//            }
//        }
//        return null;
//    }
//}
