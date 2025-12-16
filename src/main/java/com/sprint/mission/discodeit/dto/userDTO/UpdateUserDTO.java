package com.sprint.mission.discodeit.dto.userDTO;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;

import java.util.Optional;
import java.util.UUID;

public record UpdateUserDTO(
        UUID id,
        String name,
        String password,
        String email,
        Optional<BinaryContentDTO> profileImage
) {
}
