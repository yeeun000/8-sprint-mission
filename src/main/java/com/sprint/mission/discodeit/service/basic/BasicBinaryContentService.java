package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateRequest binaryContentRequest) {

    log.debug("파일 생성 요청 - fileName: {}, contentType: {}, size: {}",
        binaryContentRequest.fileName(),
        binaryContentRequest.contentType(),
        binaryContentRequest.bytes().length);

    BinaryContent binaryContent = new BinaryContent(
        binaryContentRequest.fileName(),
        (long) binaryContentRequest.bytes().length,
        binaryContentRequest.contentType()
    );
    byte[] bytes = binaryContentRequest.bytes();

    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), bytes);

    log.info("파일 생성 완료 - id: {}", binaryContent.getId());

    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto find(UUID id) {
    return binaryContentRepository.findById(id)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> new BinaryContentNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllByIdIn(List<UUID> id) {
    return binaryContentRepository.findAllById(id)
        .stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {

    log.debug("파일 삭제 요청 - id: {}", id);

    if (!binaryContentRepository.existsById(id)) {
      log.warn("파일 삭제 실패 - id: {}", id);
      throw new BinaryContentNotFoundException(id);
    }
    binaryContentRepository.deleteById(id);

    log.info("파일 삭제 완료 - id: {}", id);
  }
}
