//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.List;
//import java.util.Scanner;
//import java.util.UUID;
//
//
//public class BasicUserService implements UserService {
//
//    private static BasicUserService instance;
//    private ChannelRepository channelRepository;
//    private UserRepository userRepository;
//
//    private BasicUserService() {
//        this.channelRepository = JCFChannelRepository.getInstance();
//        this.userRepository = JCFUserRepository.getInstance();
//    }
//
//    public static BasicUserService getInstance() {
//        if (instance == null) {
//            instance = new BasicUserService();
//        }
//        return instance;
//    }
//    public BasicUserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public User create(String name, String email, String nickname) {
//        User user = new User(name, nickname, email);
//        userRepository.addUser(user);
//        return user;
//    }
//
//    public void create() {
//        String newName, newNickName, newEmail;
//        System.out.println(" 회원가입을 진행하겠습니다.");
//        System.out.println();
//        User u;
//        while (true) {
//            System.out.print(" 아이디를 입력해주세요 : ");
//            newName = sc.nextLine().trim();
//            u = readName(newName);
//            if (u == null) {
//                break;
//            }
//            System.out.println(" 이미 사용중인 아이디입니다. ");
//        }
//        while (true) {
//            System.out.print(" 닉네임을 입력해주세요 : ");
//            newNickName = sc.nextLine().trim();
//            u = readNickname(newNickName);
//            if (u == null) {
//                break;
//            }
//            System.out.println(" 이미 사용중인 닉네임입니다. ");
//        }
//        while (true) {
//            System.out.print(" 이메일을 입력해주세요 : ");
//            newEmail = sc.nextLine().trim();
//            u = readEmail(newEmail);
//            if (u == null) {
//                break;
//            }
//            System.out.println(" 이미 사용중인 이메일입니다. ");
//        }
//
//        System.out.println();
//
//        User user = new User(newName, newNickName, newEmail);
//        userRepository.addUser(user);
//        System.out.println("회원가입이 완료됐습니다.");
//    }
//
//    public User login() {
//        System.out.println("로그인을 진행하겠습니다.");
//        System.out.println();
//
//        System.out.print("아이디를 적어주세요 : ");
//        String loginName = sc.nextLine().trim();
//
//        System.out.print("이메일을 적어주세요 : ");
//        String loginEmail = sc.nextLine().trim();
//
//        User name = readName(loginName);
//        User email = readEmail(loginEmail);
//
//        if (name != null && name.equals(email)) {
//            System.out.println("\n" + loginName + "님 환영합니다. ");
//            return name;
//        }
//        System.out.println(" 해당 정보가 없습니다. 회원가입 해주세요. ");
//        return null;
//    }
//
//    public void delete(UUID id) {
//        User u = readID(id);
//
//        userRepository.removeUser(u);
//        System.out.println(" 탈퇴 됐습니다. ");
//    }
//
//    public User readID(UUID id) {
//        for (User user : userRepository.findAll()) {
//            if (user.getId().equals(id)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    public User readName(String name) {
//        for (User user : userRepository.findAll()) {
//            if (user.getName().equals(name)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    public User readNickname(String nickname) {
//        for (User user : userRepository.findAll()) {
//            if (user.getNickName().equals(nickname)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    public User readEmail(String email) {
//        for (User user : userRepository.findAll()) {
//            if (user.getEmail().equals(email)) {
//                return user;
//            }
//        }
//        return null;
//    }
//
//    public void read(User user) {
//        System.out.println(" 유저 상세 정보 ");
//        System.out.println(user.toString());
//        System.out.println();
//    }
//
//    public void update(User user) {
//        while (true) {
//            System.out.println("변경하고 싶은 정보의 번호를 입력해주세요.  ");
//            System.out.println("1. 이름");
//            System.out.println("2. 닉네임");
//            System.out.println("3. 이메일");
//            System.out.println("4. 서비스 종료");
//            String input = sc.nextLine().trim();
//
//            switch (input) {
//                case "4":
//                    System.out.println("서비스를 종료합니다. ");
//                    return;
//                case "1":
//                    System.out.print("변경하고 싶은 이름을 입력해주세요 : ");
//                    String name = sc.nextLine().trim();
//
//                    User newName = readName(name);
//
//                    if (newName == null) {
//                        userRepository.updateName(user,name);
//                        System.out.println("변경이 완료됐습니다. \n");
//                    } else {
//                        System.out.println("이미 사용중입니다. \n");
//                    }
//                    break;
//                case "2":
//                    System.out.print("변경하고 싶은 닉네임을 입력해주세요 : ");
//                    String nickname = sc.nextLine().trim();
//
//                    User newNickname = readNickname(nickname);
//
//                    if (newNickname == null) {
//                        userRepository.updateNickname(user,nickname);
//                        System.out.println("변경이 완료됐습니다. \n");
//                    } else {
//                        System.out.println("이미 사용중입니다. \n");
//                    }
//                    break;
//                case "3":
//                    System.out.print("변경하고 싶은 이메일을 입력해주세요 : ");
//                    String email = sc.nextLine().trim();
//
//                    User newEmail = readEmail(email);
//
//                    if (newEmail == null) {
//                        userRepository.updateEmail(user,email);
//                        System.out.println("변경이 완료됐습니다. \n");
//                    } else {
//                        System.out.println("이미 사용중입니다. \n");
//                    }
//                    break;
//                default:
//                    System.out.println(" 변경 할 수 없습니다. ");
//            }
//            System.out.println();
//        }
//    }
//}
