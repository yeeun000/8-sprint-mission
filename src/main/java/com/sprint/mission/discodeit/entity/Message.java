package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    //private Long id;
    private String messageContents;
    private User sender;
    private Channel channeld;


    private Long createAt;
    private Long updateAt;

    public Message(String messageContents, User sender, Channel channeld) {
        this.id = UUID.randomUUID();
       // this.id= System.currentTimeMillis();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
        this.messageContents = messageContents;
        this.sender = sender;
        this.channeld = channeld;
    }

    public UUID getId() {
        return id;
    }

//    public Long getId() {
//        return id;
//    }

    public String getMessageContents() {
        return messageContents;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChanneld() {
        return channeld;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    @Override
    public String toString() {
        return sender.getNickName() + "님의 메시지 : " + messageContents + "\n";
    }
}
