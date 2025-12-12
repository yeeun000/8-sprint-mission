package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
//    static User setupUser(UserService userService) {
//        User user = userService.create("woody", "woody@codeit.com", "woody1234");
//        return user;
//    }
//
//    static User setupUser2(UserService userService) {
//        User user = userService.create("qwer", "qwer", "qwerqwer");
//        return user;
//    }
//
//    static Channel setupChannel(ChannelService channelService) {
//        Channel channel = channelService.create(Channel.ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//        return channel;
//    }
//
//    static Channel setupChannel2(ChannelService channelService) {
//        Channel channel = channelService.create(Channel.ChannelType.PRIVATE, "asdf", "asdf 채널입니다.");
//        return channel;
//    }
//
//    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
//        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
//        System.out.println("메시지 생성: " + message.getId());
//    }
//
//    static void messageCreateTest2(MessageService messageService, Channel channel, User author) {
//        Message message = messageService.create("zxcvzxcvzxcv.", channel.getId(), author.getId());
//        System.out.println("메시지 생성: " + message.getId());
//    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

//        User user = setupUser(userService);
//        User user2 = setupUser2(userService);
//        System.out.println(userService.findAll());
//        userService.findAll().forEach(System.out::println);
//
//        Channel channel = setupChannel(channelService);
//        Channel channel1 = setupChannel2(channelService);
//        System.out.println(channelService.findAll());
//        channelService.findAll().forEach(System.out::println);
//
//        messageCreateTest(messageService, channel, user);
//        messageCreateTest2(messageService, channel1, user2);
//        System.out.println(messageService.findAll());
//        messageService.findAll().forEach(System.out::println);
    }

}
