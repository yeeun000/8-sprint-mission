package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Data {

    public static final List<Channel> channelList = new ArrayList<>();
    public static final List<User> userList = new ArrayList<>();
    public static final List<Message> messageList = new ArrayList<>();

    static {
        try (FileInputStream fis = new FileInputStream("user.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<User> user = (List<User>) ois.readObject();
            userList.addAll(user);
        } catch (IOException | ClassNotFoundException e) {
        }


        try (FileInputStream fis = new FileInputStream("channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Channel> channel = (List<Channel>) ois.readObject();
            channelList.addAll(channel);
        } catch (IOException | ClassNotFoundException e) {
        }


        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            List<Message> message = (List<Message>) ois.readObject();
            messageList.addAll(message);
        } catch (IOException | ClassNotFoundException e) {
        }


        for (Message msg : messageList) {
            if (msg.getChanneld() == null)
                continue;
            UUID msgChannelId = msg.getChanneld().getId();
          //  Long msgChannelId = msg.getChanneld().getId();

            for (Channel ch : channelList) {
                if (ch.getId().equals(msgChannelId)) {
                    ch.addMessage(msg);
                    break;
                }
            }
        }

    }
}
