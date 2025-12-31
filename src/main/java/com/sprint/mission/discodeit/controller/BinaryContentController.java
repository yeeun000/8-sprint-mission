package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  public BinaryContentController(BinaryContentService binaryContentService) {
    this.binaryContentService = binaryContentService;
  }

  @GetMapping(value = "/{binaryContentId}")
  public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
    BinaryContent bc = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(bc);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> bc = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(bc);
  }
}
