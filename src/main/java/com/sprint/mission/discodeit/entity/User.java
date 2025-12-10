package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String nickName;
    private String email;
    private Long createAt;
    private Long updateAt;

    public User(String name, String nickName, String email) {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
        this.name = name;
        this.nickName = nickName;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public void update(String name, String nickName, String email){
        this.name=name;
        this.nickName=nickName;
        this.email=email;
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
