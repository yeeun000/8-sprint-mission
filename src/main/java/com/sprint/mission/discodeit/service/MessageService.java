package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(CreateMessageDTO createMessageDTO);

    List<Message> findallByChannelId(UUID channelId);

    void delete(UUID messageId);

    Message update(MessageDTO messageDTO);

    Message findId(UUID messageId);

}
