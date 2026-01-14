package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus create(ReadStatusCreateRequest readStatusDTO);

  ReadStatus find(UUID id);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest updateReadStatusRequest);

  void delete(UUID id);
}
