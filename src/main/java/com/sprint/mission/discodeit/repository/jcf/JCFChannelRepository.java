package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;


public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID,Channel> channelList = new HashMap<>();
    private static final JCFChannelRepository instance = new JCFChannelRepository();

    private JCFChannelRepository() {}

    public static JCFChannelRepository getInstance() {
        return instance;
    }

    @Override
    public void add(Channel channel) {
        channelList.put(channel.getId(),channel);
    }

    @Override
    public List<Channel> findAll() {
        return channelList.values().stream().toList();
    }

    @Override
    public Channel save(Channel channel){
        channelList.put(channel.getId(),channel);
        return null;
    }

    @Override
    public Channel findId(UUID channelId){
        boolean find = channelList.containsKey(channelId);
        if(find)
            return channelList.get(channelId);
        else return null;
    }

    @Override
    public void remove(UUID channelId) {
        channelList.remove(channelId);
    }

}
