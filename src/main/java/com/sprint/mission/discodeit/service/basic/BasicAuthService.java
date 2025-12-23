package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.username())
                .orElseThrow(() -> new NoSuchElementException("아이디가 틀렸습니다."));

        if (loginDTO.password().equals(user.getPassword()))
            return user;
        else throw new NoSuchElementException("비밀번호가 틀렸습니다.");
    }

}
