package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileBinaryContentRepository extends FileRepository<BinaryContent> implements BinaryContentRepository {

    private FileBinaryContentRepository() {
        super("src/main/java/com/sprint/mission/discodeit/service/data/binaryContent.ser");
    }


    public void add(BinaryContent binaryContent) {
        getFile().put(binaryContent.getId(), binaryContent);
        saveFile();
    }

    public void remove(UUID id) {
        getFile().remove(id);
        saveFile();
    }

    public BinaryContent find(UUID id) {
        return getFile().get(id);
    }

    public List<BinaryContent> findAll(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            if (getFile().containsKey(id)) {
                result.add(getFile().get(id));
            }
        }
        return result;
    }
}
