package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Validated
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") @Valid MessageCreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
      throws IOException {
    log.debug("메시지 생성 시작 - channelId: {}", request.channelId());
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

    MessageDto message = messageService.create(request, binaryContentDTO);
    log.debug("메시지 생성 완료 - messageId: {}", message.id());
    return ResponseEntity.status(HttpStatus.CREATED).body(message);
  }

  @PatchMapping("/{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable("messageId") UUID messageId,
      @RequestBody @Valid MessageUpdateRequest request) {
    log.debug("메시지 수정 시작 - messageId: {}", messageId);
    MessageDto message = messageService.update(messageId, request);
    log.debug("메시지 수정 완료");
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
    log.debug("메시지 삭제 시작 - messageId: {}", messageId);
    messageService.delete(messageId);
    log.debug("메시지 삭제 완료");
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam UUID channelId,
      @RequestParam(value = "cursor", required = false) Instant cursor,
      @PageableDefault(
          size = 50,
          page = 0,
          sort = "createdAt",
          direction = Sort.Direction.DESC
      ) Pageable pageable
  ) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
        pageable);
    return ResponseEntity.ok(messages);
  }
}
