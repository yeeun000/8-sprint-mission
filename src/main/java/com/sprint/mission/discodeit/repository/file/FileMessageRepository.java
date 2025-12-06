package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.sprint.mission.discodeit.service.Data.messageList;

public class FileMessageRepository implements MessageRepository {

    private static FileMessageRepository instance = new FileMessageRepository();

    private FileMessageRepository() {}

    public static FileMessageRepository getInstance() {
        return instance;
    }

    public void addMessage(Channel channelId, Message message) {
        channelId.addMessage(message);
        messageList.add(message);
        save();
    }

    public void removeMessage(Message message) {
        save();
    }

    private void save() {
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(messageList);
        } catch (IOException e) {
            System.out.println();
        }
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
