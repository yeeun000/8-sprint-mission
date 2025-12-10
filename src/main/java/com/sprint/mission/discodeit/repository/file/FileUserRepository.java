package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class FileUserRepository extends FileRepository<User> implements UserRepository {


    private static FileUserRepository instance = new FileUserRepository();

    private FileUserRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/user.ser");
    }

    public static FileUserRepository getInstance()
    {
        return instance;
    }



    public void add(User user) {
        getFile().put(user.getId(),user);
        saveFile();
    }

    public List<User> findAll() {
        return getFile().values().stream().toList();
    }

    public User findId(UUID userid){
        boolean find = getFile().containsKey(userid);
        if(find) {
            saveFile();
            return getFile().get(userid);
        }
        else return null;
    }

    public void remove(UUID userId) {
        getFile().remove(userId);
        saveFile();
    }

}
