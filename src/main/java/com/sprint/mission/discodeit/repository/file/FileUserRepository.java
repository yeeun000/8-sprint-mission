package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class FileUserRepository extends FileRepository<User> implements UserRepository {


    private static FileUserRepository instance = new FileUserRepository();

    private FileUserRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/user.ser");
    }

    public static FileUserRepository getInstance() {
        return instance;
    }


    public void add(User user) {
        getFile().put(user.getId(), user);
        saveFile();
    }

    public List<User> findAll() {
        return getFile().values().stream().toList();
    }

    public User findId(UUID userid) {
        boolean find = getFile().containsKey(userid);
        if (find) {
            saveFile();
            return getFile().get(userid);
        } else return null;
    }

    public void remove(UUID userId) {
        getFile().remove(userId);
        saveFile();
    }

    public boolean existsName(String name) {
        for (User user : getFile().values()) {
            if (name.equals(user.getName()))
                return true;
        }
        return false;
    }

    public boolean existsEmail(String email) {
        for (User user : getFile().values()) {
            if (email.equals(user.getEmail()))
                return true;
        }
        return false;
    }

    public User login(String name) {
        for (User user : getFile().values()) {
            if (name.equals(user.getName()))
                return user;
        }
        return null;
    }

}
