package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.binaryContentDTO.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.messageDTO.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public Message create(CreateMessageDTO createMessageDTO) {
        Message message = new Message(createMessageDTO.content(), createMessageDTO.userId(), createMessageDTO.channelId(), createMessageDTO.files());
        messageRepository.add(message);
        if (createMessageDTO.files() != null && !createMessageDTO.files().isEmpty()) {
            List<BinaryContentDTO> files = createMessageDTO.files();
            for (BinaryContentDTO file : files) {
                BinaryContent binaryContent = new BinaryContent(message.getUserId(), message.getChannelId(), message.getId(), file.fileName(), file.filePath());
                binaryContentRepository.add(binaryContent);
            }
        }
        return message;
    }

    @Override
    public List<Message> findallByChannelId(UUID channelId) {
        List<Message> find = new ArrayList<>();
        for (Message message : messageRepository.findAll()) {
            if (channelId.equals(message.getChannelId())) {
                find.add(message);
            }
        }
        return find;
    }

    @Override
    public void delete(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        binaryContentRepository.remove(messageId);
        messageRepository.remove(messageId);
    }

    @Override
    public Message update(MessageDTO messageDTO) {
        Message message = findId(messageDTO.id());
        if (message == null)
            throw new NoSuchElementException(messageDTO.id() + "를 찾을 수 없습니다.");

        List<BinaryContentDTO> files = messageDTO.files();
        List<UUID> newmassageList = new ArrayList<>();

        if (files != null) {
            binaryContentRepository.remove(message.getId());
            if (!files.isEmpty()) {
                for (BinaryContentDTO dto : files) {
                    BinaryContent binaryContent = new BinaryContent(message.getUserId(), message.getChannelId(), message.getId(), dto.fileName(), dto.filePath());
                    binaryContentRepository.add(binaryContent);
                    newmassageList.add(binaryContent.getId());
                }
            }
        } else {
            newmassageList = message.getAttachmentlds();
        }
        message.update(messageDTO.content(), newmassageList);
        return message;
    }

    @Override
    public Message findId(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        return messageRepository.findId(messageId);
    }
}
