package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class FileMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messageList = new HashMap<>();
    private final File messageFile = new File("src/main/java/com/sprint/mission/discodeit/service/data/message.ser");

    private static FileMessageRepository instance = new FileMessageRepository();

    private FileMessageRepository() {
        loadFromFile();
    }

    public static FileMessageRepository getInstance() {
        return instance;
    }

    private void loadFromFile() {
        if (!messageFile.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFile))) {
            Map<UUID, Message> list = (Map<UUID, Message>) ois.readObject();
            messageList.putAll(list);
        } catch (Exception e) {
        }
    }

    public void add(Message message) {
        messageList.put(message.getId(), message);
        saveFile();
    }

    public List<Message> findAll() {
        return messageList.values().stream().toList();
    }

    public Message findId(UUID messageId) {
        boolean find = messageList.containsKey(messageId);
        if (find)
            return messageList.get(messageId);
        else return null;
    }

    public void remove(UUID messageId) {
        messageList.remove(messageId);
        saveFile();
    }

    private void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(messageFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(messageList);
        } catch (IOException e) {
            System.out.println();
        }
    }

}
