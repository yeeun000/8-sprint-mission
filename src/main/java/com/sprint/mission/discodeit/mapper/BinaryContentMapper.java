package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent entity) {
    if (entity == null) {
      return null;
    }

    // API 문서의 /api/binaryContents/{binaryContentId}/download 경로와 일치해야 함
    String downloadUrl = "http://localhost:8080/api/binaryContents/" + entity.getId() + "/download";

    return new BinaryContentDto(
        entity.getId(),
        entity.getFileName(),
        entity.getSize(),
        entity.getContentType(),
        downloadUrl // 프론트엔드가 이 URL을 <img src="...">에 사용함
    );
  }
}
