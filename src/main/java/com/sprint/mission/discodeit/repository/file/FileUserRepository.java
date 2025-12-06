package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.Data.userList;

public class FileUserRepository implements UserRepository {

    private static FileUserRepository instance = new FileUserRepository();

    private FileUserRepository() {}

    public static FileUserRepository getInstance()
    {
        return instance;
    }

    public List<User> findAll() {
        return userList;
    }

    public void addUser(User user) {
        findAll();
        userList.add(user);
        save();
    }

    public void removeUser(User user) {
        userList.remove(user);
        save();
    }

    private void save() {
        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userList);
        } catch (IOException e) {
            System.out.println();
        }
    }
    public void updateName(User user, String name){
        user.setName(name);
        user.setUpdateAt();
    }
    public void updateNickname(User user, String nickname){
        user.setNickName(nickname);
        user.setUpdateAt();
    }
    public void updateEmail(User user, String email){
        user.setEmail(email);
        user.setUpdateAt();
    }
    public User readId(UUID id){
        for(User user : userList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }
}
