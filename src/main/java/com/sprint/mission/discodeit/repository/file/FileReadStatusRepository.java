package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileReadStatusRepository extends FileRepository<ReadStatus> implements ReadStatusRepository {

    private FileReadStatusRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/readStatus.ser");
    }

    public void add(ReadStatus readStatus) {
        getFile().put(readStatus.getId(), readStatus);
        saveFile();
    }

    public ReadStatus find(UUID id) {
        return getFile().get(id);
    }

    public List<ReadStatus> findALL(UUID userId) {
        List<ReadStatus> findUser = new ArrayList<>();
        for (ReadStatus re : getFile().values()) {
            if (re.getUserId().equals(userId)) {
                findUser.add(re);
            }
        }
        return findUser;
    }

    public void remove(UUID id) {
        getFile().remove(id);
        saveFile();
    }

    public boolean exists(UUID userId, UUID channelId) {
        for (ReadStatus read : getFile().values()) {
            if (read.getUserId().equals(userId) && read.getChannelId().equals(channelId))
                return true;
        }
        return false;
    }
}
