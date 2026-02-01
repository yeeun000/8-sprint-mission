package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @RequestBody ReadStatusCreateRequest readStatusDTO) {
    ReadStatusDto readStatus = readStatusService.create(readStatusDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(@PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest updateReadStatusRequest) {
    ReadStatusDto readStatus = readStatusService.update(readStatusId, updateReadStatusRequest);
    return ResponseEntity.ok(readStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }
}
