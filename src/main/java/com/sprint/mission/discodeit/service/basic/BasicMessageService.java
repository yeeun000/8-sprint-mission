package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
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
  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO) {

    UUID channelId = createMessageRequest.channelId();
    UUID userId = createMessageRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    User author = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<BinaryContent> attachmentIds = binaryContentDTO == null ? List.of() :
        binaryContentDTO.stream()
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

    return toMessageDto(messageRepository.save(message));
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(
      UUID channelId,
      Pageable pageable
  ) {
    Slice<Message> slice =
        messageRepository.findAllByChannelId(channelId, pageable);
    return PageResponseMapper.fromSlice(
        slice,
        this::toMessageDto
    );
  }


  @Override
  @Transactional
  public void delete(UUID id) {
    messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));
    messageRepository.deleteById(id);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest updateMessageRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));
    message.update(updateMessageRequest.newContent());
    return toMessageDto(messageRepository.save(message));
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID id) {
    return toMessageDto(messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다.")));
  }

  public MessageDto toMessageDto(Message message) {
    UserDto author = userMapper.toDto(message.getAuthor());
    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto).toList();
    return messageMapper.toDto(
        message, author,
        attachments);
  }
}
