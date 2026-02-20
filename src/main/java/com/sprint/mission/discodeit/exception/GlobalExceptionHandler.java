package com.sprint.mission.discodeit.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 사용자 도메인 예외 처리
  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    log.warn("커스텀 예외 처리 : code={}, message={}", e.getErrorCode(), e.getMessage(), e);
    ErrorResponse response = ErrorResponse.of(e);
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(response);
  }

  // 검증 실패 처리
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    log.warn("검증 실패 오류 발생 : {}", e.getMessage());
    List<String> errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    return ResponseEntity.badRequest()
        .body(ErrorResponse.validationFailed(errors));
  }

  // 예상치 못한 시스템 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
    log.error("예상치 못한 오류 발생 : {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.systemError());
  }
}

