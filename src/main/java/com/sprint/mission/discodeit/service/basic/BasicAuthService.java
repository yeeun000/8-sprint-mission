package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BasicAuthService implements AuthService {

    private UserRepository userRepository;

    public BasicAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(LoginDTO loginDTO) {
        User user = userRepository.findName(loginDTO.username());
        if (user == null)
            throw new NoSuchElementException(loginDTO.username() + "회원 정보가 없습니다..");

        if (loginDTO.password().equals(user.getPassword()))
            return user;
        else throw new NoSuchElementException(loginDTO.username() + "비밀번호가 틀렸습니다.");
    }

}
