package com.sprint.mission.discodeit.controller;

//import com.sprint.mission.discodeit.controller.api.BinaryContentApi;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
//public class BinaryContentController implements BinaryContentApi {
public class BinaryContentController {


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

//  @GetMapping("/{binaryContentId}/download")
//  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
//    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
//    return binaryContentStorage.download(binaryContentDto);
//  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    // 1. DB에서 메타데이터(파일명, 컨텐츠 타입 등) 조회
    BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);

    // 2. Storage의 download 메서드 호출 (타입: ResponseEntity<Resource>)
    return binaryContentStorage.download(binaryContentDto);
  }
}
