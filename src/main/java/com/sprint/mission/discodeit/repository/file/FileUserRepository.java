package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserRepository extends FileRepository<User> implements UserRepository {

  public FileUserRepository(
      @Value("${discodeit.repository.file-directory}") String filePath
  ) {
    super(filePath, "user.ser");
  }

  @Override
  public User save(User user) {
    getFile().put(user.getId(), user);
    saveFile();
    return user;
  }

  @Override
  public List<User> findAll() {
    return getFile().values().stream().toList();
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(getFile().get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return findAll().stream()
        .filter(user -> user.getName() != null && user.getName().equals(username))
        .findFirst();
  }

  @Override
  public void deleteById(UUID id) {
    getFile().remove(id);
    saveFile();
  }

  @Override
  public boolean existsName(String name) {
    return findAll().stream()
        .anyMatch(user -> user.getName() != null && user.getName().equals(name));
  }

  @Override
  public boolean existsEmail(String email) {
    return findAll().stream()
        .anyMatch(user -> user.getEmail() != null && user.getEmail().equals(email));
  }
}
