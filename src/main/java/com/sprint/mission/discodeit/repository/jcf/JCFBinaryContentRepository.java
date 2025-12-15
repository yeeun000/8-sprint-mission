package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private Map<UUID, BinaryContent> contentList = new HashMap<>();

    public void add(BinaryContent binaryContent) {
        contentList.put(binaryContent.getId(), binaryContent);
    }

    public void remove(UUID id) {
        contentList.remove(id);
    }

    public BinaryContent find(UUID id) {
        return contentList.get(id);
    }

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
