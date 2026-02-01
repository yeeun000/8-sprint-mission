package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    User user = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new IllegalArgumentException("아이디가 틀렸습니다."));

    if (loginRequest.password().equals(user.getPassword())) {
      UserStatus status = userStatusRepository.findByUserId(user.getId())
          .orElseGet(() -> {
            UserStatus newStatus = new UserStatus(user, Instant.now());
            return userStatusRepository.save(newStatus);
          });

      status.update(Instant.now());
      userStatusRepository.save(status);
      return userMapper.toDto(user);
    } else {
      throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }
  }

}
