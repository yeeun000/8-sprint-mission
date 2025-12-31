package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  public ReadStatusController(ReadStatusService readStatusService) {
    this.readStatusService = readStatusService;
  }

  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusDTO readStatusDTO) {
    ReadStatus readStatus = readStatusService.create(readStatusDTO);
    return ResponseEntity.ok(readStatus);
  }

  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId,
      @RequestBody UpdateReadStatusRequest updateReadStatusRequest) {
    ReadStatus readStatus = readStatusService.update(readStatusId, updateReadStatusRequest);
    return ResponseEntity.ok(readStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }
}
