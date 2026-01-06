package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;


  @Override
  public Message create(MessageCreateRequest createMessageRequest,
      List<BinaryContentCreateRequest> binaryContentDTO) {

    UUID channelId = createMessageRequest.channelId();
    UUID userId = createMessageRequest.authorId();

    channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다."));
    userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

    List<UUID> attachmentIds = binaryContentDTO.stream()
        .map(file -> {
          String fileName = file.fileName();
          String contentType = file.contentType();
          byte[] bytes = file.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        }).toList();

    String content = createMessageRequest.content();
    Message message = new Message(
        content,
        channelId,
        userId,
        attachmentIds
    );

    return messageRepository.save(message);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId);
  }

  @Override
  public void delete(UUID id) {
    Message message = find(id);
    message.getAttachmentIds().forEach(binaryContentRepository::deleteById);
    messageRepository.deleteById(id);
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest updateMessageRequest) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));
    message.update(updateMessageRequest.newContent());
    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다."));
  }
}
