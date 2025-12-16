package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.channelDTO.FindChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelDTO;
import com.sprint.mission.discodeit.dto.channelDTO.UpdateChannelDTO;
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

    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private ReadStatusRepository readStatusRepository;

    private BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository, ReadStatusRepository readStatusRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public Channel create(PublicChannelDTO dto) {
        Channel channel = new Channel(PUBLIC, dto.name(), dto.description(), null);
        channelRepository.add(channel);
        return channel;
    }

    @Override
    public Channel create(PrivateChannelDTO dto) {
        Channel channel = new Channel(PRIVATE, null, null, dto.users());
        if (dto.users() != null) {
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
            FindChannelDTO findchannelDTO;
            if (channel.getType() == PUBLIC || channel.getUsers().contains(userId)) {
                findchannelDTO = new FindChannelDTO(channel.getId(), channel.getType(), lastRead, channel.getUsers());
                findChannelList.add(findchannelDTO);
            }
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
    public Channel update(UpdateChannelDTO updateChannelDTO) {
        Channel channel = channelRepository.findId(updateChannelDTO.id());
        if (channel == null)
            throw new NoSuchElementException(updateChannelDTO.id() + "를 찾을 수 없습니다.");
        if (channel.getType() == PUBLIC)
            channel.update(updateChannelDTO.name(), updateChannelDTO.description());
        return channel;
    }

    @Override
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