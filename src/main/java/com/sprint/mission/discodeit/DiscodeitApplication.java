package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class DiscodeitApplication {
    static User setupUser(UserService userService) {
        CreateUserRequest request = new CreateUserRequest("woody", "woody@codeit.com", "woody1234");
        User user = userService.create(request);
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        PublicChannelDTO request = new PublicChannelDTO("공지", "공지 채널입니다.");
        Channel channel = channelService.create(request);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        CreateMessageRequest request = new CreateMessageRequest("안녕하세요.", channel.getId(), author.getId());
        Message message = messageService.create(request, new ArrayList<>());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
        // 서비스 초기화
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);

        messageCreateTest(messageService, channel, user);
    }
}
