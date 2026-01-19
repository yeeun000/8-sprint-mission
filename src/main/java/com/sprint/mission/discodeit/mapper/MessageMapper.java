package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageMapper(BinaryContentMapper binaryContentMapper, UserMapper userMapper) {
    this.binaryContentMapper = binaryContentMapper;
    this.userMapper = userMapper;
  }

  public MessageDto toDto(Message message) {
    if (message.getAuthor() != null) {
      System.out.println("디버깅 - 메시지 ID: " + message.getId());
      System.out.println("디버깅 - 작성자 이름: " + message.getAuthor().getUsername());
    } else {
      System.out.println("디버깅 - 메시지 ID: " + message.getId() + " 의 작성자가 null입니다!");
    }

    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto).toList();
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        userMapper.toDto(message.getAuthor()),
        attachments);
  }
}
