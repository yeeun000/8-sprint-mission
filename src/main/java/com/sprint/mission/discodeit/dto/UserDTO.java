package com.sprint.mission.discodeit.dto;

import java.util.Optional;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String password,
        String email,
        Optional<FileDTO> profileImage) {
}
