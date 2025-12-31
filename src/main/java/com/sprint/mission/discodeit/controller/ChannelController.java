package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
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
public class ChannelController {

  private final ChannelService channelService;

  public ChannelController(ChannelService channelService) {
    this.channelService = channelService;
  }

  @PostMapping(value = "/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelDTO publicChannelDTO) {
    Channel channel = channelService.create(publicChannelDTO);
    return ResponseEntity.ok(channel);
  }

  @PostMapping(value = "/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelDTO privateChannelDTO) {
    Channel channel = channelService.create(privateChannelDTO);
    return ResponseEntity.ok(channel);
  }

  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<Channel> updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelService.update(channelId, publicChannelUpdateRequest);
    return ResponseEntity.ok(channel);
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok().build();
  }


  @GetMapping
  public ResponseEntity<List<ChannelDTO>> findAllByUserId(@RequestParam UUID userId) {
    List<ChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}
