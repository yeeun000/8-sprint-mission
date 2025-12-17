package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.userDTO.CreateUserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        CreateUserDTO dto = new CreateUserDTO("woody", "woody1234", "woody@codeit.com", Optional.empty());
        return userService.create(dto);
    }

    static User setupUser2(UserService userService) {
        CreateUserDTO dto = new CreateUserDTO("asdf", "zxcv", "qwer@codeit.com", Optional.empty());
        return userService.create(dto);
    }

    static Channel setupPublicChannel(ChannelService channelService) {
        PublicChannelDTO dto = new PublicChannelDTO(Channel.ChannelType.PUBLIC, "공지 채널", "공지 채널입니다.");
        return channelService.create(dto);
    }

    static Channel setupPrivateChannel(ChannelService channelService, User user1) {
        List<UUID> participants = List.of(user1.getId());
        PrivateChannelDTO dto = new PrivateChannelDTO(Channel.ChannelType.PRIVATE, participants);
        return channelService.create(dto);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        CreateMessageDTO dto = new CreateMessageDTO(author.getId(), channel.getId(), "안녕하세요.", Collections.emptyList());
        Message message = messageService.create(dto);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 1. 유저 생성
        User user1 = setupUser(userService);
        User user2 = setupUser2(userService);
        System.out.println("모든 유저: " + userService.findAll());

        // 2. 채널 생성
        Channel publicChannel = setupPublicChannel(channelService);
        Channel privateChannel = setupPrivateChannel(channelService, user1);

        // 3. 각 유저별 채널 조회
        System.out.println("User1 채널: " + channelService.findAllByUserId(user1.getId()));
        System.out.println("User2 채널: " + channelService.findAllByUserId(user2.getId()));

        // 4. 메시지 생성
        messageCreateTest(messageService, publicChannel, user1);
        messageCreateTest(messageService, privateChannel, user2);

        // 5. 채널별 메시지 조회
        System.out.println("Public 채널 메시지: " + messageService.findallByChannelId(publicChannel.getId()));
        System.out.println("Private 채널 메시지: " + messageService.findallByChannelId(privateChannel.getId()));
    }
}
