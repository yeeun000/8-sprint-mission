package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.dto.messageDTO.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDTO.MessageDto;
import com.sprint.mission.discodeit.dto.messageDTO.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.userDTO.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService 테스트")
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @InjectMocks
    private BasicMessageService messageService;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @BeforeEach
    @DisplayName("테스트 환경 설정 확인")
    void setUp() {
        assertNotNull(messageRepository);
        assertNotNull(channelRepository);
        assertNotNull(userRepository);
        assertNotNull(messageMapper);
        assertNotNull(messageService);
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("채널아이디, 유저 아이디가 있으면 메시지를 작성할 수 있다.")
        void create_whenWriteChannelIdAndUserId_MessageSuccess() {
            Channel channel = new Channel(Channel.ChannelType.PUBLIC, "name", "desc");
            User author = new User("username", "email", "password", null);
            MessageCreateRequest request = new MessageCreateRequest(
                    "hi",
                    channel.getId(),
                    author.getId()
            );

            given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
            given(userRepository.findById(author.getId())).willReturn(Optional.of(author));

            given(messageRepository.save(any(Message.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            UserDto userDto = new UserDto(
                    UUID.randomUUID(),
                    "test",
                    "test",
                    null,
                    true
            );

            MessageDto fakeDto = new MessageDto(
                    UUID.randomUUID(),
                    Instant.now(),
                    Instant.now(),
                    "test",
                    UUID.randomUUID(),
                    userDto,
                    List.of()
            );

            given(messageMapper.toDto(any(Message.class))).willReturn(fakeDto);

            MessageDto result = messageService.create(request, List.of());

            assertNotNull(result);
            then(messageRepository).should().save(messageCaptor.capture());
            Message savedMessage = messageCaptor.getValue();

            assertEquals("hi", savedMessage.getContent());

            then(messageMapper).should().toDto(any(Message.class));

        }

        @Test
        @DisplayName("유저 아이디가 없으면 메시지를 작성할 수 없다.")
        void create_whenWriteNotUserId_MessageFail() {
            Channel channel = new Channel(Channel.ChannelType.PUBLIC, "name", "desc");
            UUID authorId = UUID.randomUUID();
            MessageCreateRequest request = new MessageCreateRequest(
                    "hi",
                    channel.getId(),
                    authorId
            );

            given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
            given(userRepository.findById(authorId)).willReturn(Optional.empty());

            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> messageService.create(request, List.of()),
                    "유저를 찾을 수 없습니다."
            );

            assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("수정 테스트")
    class UpdateTest {

        @Test
        @DisplayName("메시지 내용을 변경할 수 있다.")
        void update_whenWriteUpdate_MessageSuccess() {
            MessageUpdateRequest request = new MessageUpdateRequest(
                    "newContent"
            );
            Channel channel = new Channel(Channel.ChannelType.PUBLIC, "name", "desc");
            User author = new User("username", "email", "password", null);
            Message message = new Message("content", channel, author, List.of());

            given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));

            MessageDto fakeDto = new MessageDto(
                    message.getId(),
                    message.getCreatedAt(),
                    message.getUpdatedAt(),
                    "newContent",
                    channel.getId(),
                    null,
                    List.of()
            );
            given(messageMapper.toDto(any(Message.class))).willReturn(fakeDto);

            MessageDto result = messageService.update(message.getId(), request);

            then(messageRepository).should().findById(message.getId());
            assertEquals("newContent", message.getContent());

            then(messageMapper).should().toDto(message);
            assertEquals("newContent", result.content());

        }

        @Test
        @DisplayName("메시지 아이디가 없으면 실패해야한다.")
        void update_whenUserIdNotExist_MessageFail() {
            MessageUpdateRequest request = new MessageUpdateRequest(
                    "newContent"
            );
            UUID messageId = UUID.randomUUID();

            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            MessageNotFoundException exception = assertThrows(
                    MessageNotFoundException.class,
                    () -> messageService.update(messageId, request),
                    "메시지를 찾을 수 없습니다."
            );

            assertEquals("메시지를 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

        @Test
        @DisplayName("메시지 아이디가 있으면 메시지를 삭제할 수 있다.")
        void delete_whenMessageIdExists_MessageSuccess() {
            Channel channel = new Channel(Channel.ChannelType.PUBLIC, "name", "desc");
            User author = new User("username", "email", "password", null);
            Message message = new Message("content", channel, author, List.of());
            UUID messageId = message.getId();

            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

            messageService.delete(messageId);

            then(messageRepository).should().deleteById(messageId);
        }

        @Test
        @DisplayName("메시지 아이디가 없으면 실패해야한다.")
        void delete_whenNotMessageIdExists_MessageFail() {
            UUID messageId = UUID.randomUUID();

            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            MessageNotFoundException exception = assertThrows(
                    MessageNotFoundException.class,
                    () -> messageService.delete(messageId),
                    "메시지를 찾을 수 없습니다."
            );

            assertEquals("메시지를 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("채널아이디 찾기 테스트")
    class FindByChannelIdTest {

        @Test
        @DisplayName("채널아이디가 있으면 찾을 수 있다.")
        void find_whenChannelId_MessageSuccess() {
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);
            Message message = new Message("content", null, null, List.of());
            MessageDto dto = new MessageDto(
                    UUID.randomUUID(),
                    Instant.now(),
                    Instant.now(),
                    "content",
                    channelId,
                    null,
                    List.of()
            );
            Slice<Message> slice = new SliceImpl<>(List.of(message), pageable, false);

            given(messageRepository.findAllByChannelIdWithAuthor(any(UUID.class), any(Instant.class),
                    any(Pageable.class)))
                    .willReturn(slice);
            given(messageMapper.toDto(any(Message.class)))
                    .willReturn(dto);

            messageService.findAllByChannelId(channelId, null, pageable);

            then(pageResponseMapper).should().fromSlice(any(), any(Instant.class));
        }

        @Test
        @DisplayName("메시지가 없으면 빈 페이지를 반환해야 한다.")
        void find_whenNotChannelId_Empty() {
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);

            given(messageRepository.findAllByChannelIdWithAuthor(any(UUID.class), any(Instant.class),
                    any(Pageable.class)))
                    .willReturn(emptySlice);

            messageService.findAllByChannelId(channelId, null, pageable);

            then(messageRepository).should().findAllByChannelIdWithAuthor(any(), any(), any());
            then(pageResponseMapper).should().fromSlice(any(Slice.class), any());
        }
    }

    @Test
    @DisplayName("메시지 상세 조회 성공")
    void find_MessageSuccess() {
        Message message = new Message("content", null, null, List.of());
        UUID id = UUID.randomUUID();
        ReflectionTestUtils.setField(message, "id", id);

        MessageDto mockDto = new MessageDto(id, Instant.now(), Instant.now(), "content", null, null, List.of());

        given(messageRepository.findById(any())).willReturn(Optional.of(message));
        given(messageMapper.toDto(any(Message.class))).willReturn(mockDto);

        MessageDto result = messageService.find(id);

        assertNotNull(result);
        assertEquals("content", result.content());
        assertEquals(id, result.id());
        then(messageRepository).should().findById(id);
    }

    @Test
    @DisplayName("메시지 조회 실패 시 예외 발생")
    void find_MessageFail_ThrowsException() {
        given(messageRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> {
            messageService.find(UUID.randomUUID());
        });
    }
}
