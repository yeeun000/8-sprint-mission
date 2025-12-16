package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void add(BinaryContent binaryContent);

    void remove(UUID id);

    void removeProfile(UUID id);

    List<BinaryContent> findAll(List<UUID> ids);

    BinaryContent find(UUID id);
}
