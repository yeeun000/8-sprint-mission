package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public Channel createPublicChannel(@RequestBody PublicChannelDTO publicChannelDTO) {
        return channelService.create(publicChannelDTO);
    }

    @RequestMapping(value = "/channel/private", method = RequestMethod.POST)
    public Channel createPrivateChannel(@RequestBody PrivateChannelDTO privateChannelDTO) {
        return channelService.create(privateChannelDTO);
    }

    @RequestMapping(value = "/channel/{id}", method = RequestMethod.PUT)
    public Channel updatePublicChannel(@PathVariable UUID id, @RequestBody PublicChannelDTO publicChannelDTO) {
        return channelService.update(id, publicChannelDTO);
    }

    @RequestMapping(value = "/channel/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        channelService.delete(id);
    }

    @RequestMapping(value = "/channel", method = RequestMethod.GET)
    public List<ChannelDTO> findAllByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}
