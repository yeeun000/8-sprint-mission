package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

public interface ChannelService {
    void create(User user);
    Channel create(Channel.ChannelType type, String name, String description);
    void delete(User user);
    void readAll();
    Channel readChannelName(String channelName);
    void read(User user);
    void update();
    void modifyChannel(User user);

    //Channel create(User owner, String channelName);
}
