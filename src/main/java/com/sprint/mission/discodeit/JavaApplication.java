package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


public class JavaApplication {
    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }
    public static void main(String[] args) {

        UserRepository userRepository = FileUserRepository.getInstance();
        ChannelRepository channelRepository = FileChannelRepository.getInstance();
        MessageRepository messageRepository = FileMessageRepository.getInstance();

        // 서비스 초기화
        UserService userService = BasicUserService.getInstance(userRepository);
        ChannelService channelService = BasicChannelService.getInstance(channelRepository);
        MessageService messageService = BasicMessageService.getInstance(messageRepository, channelRepository, userRepository);

        // 셋업
        User user = setupUser(userService);
        System.out.println(userService.findAll());

        Channel channel = setupChannel(channelService);
        System.out.println(channelService.findAll());


        // 테스트
        messageCreateTest(messageService, channel, user);
        System.out.println(messageService.findAll());
    }
}
