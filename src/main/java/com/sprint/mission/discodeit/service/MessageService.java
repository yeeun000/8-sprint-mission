package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.messageDTO.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageRequest createMessageDTO, List<BinaryContentDTO> binaryContentDTO);

    List<Message> findAllByChannelId(UUID channelId);

    void delete(UUID id);

    Message update(UpdateMessageRequest updateMessageDTO);

    Message find(UUID id);

}
