package com.sprint.mission.discodeit.repositoryTest;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("PUBLIC 채널일 경우 조회할 수 있다.")
  void findByPublicType_success() {
    Channel publicChannel = new Channel(PUBLIC, "public", "desc");
    em.persist(publicChannel);

    em.flush();
    em.clear();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(PUBLIC, List.of());

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getType()).isEqualTo(PUBLIC);
  }

  @Test
  @DisplayName("PRIVATE 채널일 경우 id 목록이 없으면 조회할 수 없다.")
  void findByPrivateType_fail() {
    Channel privateChannel = new Channel(PRIVATE, "private", "desc");
    em.persist(privateChannel);

    em.flush();
    em.clear();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(PUBLIC, List.of());

    assertThat(result).isEmpty();
  }
}
