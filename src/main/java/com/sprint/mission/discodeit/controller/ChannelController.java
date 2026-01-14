package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/channels")
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  public ChannelController(ChannelService channelService) {
    this.channelService = channelService;
  }

  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(
      @RequestBody PublicChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(channel);
  }

  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.ok(channel);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}
