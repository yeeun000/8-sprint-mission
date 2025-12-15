package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {
    //    static User setupUser(UserService userService) {
//        User user = userService.create("woody", "woody@codeit.com", "woody1234");
//        return user;
//    }
//
//    static Channel setupChannel(ChannelService channelService) {
//        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//        return channel;
//    }
//
//
//    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
//        System.out.println("메시지 생성: " + message.getId());
//    }
//
    static User setupUser(UserService userService) {
        UserDTO dto = new UserDTO(null,"woody", "woody@codeit.com", "woody1234", Optional.empty());
        return userService.create(dto);
    }

    static Channel setupChannel(ChannelService channelService) {
        ChannelDTO dto = new ChannelDTO(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.", null);
        return channelService.create(dto);
    }


    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageDTO dto = new MessageDTO(null, channel.getId(), author.getId(), "안녕하세요.",null);
        Message message = messageService.create(dto);
        System.out.println("메시지 생성: " + message.getId());
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        User user = setupUser(userService);
        System.out.println(userService.findAll());

        Channel channel = setupChannel(channelService);
        System.out.println(channelService.findAllByUserId(user.getId()));

        messageCreateTest(messageService, channel, user);
        System.out.println(messageService.findallByChannelId(channel.getId()));
    }

}
