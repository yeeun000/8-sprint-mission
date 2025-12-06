package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface MessageService {
//    Message create(User user, Channel channel);
//    void delete(User user, Channel channel);
//    Message read(User user, Channel channel);
//    void update(User user, Channel channel);
Message create(String content, UUID channelId, UUID authorId);

}
