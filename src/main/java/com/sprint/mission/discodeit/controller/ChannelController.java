package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public Channel createPublicChannel(@RequestBody PublicChannelDTO publicChannelDTO) {
    return channelService.create(publicChannelDTO);
  }

  @PostMapping(value = "/private")
  public Channel createPrivateChannel(@RequestBody PrivateChannelDTO privateChannelDTO) {
    return channelService.create(privateChannelDTO);
  }

  @PatchMapping(value = "/{channelId}")
  public Channel updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest) {
    return channelService.update(channelId, publicChannelUpdateRequest);
  }

  @DeleteMapping(value = "/{channelId}")
  public void delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
  }


  @GetMapping
  public List<ChannelDTO> findAllByUserId(@RequestParam UUID userId) {
    return channelService.findAllByUserId(userId);
  }
}
