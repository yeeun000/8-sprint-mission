package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  @Override
  public UserDto getCurrentUserInfo(DiscodeitUserDetails discodeitUserDetails) {
    if (discodeitUserDetails == null) {
      throw new UsernameNotFoundException("유저를 찾을 수 없습니다");
    }

    UUID userId = discodeitUserDetails.getUser().id();
    User user = userRepository.findByIdWithProfile(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    return userMapper.toDto(user, isOnline(user.getId()));
  }

  private boolean isOnline(UUID userId) {
    return sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(principal -> (DiscodeitUserDetails) principal)
        .anyMatch(details -> details.getUser().id().equals(userId));
  }

}
