package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusDTO readStatusDTO);

    ReadStatus find(UUID id);

    void findALlByUserId(UUID userId);

    void update(ReadStatusUpdateDTO readStatusUpdateDTO);

    void delete(UUID id);
}
