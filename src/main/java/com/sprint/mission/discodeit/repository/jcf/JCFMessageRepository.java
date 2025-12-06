package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import static com.sprint.mission.discodeit.service.Data.messageList;
import static com.sprint.mission.discodeit.service.Data.userList;

public class JCFMessageRepository implements MessageRepository {

    private static JCFMessageRepository instance = new JCFMessageRepository();

    private JCFMessageRepository() {}

    public static JCFMessageRepository getInstance() {
        return instance;
    }

    public void addMessage(Channel channelId, Message message){
        channelId.addMessage(message);
    }

    public void removeMessage(Message message){

    }
   public Message readId(Long id){
       for(Message message : messageList){
           if(message.getId().equals(id)){
               return message;
           }
       }
       return null;
    }

}
