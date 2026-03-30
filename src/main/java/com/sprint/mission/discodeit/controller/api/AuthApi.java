package com.sprint.mission.discodeit.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;

@Tag(name = "Auth", description = "인증 API")
public interface AuthApi {

  @Operation(
      summary = "CSRF 토큰 조회",
      description = "CSRF 보호를 위한 토큰을 반환합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공"
      )
  })
  ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);

} 