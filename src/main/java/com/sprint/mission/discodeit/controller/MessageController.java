package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.messageDTO.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, consumes = "multipart/form-data")
    public Message send(@RequestPart("createMessageRequest") String createMessageRequestJson,
                        @RequestPart(value = "file", required = false) MultipartFile[] files) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateMessageRequest createMessageRequest = objectMapper.readValue(createMessageRequestJson, CreateMessageRequest.class);

        List<BinaryContentDTO> binaryContentDTO = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    binaryContentDTO.add(new BinaryContentDTO(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    ));
                }
            }
        }

        return messageService.create(createMessageRequest, binaryContentDTO);
    }

    @RequestMapping(value = "/message", method = RequestMethod.PUT)
    public Message update(@RequestBody UpdateMessageRequest updateMessageRequest) {
        return messageService.update(updateMessageRequest);
    }

    @RequestMapping(value = "/message/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        messageService.delete(id);
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public List<Message> findAllByUserId(@RequestParam UUID channelId) {
        return messageService.findAllByChannelId(channelId);
    }
}
