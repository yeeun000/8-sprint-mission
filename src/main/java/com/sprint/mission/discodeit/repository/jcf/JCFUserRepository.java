package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList = new HashMap<>();


    @Override
    public void add(User user) {
        userList.put(user.getId(), user);
    }

    @Override
    public List<User> findAll() {
        return userList.values().stream().toList();
    }

    @Override
    public User findId(UUID userId) {
        boolean find = userList.containsKey(userId);
        if (find)
            return userList.get(userId);
        else return null;
    }

    @Override
    public void remove(UUID userId) {
        userList.remove(userId);
    }

    @Override
    public boolean existsName(String name) {
        for (User user : userList.values()) {
            if (name.equals(user.getName()))
                return true;
        }
        return false;
    }

    @Override
    public boolean existsEmail(String email) {
        for (User user : userList.values()) {
            if (email.equals(user.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public User findName(String name) {
        for (User user : userList.values()) {
            if (name.equals(user.getName()))
                return user;
        }
        return null;
    }

}
