//package com.sprint.mission.discodeit.repository.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class JCFMessageRepository implements MessageRepository {
//
//    private final List<Message> messageList = new ArrayList<>();
//    private static JCFMessageRepository instance = new JCFMessageRepository();
//
//    private JCFMessageRepository() {}
//
//    public static JCFMessageRepository getInstance() {
//        return instance;
//    }
//
//    public void addMessage(Channel channelId, Message message){
//        channelId.addMessage(message);
//    }
//
//    public void removeMessage(Message message){
//
//    }
//   public Message readId(Long id){
//       for(Message message : messageList){
//           if(message.getId().equals(id)){
//               return message;
//           }
//       }
//       return null;
//    }
//
//}
