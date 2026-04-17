package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.jwt.JwtDto;
import com.sprint.mission.discodeit.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final DiscodeitUserDetailsService discodeitUserDetailsService;

  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
  }

  @PutMapping("role")
  public ResponseEntity<UserDto> updateRole(
      @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
    try {
      UserDto userDto = userService.updateUserRole(userRoleUpdateRequest);
      return ResponseEntity.ok(userDto);
    } catch (UserNotFoundException e) {
      log.warn("사용자 없음: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      log.warn("사용자 역할 변경 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping("refresh")
  public ResponseEntity<JwtDto> refresh(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = extractRefreshToken(request);

    if (refreshToken == null || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
      log.warn("refresh token 없음");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) discodeitUserDetailsService.loadUserByUserId(
        UUID.fromString(userId));

    try {
      String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
      String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

      jwtTokenProvider.addRefreshCookie(response, newRefreshToken);

      UserDto userDto = userDetails.getUser();
      JwtDto jwtDto = new JwtDto(userDto, newAccessToken);
      return ResponseEntity.ok(jwtDto);

    } catch (UserNotFoundException e) {
      log.warn("사용자 없음: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (IllegalArgumentException e) {
      log.warn("잘못된 토큰: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (Exception e) {
      log.warn("refresh token 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  private String extractRefreshToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      return null;
    }

    for (Cookie cookie : cookies) {
      if (JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }

    return null;
  }

}
