package com.sprint.mission.discodeit.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ChannelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasicChannelService channelService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채널 생성")
    void channelCreate_success() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
                "name",
                "desc"
        );

        mockMvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Channel saved = channelRepository.findAll().stream()
                .filter(c -> "name".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("채널이 저장되지 않았습니다."));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("채널 삭제")
    void channelDelete_success() throws Exception {
        PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("name", "desc");
        ChannelDto channel = channelService.create(channelRequest);
        UUID id = channel.id();

        mockMvc.perform(delete("/api/channels/{channelId}", id))
                .andExpect(status().isNoContent());

        assertThat(channelRepository.findById(id)).isEmpty();
    }

}
