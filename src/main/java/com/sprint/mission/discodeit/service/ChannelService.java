package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public interface ChannelService {
    Channel create(Channel.ChannelType type, String name, String description);
    List<Channel> findAll();
    void delete(UUID channelId);
    Channel update(UUID channelId, String newName, String newDescription);
   // Channel findId(UUID channelId);
}

