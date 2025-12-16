package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicReadStatusService implements ReadStatusService {

    private ReadStatusRepository readStatusRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;

    public BasicReadStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.readStatusRepository = readStatusRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(ReadStatusDTO readStatusDTO) {
        if (userRepository.findId(readStatusDTO.userId()) == null)
            throw new NoSuchElementException(readStatusDTO.userId() + "를 찾을 수 없습니다.");
        if (channelRepository.findId(readStatusDTO.channelId()) == null)
            throw new NoSuchElementException(readStatusDTO.channelId() + "를 찾을 수 없습니다.");

        if (readStatusRepository.exists(readStatusDTO.userId(), readStatusDTO.channelId()))
            throw new IllegalArgumentException(readStatusDTO.channelId() + "이 이미 있습니다.");
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.find(id);
    }

    @Override
    public void findALlByUserId(UUID userId) {
        readStatusRepository.findAll(userId);
    }

    @Override
    public void update(UpdateReadStatusDTO readStatusUpdateDTO) {
        ReadStatus readStatus = readStatusRepository.find(readStatusUpdateDTO.id());
        if (readStatus == null)
            throw new NoSuchElementException(readStatusUpdateDTO.id() + "를 찾을 수 없습니다.");
        readStatus.setLastRead(Instant.now());
        readStatusRepository.add(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.remove(id);
    }
}
