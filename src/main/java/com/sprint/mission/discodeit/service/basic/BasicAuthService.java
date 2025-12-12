package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;

import java.util.NoSuchElementException;

public class BasicAuthService implements AuthService {

    private UserRepository userRepository;

    public User login(String username, String password) {
        User user = userRepository.login(username);
        if (user == null)
            throw new NoSuchElementException(username + "를 찾을 수 없습니다.");

        LoginDTO loginDTO = new LoginDTO(user.getName(), user.getPassword());

        if (loginDTO.username().equals(username) && loginDTO.password().equals(password))
            return user;
        return null;
    }

}
