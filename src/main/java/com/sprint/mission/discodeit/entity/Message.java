package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String messageContents;
    private UUID channeld;
    private UUID userId;
    private Long createAt;
    private Long updateAt;

    public Message(String messageContents, UUID userId, UUID channeld) {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
        this.messageContents = messageContents;
        this.userId = userId;
        this.channeld = channeld;
    }

    public UUID getId() {
        return id;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChanneld() {
        return channeld;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

}
