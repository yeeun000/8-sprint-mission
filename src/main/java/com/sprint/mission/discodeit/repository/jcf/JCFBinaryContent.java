package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFBinaryContent implements BinaryContentRepository {
    private Map<UUID, BinaryContent> contentList = new HashMap<>();

    public void add(BinaryContent binaryContent){
        contentList.put(binaryContent.getId(), binaryContent);
    }

    public void remove(UUID id){
        contentList.remove(id);
    }
}
