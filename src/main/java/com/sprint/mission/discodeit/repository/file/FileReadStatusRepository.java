package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository extends FileRepository<ReadStatus> implements ReadStatusRepository {

    public FileReadStatusRepository(
            @Value("${discodeit.repository.file-directory}") String filePath
    ) {
        super(filePath, "readStatus.ser");
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        getFile().put(readStatus.getId(), readStatus);
        saveFile();
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(getFile().get(id));
    }


    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return getFile().values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return getFile().values().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        getFile().remove(id);
        saveFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(readStatus -> this.deleteById(readStatus.getId()));
    }
}
