package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final Map<UUID, User> userList = new HashMap<>();
    private final File userFile = new File("src/main/java/com/sprint/mission/discodeit/service/data/user.ser");

    private static FileUserRepository instance = new FileUserRepository();

    private FileUserRepository() {
        loadFromFile();
    }

    public static FileUserRepository getInstance()
    {
        return instance;
    }

    private void loadFromFile() {
        if (!userFile.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
            Map<UUID, User> list = (Map<UUID,User>) ois.readObject();
            userList.putAll(list);
        } catch (Exception e) {}
    }

    public void add(User user) {
        userList.put(user.getId(),user);
        saveFile();
    }

    public List<User> findAll() {
        return userList.values().stream().toList();
    }

    public User findId(UUID userid){
        boolean find = userList.containsKey(userid);
        if(find) {
            saveFile();
            return userList.get(userid);
        }
        else return null;
    }

    public void remove(UUID userId) {
        userList.remove(userId);
        saveFile();
    }

    private void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(userFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userList);
        } catch (IOException e) {
            System.out.println();
        }
    }
}
