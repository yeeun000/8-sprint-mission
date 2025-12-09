//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class FileMessageRepository implements MessageRepository {
//    private final List<Message> messageList = new ArrayList<>();
//    private final File messageFile = new File("data/message.ser");
//
//    private static FileMessageRepository instance = new FileMessageRepository();
//
//    private FileMessageRepository() {
//        loadFromFile();
//    }
//
//    public static FileMessageRepository getInstance() {
//        return instance;
//    }
//
//    private void loadFromFile() {
//        if (!messageFile.exists()) return;
//
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFile))) {
//            List<User> list = (List<User>) ois.readObject();
//            messageList.addAll(list);
//        } catch (Exception e) {}
//    }
//
//    public void addMessage(Channel channelId, Message message) {
//        channelId.addMessage(message);
//        messageList.add(message);
//        save();
//    }
//
//    public void removeMessage(Message message) {
//        save();
//    }
//
//    private void save() {
//        try (FileOutputStream fos = new FileOutputStream("data/message.ser");
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(messageList);
//        } catch (IOException e) {
//            System.out.println();
//        }
//    }
//
//    public Message readId(Long id){
//        for(Message message : messageList){
//            if(message.getId().equals(id)){
//                return message;
//            }
//        }
//        return null;
//    }
//}
