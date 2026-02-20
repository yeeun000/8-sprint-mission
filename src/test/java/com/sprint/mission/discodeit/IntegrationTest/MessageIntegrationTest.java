package com.sprint.mission.discodeit.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.userDTO.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasicUserService userService;

    @Autowired
    private BasicChannelService channelService;

    @Autowired
    private BasicMessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("메시지 생성")
    void messageCreate_success() throws Exception {
        UserCreateRequest userRequest = new UserCreateRequest("username", "email@test.com", "password");
        UserDto author = userService.create(userRequest, null);

        PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("name", "desc");
        ChannelDto channel = channelService.create(channelRequest);

        MessageCreateRequest request = new MessageCreateRequest(
                "content",
                channel.id(),
                author.id()
        );

        MockMultipartFile partFile = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile attachments = new MockMultipartFile(
                "attachments",
                "name",
                "png",
                "attachments".getBytes()
        );

        mockMvc.perform(multipart("/api/messages")
                        .file(partFile)
                        .file(attachments))
                .andExpect(status().isCreated());

        Message savedMessage = messageRepository.findAll().stream()
                .filter(m -> "content".equals(m.getContent()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("메시지가 저장되지 않았습니다."));

        assertThat(savedMessage.getId()).isNotNull();
        assertThat(savedMessage.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("메시지 삭제")
    void messageDelete_success() throws Exception {
        UserCreateRequest userRequest = new UserCreateRequest("username", "email@test.com", "password");
        UserDto author = userService.create(userRequest, null);

        PublicChannelCreateRequest channelRequest = new PublicChannelCreateRequest("name", "desc");
        ChannelDto channel = channelService.create(channelRequest);

        MessageCreateRequest messageRequest = new MessageCreateRequest(
                "message",
                channel.id(),
                author.id()
        );

        MessageDto savedMessage = messageService.create(messageRequest, List.of());
        UUID messageId = savedMessage.id();

        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());

        assertThat(messageRepository.findById(messageId)).isEmpty();
    }
}
