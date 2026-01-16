package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;


  @Override
  public MessageDto create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO) {

    UUID channelId = createMessageRequest.channelId();
    UUID userId = createMessageRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    User author = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<BinaryContent> attachmentIds = binaryContentDTO.stream()
        .map(file -> {
          String fileName = file.fileName();
          String contentType = file.contentType();
          byte[] bytes = file.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent);
        }).toList();

    String content = createMessageRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachmentIds
    );

    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId)
        .stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    messageMapper.toDto(messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다.")));
    messageRepository.deleteById(id);
  }

  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest updateMessageRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));
    message.update(updateMessageRequest.newContent());
    return messageMapper.toDto(messageRepository.save(message));
  }

  @Override
  public MessageDto find(UUID id) {
    return messageMapper.toDto(messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다.")));
  }
}
