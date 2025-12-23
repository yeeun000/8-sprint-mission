package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusDTO readStatusDTO);

    ReadStatus find(UUID id);

    void findAllByUserId(UUID userId);

    void update(UpdateReadStatusDTO readStatusUpdateDTO);

    void delete(UUID id);
}
