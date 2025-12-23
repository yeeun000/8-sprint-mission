package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatus create(ReadStatusDTO readStatusDTO) {
        UUID userId = userRepository.findById(readStatusDTO.userId())
                .orElseThrow(() -> new NoSuchElementException(" 유저를 찾을 수 없습니다."))
                .getId();
        UUID channelId = channelRepository.findById(readStatusDTO.channelId())
                .orElseThrow(() -> new NoSuchElementException(" 채널을 찾을 수 없습니다."))
                .getId();

        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
            throw new IllegalArgumentException("이미 있습니다.");
        }

        Instant lastReadAt = readStatusDTO.lastRead();
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(" readStatus를 찾을 수 없습니다."));
    }

    @Override
    public void findAllByUserId(UUID userId) {
        readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void update(UpdateReadStatusRequest readStatusUpdateDTO) {
        Instant lastReadAt = readStatusUpdateDTO.lastRead();
        ReadStatus readStatus = find(readStatusUpdateDTO.id());
        readStatus.update(lastReadAt);
        readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        find(id);
        readStatusRepository.deleteById(id);
    }
}
