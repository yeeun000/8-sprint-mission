package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.Data.channelList;

public class FileChannelRepository implements ChannelRepository {

    private static final FileChannelRepository instance = new FileChannelRepository();

    private FileChannelRepository() {}

    public static FileChannelRepository getInstance() {
        return instance;
    }

    public void addChannel(Channel channel) {
        channelList.add(channel);
        save();
    }

    public void removeChannel(Channel channel) {
        channelList.remove(channel);
        save();
    }

    public void addUser(Channel channel, User user) {
        channel.addUsers(user);
        channel.setUpdateAt();
        save();
    }


    public void removeUser(Channel channel, User user) {
        channel.deleteUsers(user);
        save();
    }
    public List<Channel> findAll() {
        return channelList;
    }

    public void update(String channelName, Channel channel) {
        channel.setChannelName(channelName);
        channel.setUpdateAt();
        save();
    }


    private void save() {
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(channelList);
        } catch (IOException e) {
            System.out.println();
        }
    }

    public Channel readId(UUID id){
        for(Channel channel : channelList){
            if(channel.getId().equals(id)){
                return channel;
            }
        }
        return null;
    }
}
