package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    BinaryContentCreateRequest binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentCreateRequest(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getBytes()
      );
    }

    User user = userService.create(userCreateRequest, binaryContentDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
  public ResponseEntity<User> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    BinaryContentCreateRequest binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentCreateRequest(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getBytes()
      );
    }
    User user = userService.update(userId, userUpdateRequest, binaryContentDTO);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateStatus(@PathVariable("userId") UUID userId,
      @RequestBody UserStatusUpdateRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.ok(updatedUserStatus);
  }
}
