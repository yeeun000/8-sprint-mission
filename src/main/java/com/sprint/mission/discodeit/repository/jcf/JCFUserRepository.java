package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList = new HashMap<>();

    @Override
    public User save(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userList.values().stream().toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userList.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        userList.remove(id);
    }

    @Override
    public boolean existsName(String name) {
        return findAll().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public boolean existsEmail(String email) {
        return findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

}
