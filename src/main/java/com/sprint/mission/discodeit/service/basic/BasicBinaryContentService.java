package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest binaryContentDTO) {
    BinaryContent binaryContent = new BinaryContent(
        binaryContentDTO.fileName(),
        (long) binaryContentDTO.bytes().length,
        binaryContentDTO.contentType(),
        binaryContentDTO.bytes()
    );
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 파일을 찾을 수 없습니다."));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> id) {
    return binaryContentRepository.findAllByIdIn(id);
  }

  @Override
  public void delete(UUID id) {
    find(id);
    binaryContentRepository.deleteById(id);
  }
}
