package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicReadStatusService implements ReadStatusService {

    private ReadStatusRepository readStatusRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;

    public void create(ReadStatusDTO readStatusDTO) {
        if (userRepository.findId(readStatusDTO.userId()) == null)
            throw new NoSuchElementException(readStatusDTO.userId() + "를 찾을 수 없습니다.");
        if (channelRepository.findId(readStatusDTO.channelId()) == null)
            throw new NoSuchElementException(readStatusDTO.channelId() + "를 찾을 수 없습니다.");

        if (readStatusRepository.exists(readStatusDTO.userId(), readStatusDTO.channelId()))
            throw new IllegalArgumentException(readStatusDTO.channelId() + "이 이미 있습니다.");
    }

    public ReadStatus find(UUID id) {
        return readStatusRepository.find(id);
    }

    public void findALlByUserId(UUID userId) {
        readStatusRepository.findALL(userId);
    }

    public void update(ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatus readStatus = readStatusRepository.find(readStatusUpdateDTO.id());
        if (readStatus == null)
            throw new NoSuchElementException(readStatusUpdateDTO.id() + "를 찾을 수 없습니다.");
        readStatus.setLastRead(Instant.now());
        readStatusRepository.add(readStatus);
    }

    public void delete(UUID id) {
        readStatusRepository.remove(id);
    }
}
