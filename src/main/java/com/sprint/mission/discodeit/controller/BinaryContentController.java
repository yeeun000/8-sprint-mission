package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Validated
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto bc = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(bc);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentDto> bc = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(bc);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    log.info("파일 다운로드 시작 - fileId: {}", binaryContentId);
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
    return binaryContentStorage.download(binaryContentDto);
  }
}
