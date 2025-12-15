package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {

    private static BasicMessageService instance;

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private BinaryContentRepository binaryContentRepository;


    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    public static BasicMessageService getInstance(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        if (instance == null) {
            instance = new BasicMessageService(messageRepository, channelRepository, userRepository);
        }
        return instance;
    }

    @Override
    public Message create(MessageDTO messageDTO) {
        Message message = new Message(messageDTO.content(), messageDTO.userId(), messageDTO.channelId(), messageDTO.files());
        if(messageDTO.files()!=null && !messageDTO.files().isEmpty()){
            List<BinaryContentDTO> files = messageDTO.files();
            for(BinaryContentDTO file : files){
                BinaryContent binaryContent = new BinaryContent(messageDTO.userId(),messageDTO.channelId(),file.fileName(), file.filePath());
                binaryContentRepository.add(binaryContent);
            }
        }
        messageRepository.add(message);
        return message;
    }

    @Override
    public List<Message> findallByChannelId(UUID channelId) {
        List<Message> find = new ArrayList<>();
        for(Message message :  messageRepository.findAll()){
            if(channelId.equals(message.getChanneld())){
                find.add(message);
            }
        }
        return find;
    }

    @Override
    public void delete(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        messageRepository.remove(messageId);
        binaryContentRepository.remove(messageId);
    }

    @Override
    public Message update(MessageDTO messageDTO) {
        Message message = findId(messageDTO.id());
        if (message == null)
            throw new NoSuchElementException(messageDTO.id() + "를 찾을 수 없습니다.");

        List<BinaryContentDTO> files = messageDTO.files();
        List<UUID> newmassageList=new ArrayList<>();

        if(files != null){
            if(!files.isEmpty()){
              for(BinaryContentDTO dto : files){
                  BinaryContent binaryContent = new BinaryContent(messageDTO.userId(),messageDTO.channelId(),dto.fileName(), dto.filePath());
                  binaryContentRepository.add(binaryContent);
                  newmassageList.add(binaryContent.getId());
              }
            }
            else{
                newmassageList=List.of();
            }
        }else{
            newmassageList=message.getAttachmentlds();
        }
        message.update(messageDTO.content(),newmassageList);
        return message;
    }

    @Override
    public Message findId(UUID messageId) {
        if (messageRepository.findId(messageId) == null)
            throw new NoSuchElementException(messageId + "를 찾을 수 없습니다.");
        return messageRepository.findId(messageId);
    }
}
