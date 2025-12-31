package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.messageDTO.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<Message> send(
      @RequestPart("createMessageRequest") String createMessageRequestJson,
      @RequestPart(value = "attachments", required = false) MultipartFile[] attachments)
      throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    CreateMessageRequest createMessageRequest = objectMapper.readValue(createMessageRequestJson,
        CreateMessageRequest.class);

    List<BinaryContentDTO> binaryContentDTO = new ArrayList<>();
    if (attachments != null) {
      for (MultipartFile file : attachments) {
        if (!file.isEmpty()) {
          binaryContentDTO.add(new BinaryContentDTO(
              file.getOriginalFilename(),
              file.getContentType(),
              file.getBytes()
          ));
        }
      }
    }

    Message message = messageService.create(createMessageRequest, binaryContentDTO);

    return ResponseEntity.ok(message);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<Message> update(@PathVariable UUID messageId,
      @RequestBody UpdateMessageRequest updateMessageRequest) {
    Message message = messageService.update(messageId, updateMessageRequest);
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<Message>> findAllByUserId(@RequestParam UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messages);
  }
}
