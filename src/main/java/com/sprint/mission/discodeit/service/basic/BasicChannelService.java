package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public class BasicChannelService implements ChannelService {

    private static BasicChannelService instance;
    private ChannelRepository channelRepository;

    private BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public static BasicChannelService getInstance(ChannelRepository repository) {
        if (instance == null) {
            instance = new BasicChannelService(repository);
        }
        return instance;
    }

    @Override
    public Channel create(Channel.ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        channelRepository.add(channel);
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public void delete(UUID channelId) {
        if(channelRepository.findId(channelId)==null)
            throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
        channelRepository.remove(channelId);
    }

    @Override
    public Channel update(UUID channelId, String name, String description) {
        Channel channel = findId(channelId);
        if(channel==null)
            throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
        channel.update(name, description);
        return channel;
    }


    public Channel findId(UUID channelId) {
        if(channelRepository.findId(channelId)==null)
            throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
        return channelRepository.findId(channelId);
    }
}