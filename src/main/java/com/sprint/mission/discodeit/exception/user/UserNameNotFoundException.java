package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserNameNotFoundException extends UserException {

  public UserNameNotFoundException(String username) {
    super(ErrorCode.USERNAME_NOT_FOUND, Map.of("username", username));
  }
}
