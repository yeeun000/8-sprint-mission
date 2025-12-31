package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public User login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new NoSuchElementException("아이디가 틀렸습니다."));

    if (loginRequest.password().equals(user.getPassword())) {
      return user;
    } else {
      throw new NoSuchElementException("비밀번호가 틀렸습니다.");
    }
  }

}
