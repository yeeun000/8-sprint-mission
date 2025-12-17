package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private Map<UUID, BinaryContent> contentList = new HashMap<>();

    @Override
    public void add(BinaryContent binaryContent) {
        contentList.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public void removeProfile(UUID userid) {
        UUID id = null;
        for (BinaryContent content : contentList.values()) {
            if (content.getUserId().equals(userid) && content.isProfile()) {
                id = content.getId();
                break;
            }
        }
        if (id != null)
            contentList.remove(id);
    }

    @Override
    public void remove(UUID messageId) {
        List<UUID> id = new ArrayList<>();
        for (BinaryContent content : contentList.values()) {
            if (content.getMessageId().equals(messageId)) {
                id.add(content.getId());
            }
        }
        if (!id.isEmpty()) {
            for (UUID contentid : id) {
                contentList.remove(contentid);
            }
        }
    }

    @Override
    public BinaryContent find(UUID id) {
        return contentList.get(id);
    }

    @Override
    public List<BinaryContent> findAll(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            if (contentList.containsKey(id)) {
                result.add(contentList.get(id));
            }
        }
        return result;
    }
}
