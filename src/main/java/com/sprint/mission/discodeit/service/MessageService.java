package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable);

  void delete(UUID id);

  MessageDto update(UUID messageId, MessageUpdateRequest updateMessageRequest);

  MessageDto find(UUID id);

}
