package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Scanner;

import static com.sprint.mission.discodeit.service.Data.userList;

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

        UserService userService = JCFUserService.getInstance();
        MessageService MessageService = JCFMessageService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();

//        UserService userService = FileUserService.getInstance();
//        MessageService MessageService = FileMessageService.getInstance();
//        ChannelService channelService = FileChannelService.getInstance();

//        UserRepository userRepository = JCFUserRepository.getInstance();
//        ChannelRepository channelRepository = JCFChannelRepository.getInstance();
//        MessageRepository messageRepository = JCFMessageRepository.getInstance();
//
//
//        MessageService messageService = new BasicMessageService(messageRepository, userRepository, channelRepository);
//        UserService userService = new BasicUserService(userRepository);
//        ChannelService channelService = new BasicChannelService(channelRepository, messageService);

//        User user = setupUser(userService);
//        Channel channel = setupChannel(channelService);
//
//        // 테스트
//        messageCreateTest(messageService, channel, user);


        Scanner sc = new Scanner(System.in);
        String choose;
        User userlogin=null;

        while(true){

            System.out.println("서비스를 선택해주세요. ex) 1 ");
            System.out.println(" 0. 로그인 / 로그 아웃 ");
            System.out.println(" 1. 회원가입 ");
            System.out.println(" 2. 채널 생성 ");
            System.out.println(" 3. 채널 삭제 ");
            System.out.println(" 4. 채널 가입 / 탈퇴 ");
            System.out.println(" 5. 채널 방문 ");
            System.out.println(" 6. 채널 수정 ");
            System.out.println(" 7. 탈퇴 ");
            System.out.println(" 8. 회원 정보 수정 ");
            System.out.println(" 9. 종료하기 ");
            System.out.println();

            System.out.print(" 번호를 적어주세요 :  ");
            choose = sc.nextLine().trim();

            System.out.println();

            if(choose.equals("9")){ // 프로그램 종료
                System.out.println("서비스를 종료합니다. ");
                break;
            }
            else if(userlogin==null){ // 로그인 X
                if(choose.equals("0")){ // 로그인
                    userlogin = userService.login();

                }
                else if(choose.equals("1")){ //회원가입
                    userService.create();

                } else{
                    System.out.println(" 로그인 해주세요 ");
                }
            }
            else{ // 로그인 성공
                if(choose.equals("2")){ // 채널 생성
                    channelService.create(userlogin);
                }
                else if(choose.equals("3")){ // 채널 삭제하기
                    channelService.delete(userlogin);
                }
                else if(choose.equals("4")){ // 채널 가입
                    channelService.readAll();
                    channelService.modifyChannel(userlogin);
                }
                else if(choose.equals("5")){ // 채널 방문
                    channelService.read(userlogin);
                }
                else if(choose.equals("6")){ // 채널 수정
                    channelService.readAll();
                    channelService.update();
                }
                else if(choose.equals("7")){ // 탈퇴
                    userService.delete(userlogin.getId());
                    userlogin=null;
                }
                else if(choose.equals("8")){ //회원 정보 수정
                    userService.read(userlogin);
                    userService.update(userlogin);
                }
                else if(choose.equals("1")){
                    System.out.println(" 이미 로그인 상태입니다. ");
                }
                else if(choose.equals("0")){
                    userlogin=null;
                    System.out.println("로그아웃 됐습니다.");
                }
                else{
                    System.out.println(" 숫자만 입력해주세요 ");
                }
            }
            System.out.println();
        }
    }
}
