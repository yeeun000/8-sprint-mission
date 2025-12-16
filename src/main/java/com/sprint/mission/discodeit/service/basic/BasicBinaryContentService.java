package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicBinaryContentService implements BinaryContentService {
    private BinaryContentRepository binaryContentRepository;

    public BasicBinaryContentService(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    public void create(BinaryContentDTO binaryContentDTO) {
        BinaryContent binaryContent = find(binaryContentDTO.id());
        binaryContentRepository.add(binaryContent);
    }

    public BinaryContent find(UUID id) {
        BinaryContent bc = binaryContentRepository.find(id);
        if (bc == null)
            throw new NoSuchElementException(bc.getFileName() + "를 찾을 수 없습니다.");
        return bc;
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAll(ids);
    }

    public void delete(UUID id) {
        binaryContentRepository.remove(id);
    }
}
