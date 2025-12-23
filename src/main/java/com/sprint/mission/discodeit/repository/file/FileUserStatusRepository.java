package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileUserStatusRepository extends FileRepository<UserStatus> implements UserStatusRepository {

    private UserRepository userRepository;

    public FileUserStatusRepository(
            @Value("${discodeit.repository.file-directory}") String filePath,
            UserRepository userRepository) {
        super(filePath, "UserStatus.ser");
        this.userRepository = userRepository;
    }


    @Override
    public UserStatus save(UserStatus status) {
        getFile().put(status.getId(), status);
        saveFile();
        return status;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(getFile().get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return getFile().values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        getFile().remove(id);
        saveFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findByUserId(userId).ifPresent(status -> deleteById(status.getId()));
    }
}
