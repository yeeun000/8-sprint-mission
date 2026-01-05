package com.sprint.mission.discodeit.dto.userDTO;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
