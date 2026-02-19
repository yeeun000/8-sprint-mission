package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class PasswordNotFoundException extends UserException {

  public PasswordNotFoundException(String username) {
    super(ErrorCode.PASSWORD_NOT_FOUND, Map.of("username", username));
  }

}
