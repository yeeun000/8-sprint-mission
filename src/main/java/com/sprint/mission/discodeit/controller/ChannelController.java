package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

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
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.ok(channel);
  }

  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}
