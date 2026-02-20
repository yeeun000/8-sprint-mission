package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.PasswordNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNameNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new UserNameNotFoundException(loginRequest.username()));

    if (!loginRequest.password().equals(user.getPassword())) {
      throw new PasswordNotFoundException(loginRequest.username());
    }
    return userMapper.toDto(user);
  }

}
