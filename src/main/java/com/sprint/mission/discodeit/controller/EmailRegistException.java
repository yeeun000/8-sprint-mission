package com.sprint.mission.discodeit.controller;

public class EmailRegistException extends RuntimeException {

  public EmailRegistException(String message) {
    super(message + " 이메일은 이미 있습니다.");
  }
}
