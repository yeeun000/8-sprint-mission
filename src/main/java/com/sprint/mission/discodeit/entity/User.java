package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    //private Long id;
    private String name;
    private String nickName;
    private String email;

    private Long createAt;
    private Long updateAt;

    public User(String name, String nickName, String email) {
        this.id = UUID.randomUUID();
       // this.id= System.currentTimeMillis();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
        this.name = name;
        this.nickName = nickName;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }
//    public Long getId() {
//        return id;
//    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUpdateAt() {
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return " User ID = " + id + ",\n name = " + name + ",\n nickName = " + nickName + ",\n email = " + email + ",\n createAt = " + createAt + ",\n updateAt = " + updateAt;
    }
}
