package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
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
    private Instant createAt;
    private Instant updateAt;
    private List<UUID> users = new ArrayList<>();


    public Channel(ChannelType type, String name, String description, List<UUID> users) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.type = type;
        this.channelName = name;
        this.description = description;
        if (type == ChannelType.PRIVATE) {
            if (users != null)
                this.users = new ArrayList<>(users);
            else
                this.users = new ArrayList<>();
        } else {
            this.users = null;
        }
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

    public List<UUID> getUsers() {
        return users;
    }

    public void update(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        this.updateAt = Instant.now();
    }

    public boolean containUser(UUID userId) {
        return type == ChannelType.PRIVATE
                && users != null
                && users.contains(userId);
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
