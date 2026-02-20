package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;

  private ErrorResponse(String code, String message, Map<String, Object> details,
      String exceptionType, int status) {
    this.timestamp = Instant.now();
    this.code = code;
    this.message = message;
    this.details = details != null ? details : new HashMap<>();
    this.exceptionType = exceptionType;
    this.status = status;
  }

  public static ErrorResponse of(DiscodeitException e) {
    return new ErrorResponse(
        e.getErrorCode().getCode(),
        e.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getErrorCode().getStatus()
    );
  }

  public static ErrorResponse validationFailed(List<String> errors) {

    return new ErrorResponse(
        "VALIDATION_FAILED",
        "입력값 검증에 실패했습니다.",
        Map.of("validationErrors", errors),
        "ValidationException",
        400
    );
  }

  public static ErrorResponse systemError() {

    return new ErrorResponse(
        "INTERNAL_SERVER_ERROR",
        "일시적인 서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
        Map.of(),
        "SystemException",
        500
    );
  }

}
