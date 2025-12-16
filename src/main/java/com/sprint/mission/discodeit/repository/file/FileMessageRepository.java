//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import org.springframework.stereotype.Repository;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class FileMessageRepository extends FileRepository<Message> implements MessageRepository {
//
//    public FileMessageRepository() {
//        super("src/main/java/com/sprint/mission/discodeit/service/data/message.ser");
//    }
//
//    @Override
//    public void add(Message message) {
//        getFile().put(message.getId(), message);
//        saveFile();
//    }
//
//    @Override
//    public List<Message> findAll() {
//        return getFile().values().stream().toList();
//    }
//
//    @Override
//    public Message findId(UUID messageId) {
//        boolean find = getFile().containsKey(messageId);
//        if (find)
//            return getFile().get(messageId);
//        else return null;
//    }
//
//    @Override
//    public void remove(UUID messageId) {
//        getFile().remove(messageId);
//        saveFile();
//    }
//
//    @Override
//    public Instant last(UUID channelId) {
//        Instant a = Instant.now();
//        return a;
//    }
//
//}
