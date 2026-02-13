package com.sprint.mission.discodeit.controllerTest;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BasicChannelService channelService;

  @BeforeEach
  @DisplayName("테스트 환경 설정 확인")
  void setUp() {
    assert mockMvc != null;
    assert channelService != null;
  }

  @Test
  @DisplayName("채널 전체 조회 성공")
  void findAll_success() throws Exception {
    ChannelDto dto = new ChannelDto(
        UUID.randomUUID(),
        PUBLIC,
        "name",
        "desc",
        List.of(),
        Instant.now()
    );
    UUID userId = UUID.randomUUID();

    given(channelService.findAllByUserId(userId)).willReturn(List.of(dto));

    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name").value("name"));
  }

  @Test
  @DisplayName("채널 삭제 실패")
  void delete_fail() throws Exception {
    UUID channelId = UUID.randomUUID();

    doThrow(new ChannelNotFoundException(channelId))
        .when(channelService)
        .delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound());
  }

}
