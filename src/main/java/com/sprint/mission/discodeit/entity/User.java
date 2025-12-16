package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binaryContentDTO.ProfileDTO;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String password;
    private String email;
    private Instant createAt;
    private Instant updateAt;
    private ProfileDTO profileImage;

    public User(String name, String password, String email, Optional<ProfileDTO> profileImage) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
        this.name = name;
        this.password = password;
        this.email = email;
        if (profileImage.isPresent())
            this.profileImage = profileImage.get();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public ProfileDTO getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileDTO profileImage) {
        this.profileImage = profileImage;
    }

    public void update(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.updateAt = Instant.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
