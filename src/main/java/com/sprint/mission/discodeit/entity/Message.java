package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String contents;
    private UUID channeld;
    private UUID userId;
    private Instant createAt;
    private Instant updateAt;
    private List<UUID> attachmentlds;

    public Message(String contents, UUID userId, UUID channeld) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.contents = contents;
        this.userId = userId;
        this.channeld = channeld;
    }

    public UUID getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChanneld() {
        return channeld;
    }

    public void update(String content){
        this.contents=contents;
        this.updateAt = Instant.now();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", channeld=" + channeld +
                ", userId=" + userId +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
