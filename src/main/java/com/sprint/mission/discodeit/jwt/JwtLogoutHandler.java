package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtRegistry jwtRegistry;
  private final DiscodeitUserDetailsService userDetailsService;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    if (request.getCookies() != null) {
      Arrays.stream(request.getCookies())
          .filter(cookie -> cookie.getName().equals(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME))
          .findFirst()
          .ifPresent(cookie -> {
            String refreshToken = cookie.getValue();

            if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
              String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

              DiscodeitUserDetails userDetails =
                  (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

              UUID userId = userDetails.getUser().id();
              jwtRegistry.invalidateJwtInformationByUserId(userId);
            }
          });
    }
    jwtTokenProvider.expireRefreshCookie(response);
  }
}

