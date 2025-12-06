package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.Data;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;
import static com.sprint.mission.discodeit.service.Data.channelList;

public class FileChannelService implements ChannelService {

    private static FileChannelService instance;
    private final Scanner sc;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    private FileChannelService() {
        this.sc = new Scanner(System.in);
        this.channelRepository = FileChannelRepository.getInstance();
        this.userRepository = FileUserRepository.getInstance();
        this.messageService = FileMessageService.getInstance();
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            instance = new FileChannelService();
        }
        return instance;
    }

    public Channel create(Channel.ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        channelRepository.addChannel(channel);
        return channel;
    }



    public void create(User user) {
        Channel c;

        while (true) {
            System.out.print("생성하고 싶은 채널이름을 작성해주세요 : ");

            String newChannelName = sc.nextLine();

            if (newChannelName.equals("exit")) {
                System.out.println("서비스를 종료합니다.");
                break;
            }

            c = readChannelName(newChannelName);

            System.out.println("설명을 적어주세요. ");
            String description=sc.nextLine().trim();


            if (c == null) {
                Channel channel = new Channel(PUBLIC, newChannelName, description);
                //Channel channel = new Channel(newChannelName, user);
                channelRepository.addChannel(channel);

                try (FileOutputStream fos = new FileOutputStream("channel.ser");
                     ObjectOutputStream oos = new ObjectOutputStream(fos);) {
                    oos.writeObject(channelList);
                } catch (IOException e) {
                }

                System.out.println("채널 생성이 완료됐습니다.");
                break;
            } else {
                System.out.println(" 이미 있는 채널이름입니다. 다른 이름을 적어주세요 ");
                System.out.println(" 서비스를 종료하고 싶으면 exit를 입력해주세요 \n");
            }
        }
    }

    public void delete(User user) {
        System.out.println(" 삭제하고 싶은 채널을 선택하세요. ");
        System.out.println(" 해당 서비스를 종료하고 싶으면 exit를 입력해주세요");

        readAll();

        System.out.print(" 채널 이름 : ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        System.out.println();

        if (input.equals("exit")) {
            return;
        }

        Channel c = readChannelName(input);

        if (c != null) {
            channelRepository.removeChannel(c);
            System.out.println(" 삭제 됐습니다. ");
            return;
        }

        System.out.println(" 채널이 없습니다.  ");
    }

    public void readAll() {
        System.out.println(" 전체 채널 ");
        for (Channel c : channelList) {
            System.out.println(c.getChannelName());
        }
    }

    public Channel readChannelName(String channelName) {
        for (Channel c : channelList) {
            if (c.getChannelName().equals(channelName)) {
                return c;
            }
        }
        return null;
    }

    public void read(User user) {
        System.out.println(" 방문하고 싶은 채널을 입력해주세요 ");
        readAll();

        Scanner sc = new Scanner(System.in);
        String visitChannel = sc.nextLine();

        Channel c = readChannelName(visitChannel);

        if (c != null) {
            System.out.println("\n" + visitChannel + " 채널에 어서오세요. ");

            while (true) {
                for (Message m : c.getMessages()) {
                    System.out.print(m.toString());
                }

                System.out.println("\n하고 싶은 서비스를 입력해주세요.");
                System.out.println("1. 채널 상세 정보");
                System.out.println("2. 메시지 보내기");
                System.out.println("3. 서비스 종료");
                String input = sc.nextLine().trim();

                switch (input) {
                    case "1":
                        System.out.println("\n" + c.getChannelName() + " 채널 상세 정보 ");
                        System.out.println(c.toString());
                        System.out.println();
                        break;
                    case "2":
                        System.out.println("메시지 내용을 적어주세요.");
                        String content = sc.nextLine();
                        messageService.create(content, user.getId(), c.getId());
                        break;
                    case "3":
                        System.out.println("서비스를 종료합니다. \n");
                        return;
                    default:
                        System.out.println("다시 입력해주세요. ");
                }
            }
        }
        System.out.println(" 채널이 없습니다.  ");
    }

    public void update() {
        System.out.println("수정하고 싶은 채널을 입력해주세요.");
        String UChannel = sc.next().trim();

        Channel channel = readChannelName(UChannel);

        if (channel != null) {
            while (true) {
                System.out.println("변경하고 싶은 정보의 번호를 입력해주세요.  ");
                System.out.println("1. 채널 이름");
                System.out.println("2. 서비스 종료");
                String input = sc.nextLine().trim();

                switch (input) {
                    case "2":
                        System.out.println("서비스를 종료합니다.");
                        return;
                    case "1":
                        System.out.print("변경하고 싶은 채널 이름을 입력해주세요 : ");
                        String channelName = sc.nextLine().trim();

                        Channel newName = readChannelName(channelName);

                        if (newName == null) {
                            channel.setChannelName(channelName);
                            channel.setUpdateAt();
                            System.out.println("변경이 완료됐습니다. \n");
                        } else {
                            System.out.println("이미 사용중입니다. \n");
                        }
                        break;
                    default:
                        System.out.println(" 변경 할 수 없습니다. ");
                }
                System.out.println();
            }
        }
    }

    public void modifyChannel(User user) {
        System.out.println("\n (가입/ 탈퇴) 하고 싶은 채널을 입력해주세요. ");
        System.out.print("채널 이름 : ");
        String input = sc.next().trim();

        Channel channel = readChannelName(input);

        if (channel != null) {
            System.out.println("원하는 서비스를 입력해주세요. ");
            System.out.println("1. 가입");
            System.out.println("2. 탈퇴");
            System.out.println("3. 서비스 종료");
            System.out.print("번호 입력 : ");
            String input2 = sc.next().trim();
            System.out.println();

            switch (input2) {
                case "3":
                    System.out.println("서비스를 종료합니다. \n");
                    return;
                case "1":
                    for (User u : channel.getUser()) {
                        if (u.equals(user)) {
                            System.out.println("이미 가입 상태입니다.");
                            return;
                        }
                    }
                    channel.addUsers(user);
                    channel.setUpdateAt();
                    try (FileOutputStream fos = new FileOutputStream("channel.ser");
                         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                        oos.writeObject(Data.channelList);
                    } catch (IOException e) {
                    }
                    System.out.println("가입 됐습니다.");
                    break;
                case "2":
                    for (User u : channel.getUser()) {
                        if (u.equals(user)) {
                            channel.deleteUsers(user);
                            System.out.println("탈되 됐습니다.");
                            return;
                        }
                    }
                    System.out.println("가입 정보가 없습니다.");
                    break;
                default:
            }
        }
    }
}

