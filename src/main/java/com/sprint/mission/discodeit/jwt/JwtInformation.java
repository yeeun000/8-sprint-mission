package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.time.Instant;

public record JwtInformation(
    UserDto userDto,
    String accessToken,
    String refreshToken,
    Instant accessTokenExpiry,
    Instant refreshTokenExpiry
) {

  public JwtInformation rotate(String newAccess, String newRefresh, Instant newAccessTokenExpiry, Instant newRefreshTokenExpiry) {
    return new JwtInformation(this.userDto, newAccess, newRefresh, newAccessTokenExpiry, newRefreshTokenExpiry);
  }

  public boolean isAccessTokenExpired() {
    return accessTokenExpiry.isBefore(Instant.now());
  }

  public boolean isRefreshTokenExpired() {
    return refreshTokenExpiry.isBefore(Instant.now());
  }
}
