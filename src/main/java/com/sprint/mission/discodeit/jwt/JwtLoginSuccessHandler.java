package com.sprint.mission.discodeit.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.exception.auth.AuthException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtTokenProvider tokenProvider;
  private final JwtRegistry jwtRegistry;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    if (authentication.getPrincipal() instanceof DiscodeitUserDetails discodeitUserDetails) {
      try {
        jwtRegistry.invalidateJwtInformationByUserId(discodeitUserDetails.getUser().id());

        String accessToken = tokenProvider.generateAccessToken(discodeitUserDetails);
        String refreshToken = tokenProvider.generateRefreshToken(discodeitUserDetails);

        JwtInformation jwtInfo = new JwtInformation(discodeitUserDetails.getUser(), accessToken, refreshToken);
        jwtRegistry.registerJwtInformation(jwtInfo);

        tokenProvider.addRefreshCookie(response, refreshToken);

        UserDto userDto = discodeitUserDetails.getUser();

        JwtDto jwtDto = new JwtDto(userDto, accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(jwtDto));
      } catch (JOSEException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            new AuthException(ErrorCode.TOKEN_GENERATION_FAILED),
            HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      }
    } else {
      ErrorResponse errorResponse = new ErrorResponse(
          new AuthException(ErrorCode.AUTHENTICATION_FAILED),
          HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
  }
}
