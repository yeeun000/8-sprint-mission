//package com.sprint.mission.discodeit.repository.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//
//
//public class JCFUserRepository implements UserRepository {
//
//    private final List<User> userList = new ArrayList<>();
//    private static JCFUserRepository instance = new JCFUserRepository();
//
//    private JCFUserRepository() {}
//
//    public static JCFUserRepository getInstance()
//    {
//        return instance;
//    }
//
//    public void addUser(User user){
//        userList.add(user);
//    }
//
//    public void removeUser(User user){
//        userList.remove(user);
//    }
//    public List<User> findAll(){
//        return userList;
//    }
//
//    public void updateName(User user, String name){
//        user.setName(name);
//        user.setUpdateAt();
//    }
//    public void updateNickname(User user, String nickname){
//        user.setNickName(nickname);
//        user.setUpdateAt();
//    }
//    public void updateEmail(User user, String email){
//        user.setEmail(email);
//        user.setUpdateAt();
//    }
//
//    public User readId(UUID id){
//        for(User user : userList){
//            if(user.getId().equals(id)){
//                return user;
//            }
//        }
//        return null;
//    }
//
//}
