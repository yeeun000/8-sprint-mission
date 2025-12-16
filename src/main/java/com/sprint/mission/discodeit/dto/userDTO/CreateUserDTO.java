package com.sprint.mission.discodeit.dto.userDTO;

import com.sprint.mission.discodeit.dto.binaryContentDTO.ProfileDTO;

import java.util.Optional;
import java.util.UUID;

public record CreateUserDTO(
        String name,
        String password,
        String email,
        Optional<ProfileDTO> profileImage) {
}
