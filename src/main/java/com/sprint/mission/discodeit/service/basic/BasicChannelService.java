package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.FindChannelDTO;
import com.sprint.mission.discodeit.dto.PublicChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;

@Service
public class BasicChannelService implements ChannelService {

    private static BasicChannelService instance;
    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private ReadStatusRepository readStatusRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(ChannelRepository repository) {
        if (instance == null) {
            instance = new BasicChannelService(repository);
        }
        return instance;
    }

    public Channel create(ChannelDTO dto) {
        Channel channel;

        if (dto.type() == Channel.ChannelType.PUBLIC) {
            channel = new Channel(PUBLIC, dto.name(), dto.description(), null);
        } else {
            channel = new Channel(PRIVATE, null, null, dto.users());

            for (UUID userId : dto.users()) {
                ReadStatus readStatus = new ReadStatus(userId, channel.getId());
                readStatusRepository.add(readStatus);
            }
        }

        channelRepository.add(channel);
        return channel;
    }


    @Override
    public List<FindChannelDTO> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<FindChannelDTO> findChannelList = new ArrayList<>();

        for (Channel channel : channels) {
            Instant lastRead = messageRepository.last(channel.getId());
            FindChannelDTO channelDTO;
            if (channel.getType() == PUBLIC || channel.getUsers().contains(userId)) {
                channelDTO = new FindChannelDTO(channel.getId(), channel.getType(), lastRead, channel.getUsers());
            } else channelDTO = new FindChannelDTO(channel.getId(), channel.getType(), lastRead, null);
            findChannelList.add(channelDTO);
        }
        return findChannelList;
    }

    @Override
    public void delete(UUID channelId) {
        if (channelRepository.findId(channelId) == null)
            throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
        channelRepository.remove(channelId);
        messageRepository.remove(channelId);
        readStatusRepository.remove(channelId);
    }

    @Override
    public Channel update(PublicChannelDTO publicChannelDTO) {
        Channel channel = channelRepository.findId(publicChannelDTO.id());
        if (channel == null)
            throw new NoSuchElementException(publicChannelDTO.id() + "를 찾을 수 없습니다.");
        if (channel.getType() == PUBLIC)
            channel.update(publicChannelDTO.name(), publicChannelDTO.description());
        return channel;
    }


    public FindChannelDTO findId(UUID channelId) {
        Channel channel = channelRepository.findId(channelId);

        if (channel == null) {
            throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
        }
        List<UUID> users = null;
        if (channel.getType() == PRIVATE)
            users = channel.getUsers();

        Instant lastRead = messageRepository.last(channelId);
        FindChannelDTO channelDTO = new FindChannelDTO(channel.getId(), channel.getType(), lastRead, users);
        return channelDTO;
    }
}