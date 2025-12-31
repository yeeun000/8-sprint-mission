package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
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
    private Instant createdAt;
    private Instant updatedAt;


    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.type = type;
        this.channelName = name;
        this.description = description;
    }

    public static Channel createPrivateChannel(ChannelType type) {
        return new Channel(type, null, null);
    }

    public static Channel createPublicChannel(ChannelType type, String name, String description) {
        return new Channel(type, name, description);
    }


        public void update(String newchannelName, String newdescription) {
        this.channelName = newchannelName;
        this.description = newdescription;
        this.updatedAt = Instant.now();
    }


    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
