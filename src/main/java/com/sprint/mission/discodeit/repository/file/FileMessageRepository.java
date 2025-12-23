package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository extends FileRepository<Message> implements MessageRepository {

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory}") String filePath
    ) {
        super(filePath, "message.ser");
    }

    @Override
    public Message save(Message message) {
        getFile().put(message.getId(), message);
        saveFile();
        return message;
    }

    @Override
    public List<Message> findAll() {
        return getFile().values().stream().toList();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(getFile().get(id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
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
                .forEach(message -> deleteById(message.getId()));
    }

}
