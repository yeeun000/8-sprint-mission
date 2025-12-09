package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;


public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID,Channel> channelList = new HashMap<>();
    private final File channelFile = new File("src/main/java/com/sprint/mission/discodeit/service/data/channel.ser");


    private static final FileChannelRepository instance = new FileChannelRepository();

    private FileChannelRepository() {
        loadFromFile();
    }

    public static FileChannelRepository getInstance() {
        return instance;
    }

    private void loadFromFile() {
        if (!channelFile.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(channelFile))) {
        Map<UUID,Channel> list = (Map<UUID,Channel>) ois.readObject();
        channelList.putAll(list);
    } catch (Exception e) {}
}

    @Override
    public void add(Channel channel) {
        channelList.put(channel.getId(),channel);
        saveFile();
    }
    @Override
    public List<Channel> findAll() {
        return channelList.values().stream().toList();
    }

    @Override
    public Channel save(Channel channel){
        channelList.put(channel.getId(),channel);
        saveFile();
        return null;
    }

    @Override
    public Channel findId(UUID channelId){
        boolean find = channelList.containsKey(channelId);
        if(find) {
            saveFile();
            return channelList.get(channelId);
        }
        else return null;
    }

    @Override
    public void remove(UUID channelId) {
        channelList.remove(channelId);
        saveFile();
    }

    private void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(channelFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(channelList);
        } catch (IOException e) {
        }
    }

}
