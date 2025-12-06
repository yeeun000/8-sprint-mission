package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.Data;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.Data.channelList;
import static com.sprint.mission.discodeit.service.Data.messageList;


public class JCFChannelRepository implements ChannelRepository {

    private static final JCFChannelRepository instance = new JCFChannelRepository();

    private JCFChannelRepository() {}

    public static JCFChannelRepository getInstance() {
        return instance;
    }

    public void addChannel(Channel channel) {
        channelList.add(channel);
    }

    public void removeChannel(Channel channel) {
        channelList.remove(channel);
    }
    public void addUser(Channel channel, User user) {
        channel.addUsers(user);
        channel.setUpdateAt();
    }


    public void removeUser(Channel channel, User user) {
        channel.deleteUsers(user);
    }

    public List<Channel> findAll() {
        return channelList;
    }
    public void update(String channelName, Channel channel) {
        channel.setChannelName(channelName);
        channel.setUpdateAt();
    }

    public Channel readId(UUID id){
        for(Channel channel : channelList){
            if(channel.getId().equals(id)){
                return channel;
            }
        }
        return null;
    }
}
