package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatusDTO.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.readStatusDTO.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @RequestMapping(value = "/readStatus", method = RequestMethod.POST)
    public ReadStatus createReadStatus(@RequestBody ReadStatusDTO readStatusDTO) {
        return readStatusService.create(readStatusDTO);
    }

    @RequestMapping(value = "/readStatus", method = RequestMethod.PUT)
    public void updateReadStatus(@RequestBody UpdateReadStatusRequest updateReadStatusRequest) {
        readStatusService.update(updateReadStatusRequest);
    }

    @RequestMapping(value = "/readStatus", method = RequestMethod.GET)
    public void userReadStatus(@RequestParam UUID userId) {
        readStatusService.findAllByUserId(userId);
    }
}
