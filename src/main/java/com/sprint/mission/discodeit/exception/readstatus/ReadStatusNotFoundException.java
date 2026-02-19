package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageException;
import java.util.Map;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(UUID messageId) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, Map.of("messageId", messageId));
  }

}
