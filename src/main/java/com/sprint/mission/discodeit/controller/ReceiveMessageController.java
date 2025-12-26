package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ReceiveMessageController {

    private final ReadStatusService readStatusService;

    public ReceiveMessageController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(value = "/receiveMessage", method = RequestMethod.POST)
    public ReadStatus createReceiveMessage(@RequestBody ReadStatusDTO readStatusDTO) {
        return readStatusService.create(readStatusDTO);
    }

    @RequestMapping(value = "/receiveMessage", method = RequestMethod.PUT)
    public void updateReceiveMessage(@RequestBody UpdateReadStatusRequest updateReadStatusRequest) {
        readStatusService.update(updateReadStatusRequest);
    }

    @RequestMapping(value = "/receiveMessage", method = RequestMethod.GET)
    public void userReceiveMessage(@RequestParam UUID userId) {
        readStatusService.findAllByUserId(userId);
    }
}
