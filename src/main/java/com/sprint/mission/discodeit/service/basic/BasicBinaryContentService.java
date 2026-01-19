package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateRequest binaryContentRequest) {
    BinaryContent binaryContent = new BinaryContent(
        binaryContentRequest.fileName(),
        (long) binaryContentRequest.bytes().length,
        binaryContentRequest.contentType()
    );
    return binaryContentMapper.toDto(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentDto find(UUID id) {
    return binaryContentMapper.toDto(binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(" 파일을 찾을 수 없습니다.")));
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> id) {
    return binaryContentRepository.findAllByIdIn(id)
        .stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    find(id);
    binaryContentRepository.deleteById(id);
  }
}
