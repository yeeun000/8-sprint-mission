package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.userDTO.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  public UserController(UserService userService, UserStatusService userStatusService) {
    this.userService = userService;
    this.userStatusService = userStatusService;
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<User> register(
      @RequestPart("userCreateRequest") String userCreateRequestJson,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    UserCreateRequest userCreateRequest = objectMapper.readValue(userCreateRequestJson,
        UserCreateRequest.class);

    BinaryContentDTO binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentDTO(
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
      @PathVariable UUID userId,
      @RequestPart(value = "updateUserRequest", required = false) String updateUserRequestJson,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
    
    ObjectMapper objectMapper = new ObjectMapper();
    UserUpdateRequest userUpdateRequest;

    if (updateUserRequestJson != null && !updateUserRequestJson.isBlank()) {
      userUpdateRequest = objectMapper.readValue(updateUserRequestJson, UserUpdateRequest.class);
    } else {
      userUpdateRequest = new UserUpdateRequest(null, null, null);
    }

    BinaryContentDTO binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentDTO(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getBytes()
      );
    }
    User user = userService.update(userId, userUpdateRequest, binaryContentDTO);
    return ResponseEntity.ok(user);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(users);
  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatus> updateUserStatusByUserId(@PathVariable UUID userId,
      @RequestBody UpdateUserStatusRequest request) {
    UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedUserStatus);
  }
}
