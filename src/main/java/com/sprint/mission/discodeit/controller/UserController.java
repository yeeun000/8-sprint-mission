package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusDto;
import com.sprint.mission.discodeit.dto.userDTO.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") @Valid UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws IOException {

    log.info("유저 생성 시작 - userName: {}", userCreateRequest.username());
    BinaryContentCreateRequest binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentCreateRequest(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getBytes()
      );
    }

    UserDto user = userService.create(userCreateRequest, binaryContentDTO);
    log.info("유저 생성 완료");
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
  public ResponseEntity<UserDto> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") @Valid UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws IOException {

    log.info("유저 수정 시작 - userId: {}", userId);
    BinaryContentCreateRequest binaryContentDTO = null;
    if (profile != null && !profile.isEmpty()) {
      binaryContentDTO = new BinaryContentCreateRequest(
          profile.getOriginalFilename(),
          profile.getContentType(),
          profile.getBytes()
      );
    }
    UserDto user = userService.update(userId, userUpdateRequest, binaryContentDTO);
    log.info("유저 수정 완료");
    return ResponseEntity.ok(user);
  }

  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
    log.info("유저 삭제 시작 - userId: {}", userId);
    userService.delete(userId);
    log.info("유저 삭제 완료");
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> users = userService.findAll();
    return ResponseEntity.ok(users);
  }

  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateStatus(@PathVariable("userId") UUID userId,
      @RequestBody @Valid UserStatusUpdateRequest request) {
    UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
    return ResponseEntity.ok(updatedUserStatus);
  }
}
