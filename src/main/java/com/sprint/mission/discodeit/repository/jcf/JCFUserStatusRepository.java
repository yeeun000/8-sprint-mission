package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {

  private Map<UUID, UserStatus> statusList = new HashMap<>();

  private UserRepository userRepository;

  public JCFUserStatusRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserStatus save(UserStatus status) {
    statusList.put(status.getId(), status);
    return status;
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(statusList.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return findAll().stream()
        .filter(status -> status.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return statusList.values().stream().toList();
  }

  @Override
  public void deleteById(UUID id) {
    statusList.remove(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    findByUserId(userId).ifPresent(status -> deleteById(status.getId()));
  }

}
