package com.sprint.mission.discodeit.dto.userDTO;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import java.util.UUID;


public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
