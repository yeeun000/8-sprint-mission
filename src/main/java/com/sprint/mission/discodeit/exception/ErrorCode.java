package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("USER_NOT_FOUND", "유저를 찾을 수 없습니다.", 404),
  DUPLICATE_USERNAME("DUPLICATE_USERNAME", "이미 존재하는 아이디입니다.", 400),
  DUPLICATE_EMAIL("DUPLICATE_EMAIL", "이미 존재하는 이메일입니다.", 400),
  USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "아이디가 틀렸습니다.", 400),
  PASSWORD_NOT_FOUND("PASSWORD_NOT_FOUND", "비밀번호가 일치하지 않습니다.", 400),
  BINARY_CONTENT_NOT_FOUND("BINARY_CONTENT_NOT_FOUND", "파일을 찾을 수 없습니다.", 404),
  CHANNEL_NOT_FOUND("CHANNEL_NOT_FOUND", "채널을 찾을 수 없습니다.", 404),
  PRIVATE_CHANNEL_UPDATE("PRIVATE_CHANNEL_UPDATE", "PRIVATE 채널은 수정할 수 없습니다.", 400),
  MESSAGE_NOT_FOUND("MESSAGE_NOT_FOUND", "메시지를 찾을 수 없습니다.", 404),
  READ_STATUS_NOT_FOUND("READ_STATUS_NOT_FOUND", "읽은 상태 정보를 찾을 수 없습니다.", 404),
  USER_STATUS_NOT_FOUND("USER_STATUS_NOT_FOUND", "유저 상태 정보를 찾을 수 없습니다.", 404),
  USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 존재하는 유저입니다.", 400);

  private final String message;
  private final String code;
  private final int status;

  ErrorCode(String code, String message, int status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
