package com.sprint.mission.discodeit.controller;

public class MemberRegistException extends RuntimeException {

  public MemberRegistException(String message) {
    super(message + " 이름은 이미 있습니다.");
  }
}
