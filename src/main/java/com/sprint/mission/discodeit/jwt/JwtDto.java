package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;

public record JwtDto(
    UserDto userDto,
    String accessToken
) {

}
