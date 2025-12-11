package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {

    private static BasicUserService instance;
    private UserRepository userRepository;

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static BasicUserService getInstance(UserRepository repository) {
        if (instance == null) {
            instance = new BasicUserService(repository);
        }
        return instance;
    }

    @Override
    public User create(String name, String nickname, String email) {
        User user = new User(name, nickname, email);
        userRepository.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(UUID userId) {
        if(userRepository.findId(userId)==null)
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        userRepository.remove(userId);
    }

    @Override
    public User update(UUID userId, String name, String nickname, String email) {
        User user = findId(userId);
        if(user==null)
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        user.update(name, nickname, email);
        return user;
    }

    @Override
    public User findId(UUID userId) {
        if(userRepository.findId(userId)==null)
            throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
        return userRepository.findId(userId);
    }

}
