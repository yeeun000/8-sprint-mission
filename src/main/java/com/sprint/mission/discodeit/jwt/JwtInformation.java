package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;

public record JwtInformation(
    UserDto userDto,
    String accessToken,
    String refreshToken
) {

  public JwtInformation rotate(String newAccess, String newRefresh) {
    return new JwtInformation(this.userDto, newAccess, newRefresh);
  }
}
