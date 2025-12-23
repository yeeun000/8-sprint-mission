package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.messageDTO.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageDTO createMessageDTO, List<BinaryContentDTO> binaryContentDTO);

    List<Message> findAllByChannelId(UUID channelId);

    void delete(UUID id);

    Message update(UpdateMessageDTO updateMessageDTO);

    Message find(UUID id);

}
