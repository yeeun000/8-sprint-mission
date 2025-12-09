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
    private String channelName;
    private String description;
    private ChannelType type;
    private Long createAt;
    private Long updateAt;


    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
        this.type = type;
        this.channelName = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getChannelName() {
        return channelName;
    }

    public ChannelType getType() {
        return type;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void update(String channelName,String description){
        this.channelName=channelName;
        this.description=description;
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
