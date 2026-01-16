package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(name = "username", nullable = false, length = 50, unique = true)
  private String username;

  @Column(name = "password", nullable = false, length = 60)
  private String password;

  @Column(name = "email", nullable = false, length = 100, unique = true)
  private String email;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", orphanRemoval = true)
  private UserStatus status;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profile = profile;
  }


  public void update(String username, String password, String email, BinaryContent profile) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.profile = profile;
  }

  public static User create(String name, String email, String password) {
    return new User(name, email, password, null);
  }

  public static User createProfile(String name, String email, String password,
      BinaryContent profileId) {
    return new User(name, email, password, profileId);
  }

}
