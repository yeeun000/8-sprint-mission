package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum ChannelType {
        PUBLIC,
        PRIVATE
    }

     private UUID id;
  //  private Long id;
    private String channelName;
    private List<User> users;
    private List<Message> messages;

    private ChannelType type;
    private String description;

    private Long createAt;
    private Long updateAt;


//    public Channel(String channelName, User user) {
//        this.id = UUID.randomUUID();
//        this.createAt = System.currentTimeMillis();
//        this.updateAt = System.currentTimeMillis();
//        this.channelName = channelName;
//        this.users = new ArrayList<>();
//        this.users.add(user);
//        this.messages = new ArrayList<>();
//    }

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
//        this.id= System.currentTimeMillis();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();

        this.type = type;
        this.channelName = name;
        this.description = description;

        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }
//    public Long getId() {
//        return id;
//    }


    public String getDescription() {
        return description;
    }

    public String getChannelName() {
        return channelName;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addUsers(User user) {
        this.users.add(user);
    }

    public void deleteUsers(User user) {
        this.users.remove(user);
    }

    public List<User> getUser() {
        return users;
    }

    public void setUpdateAt() {
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Channel ID = " + id + ",\n Channel Name = " + channelName);
        for (User user : users) {
            sb.append(",\n User = " + user.getNickName());
        }
        sb.append(",\n createAt = " + createAt + ",\n updateAt = " + updateAt);
        return sb.toString();
    }
}
