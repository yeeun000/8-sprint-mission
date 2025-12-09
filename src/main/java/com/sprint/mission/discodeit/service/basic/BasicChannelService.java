package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Scanner;
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
    public void delete(UUID channelId) {
        channelRepository.remove(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String channelName, String newDescription) {
        Channel channel = channelRepository.findId(channelId);
        channel.update(channelName,newDescription);
        channelRepository.save(channel);
        return channel;
    }


    public Channel findId(UUID channelId) {
        return  channelRepository.findId(channelId);
    }
}