package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest readStatusRequest);

  ReadStatusDto find(UUID id);

  List<ReadStatusDto> findAllByUserId(UUID userId);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest updateReadStatusRequest);

  void delete(UUID id);
}
