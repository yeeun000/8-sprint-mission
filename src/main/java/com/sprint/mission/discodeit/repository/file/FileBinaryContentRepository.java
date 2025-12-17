package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Repository
public class FileBinaryContentRepository extends FileRepository<BinaryContent> implements BinaryContentRepository {

    public FileBinaryContentRepository(String filePath) {
        super(filePath,"binaryContent.ser");
    }

    @Override
    public void add(BinaryContent binaryContent) {
        getFile().put(binaryContent.getId(), binaryContent);
        saveFile();
    }

    @Override
    public void removeProfile(UUID userid) {
        UUID id = null;
        for (BinaryContent content : getFile().values()) {
            if (content.getUserId().equals(userid) && content.isProfile()) {
                id = content.getId();
                break;
            }
        }
        if (id != null)
            getFile().remove(id);
    }

    @Override
    public void remove(UUID messageId) {
        List<UUID> id = new ArrayList<>();
        for (BinaryContent content : getFile().values()) {
            if (content.getMessageId().equals(messageId)) {
                id.add(content.getId());
            }
        }
        if (!id.isEmpty()) {
            for (UUID contentid : id) {
                getFile().remove(contentid);
            }
        }
    }

    @Override
    public BinaryContent find(UUID id) {
        return getFile().get(id);
    }

    @Override
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
