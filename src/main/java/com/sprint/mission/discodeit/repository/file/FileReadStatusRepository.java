package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileReadStatusRepository extends FileRepository<ReadStatus> implements ReadStatusRepository {

    public FileReadStatusRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/readStatus.ser");
    }

    @Override
    public void add(ReadStatus readStatus) {
        getFile().put(readStatus.getId(), readStatus);
        saveFile();
    }

    @Override
    public ReadStatus find(UUID id) {
        return getFile().get(id);
    }

    @Override
    public List<ReadStatus> findAll(UUID userId) {
        List<ReadStatus> findUser = new ArrayList<>();
        for (ReadStatus re : getFile().values()) {
            if (re.getUserId().equals(userId)) {
                findUser.add(re);
            }
        }
        return findUser;
    }

    @Override
    public void remove(UUID id) {
        getFile().remove(id);
        saveFile();
    }

    @Override
    public boolean exists(UUID userId, UUID channelId) {
        for (ReadStatus read : getFile().values()) {
            if (read.getUserId().equals(userId) && read.getChannelId().equals(channelId))
                return true;
        }
        return false;
    }
}
