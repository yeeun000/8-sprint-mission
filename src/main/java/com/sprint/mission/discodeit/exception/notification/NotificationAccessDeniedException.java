package com.sprint.mission.discodeit.exception.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class NotificationAccessDeniedException extends NotificationException {

  public NotificationAccessDeniedException() {
    super(ErrorCode.Notification_NOT_FOUND);
  }

  public static NotificationAccessDeniedException withId(UUID notificationId) {
    NotificationAccessDeniedException exception = new NotificationAccessDeniedException();
    exception.addDetail("notificationId", notificationId);
    return exception;
  }
}
