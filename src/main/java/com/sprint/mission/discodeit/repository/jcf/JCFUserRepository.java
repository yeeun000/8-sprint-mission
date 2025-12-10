package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;


public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList = new HashMap<>();
    private static JCFUserRepository instance = new JCFUserRepository();

    private JCFUserRepository() {}

    public static JCFUserRepository getInstance()
    {
        return instance;
    }

    public void add(User user){
        userList.put(user.getId(),user);
    }

    public List<User> findAll(){
        return userList.values().stream().toList();
    }


    public User findId(UUID userId){
        boolean find = userList.containsKey(userId);
        if(find)
            return userList.get(userId);
        else return null;
    }

    public void remove(UUID userId){
        userList.remove(userId);
    }

}
