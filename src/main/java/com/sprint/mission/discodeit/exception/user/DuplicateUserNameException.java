package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class DuplicateUserNameException extends UserException {

  public DuplicateUserNameException(String username) {
    super(ErrorCode.DUPLICATE_USERNAME, Map.of("username", username));
  }

}
