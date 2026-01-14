package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message API")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") String messageCreateRequestJson,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    MessageCreateRequest request = objectMapper.readValue(messageCreateRequestJson,
        MessageCreateRequest.class);
    List<BinaryContentCreateRequest> binaryContentDTO = new ArrayList<>();
    if (attachments != null) {
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          binaryContentDTO.add(
              new BinaryContentCreateRequest(file.getOriginalFilename(), file.getContentType(),
                  file.getBytes()));
        }
      }
    }

    Message message = messageService.create(request, binaryContentDTO);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(message);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> update(
      @PathVariable UUID messageId, @RequestBody MessageUpdateRequest request) {
    Message message = messageService.update(messageId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(message);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}
