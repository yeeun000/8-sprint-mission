package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;

import java.util.List;
import java.util.UUID;

public class BasicBinaryContentService implements BinaryContentService {
    private BinaryContentRepository binaryContentRepository;

    public void create(BinaryContentDTO binaryContentDTO) {
        BinaryContent content = new BinaryContent(binaryContentDTO.id(), binaryContentDTO.fileName(), binaryContentDTO.filePath());
        binaryContentRepository.add(content);
    }

    public BinaryContent find(UUID id) {
        return binaryContentRepository.find(id);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAll(ids);
    }

    public void delete(UUID id) {
        binaryContentRepository.remove(id);
    }
}
