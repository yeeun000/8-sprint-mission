package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO);

  List<Message> findAllByChannelId(UUID channelId);

  void delete(UUID id);

  Message update(UUID messageId, MessageUpdateRequest updateMessageRequest);

  Message find(UUID id);

}
