package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class FileMessageRepository extends FileRepository<Message> implements MessageRepository {

    private static FileMessageRepository instance = new FileMessageRepository();

    private FileMessageRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/message.ser");
    }

    public static FileMessageRepository getInstance() {
        return instance;
    }

    public void add(Message message) {
        getFile().put(message.getId(), message);
        saveFile();
    }

    public List<Message> findAll() {
        return getFile().values().stream().toList();
    }

    public Message findId(UUID messageId) {
        boolean find = getFile().containsKey(messageId);
        if (find)
            return getFile().get(messageId);
        else return null;
    }

    public void remove(UUID messageId) {
        getFile().remove(messageId);
        saveFile();
    }

    public Instant last(UUID channelId) {
        Instant a = Instant.now();
        return a;
    }

}
