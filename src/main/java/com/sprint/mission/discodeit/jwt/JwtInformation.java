package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtInformation {

  private UserDto userDto;
  private String accessToken;
  private String refreshToken;

  public void rotate(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public void updateUser(UserDto userDto) {
    this.userDto = userDto;
  }

}
