//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class FileUserRepository implements UserRepository {
//
//    private final List<User> userList = new ArrayList<>();
//    private final File userFile = new File("data/user.ser");
//
//    private static FileUserRepository instance = new FileUserRepository();
//
//    private FileUserRepository() {
//        loadFromFile();
//    }
//
//    public static FileUserRepository getInstance()
//    {
//        return instance;
//    }
//
//    private void loadFromFile() {
//        if (!userFile.exists()) return;
//
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
//            List<User> list = (List<User>) ois.readObject();
//            userList.addAll(list);
//        } catch (Exception e) {}
//    }
//
//    public List<User> findAll() {
//        return userList;
//    }
//
//    public void addUser(User user) {
//        findAll();
//        userList.add(user);
//        save();
//    }
//
//    public void removeUser(User user) {
//        userList.remove(user);
//        save();
//    }
//
//    private void save() {
//        try (FileOutputStream fos = new FileOutputStream("data/user.ser");
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(userList);
//        } catch (IOException e) {
//            System.out.println();
//        }
//    }
//    public void updateName(User user, String name){
//        user.setName(name);
//        user.setUpdateAt();
//        save();
//    }
//    public void updateNickname(User user, String nickname){
//        user.setNickName(nickname);
//        user.setUpdateAt();
//        save();
//    }
//    public void updateEmail(User user, String email){
//        user.setEmail(email);
//        user.setUpdateAt();
//        save();
//    }
//    public User readId(UUID id){
//        for(User user : userList){
//            if(user.getId().equals(id)){
//                return user;
//            }
//        }
//        return null;
//    }
//}
