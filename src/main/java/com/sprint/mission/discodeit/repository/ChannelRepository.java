package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void addChannel(Channel channel);
    void removeChannel(Channel channel);
    void addUser(Channel channel, User user);
    void removeUser(Channel channel, User user);
    List<Channel> findAll();
    void update(String channelName,Channel channel);

    Channel readId(UUID id);
}
