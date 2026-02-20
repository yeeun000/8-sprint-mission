package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO) {

    log.debug("메시지 생성 요청 - messageContent: {}, channelId: {}, authorId: {}",
        createMessageRequest.content(),
        createMessageRequest.channelId(),
        createMessageRequest.authorId()
    );

    UUID channelId = createMessageRequest.channelId();
    UUID userId = createMessageRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("채널 찾기 실패 - channelId: {}", channelId);
          return new ChannelNotFoundException(channelId);
        });
    User author = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("유저 찾기 실패 - userId: {}", userId);
          return new UserNotFoundException(userId);
        });

    List<BinaryContent> attachmentIds =
        binaryContentDTO == null ? List.of() : binaryContentDTO.stream()
            .map(file -> {
              String fileName = file.fileName();
              String contentType = file.contentType();
              byte[] bytes = file.bytes();
              BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                  contentType);
              binaryContentRepository.save(binaryContent);
              binaryContentStorage.put(binaryContent.getId(), bytes);
              return binaryContent;
            }).toList();

    String content = createMessageRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachmentIds
    );

    messageRepository.save(message);

    log.info("메시지 생성 완료 - id: {}", message.getId());

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDto> slice =
        messageRepository.findAllByChannelIdWithAuthor(channelId,
                Optional.ofNullable(createAt).orElse(Instant.now()), pageable)
            .map(messageMapper::toDto);
    Instant nextCursor = null;

    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1).createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Override
  @Transactional
  public void delete(UUID id) {

    log.debug("메시지 삭제 요청 - id: {}", id);

    messageRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("메시지 찾기 실패 - id: {}", id);
          return new MessageNotFoundException(id);
        });
    messageRepository.deleteById(id);

    log.info("메시지 삭제 완료 - id: {}", id);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest updateMessageRequest) {

    log.debug("메시지 수정 요청 - id: {}, content: {}",
        messageId,
        updateMessageRequest.newContent());

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("메시지 찾기 실패 - id: {}", messageId);
          return new MessageNotFoundException(messageId);
        });
    message.update(updateMessageRequest.newContent());

    log.info("메시지 수정 완료 - id: {}", messageId);

    return messageMapper.toDto(message);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID id) {
    return messageRepository.findById(id)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new MessageNotFoundException(id));
  }

}
