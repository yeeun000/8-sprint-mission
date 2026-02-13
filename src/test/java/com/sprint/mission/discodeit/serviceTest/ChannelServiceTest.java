package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.dto.channelDTO.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDTO.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDTO.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PUBLIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChannelService 테스트")
public class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ChannelMapper channelMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private BasicChannelService channelService;

    @Captor
    private ArgumentCaptor<Channel> channelCaptor;

    @BeforeEach
    @DisplayName("테스트 환경 설정 확인")
    void setUp() {
        assertNotNull(channelRepository);
        assertNotNull(channelMapper);
        assertNotNull(userRepository);
        assertNotNull(readStatusRepository);
        assertNotNull(messageRepository);
        assertNotNull(channelService);
    }

    @Nested
    @DisplayName("생성 테스트")
    class CreateTest {

        @Test
        @DisplayName("Public 채널을 생성할 수 있다")
        void create_PublicChannel_ChannelSuccess() {
            PublicChannelCreateRequest request = new PublicChannelCreateRequest(
                    "PUBLIC",
                    "설명"
            );

            given(channelRepository.save(any(Channel.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            ChannelDto fakeDto = new ChannelDto(
                    UUID.randomUUID(),
                    PUBLIC,
                    "test",
                    "test",
                    List.of(),
                    null
            );

            given(channelMapper.toDto(any(Channel.class))).willReturn(fakeDto);

            ChannelDto result = channelService.create(request);

            assertNotNull(result);

            then(channelRepository).should().save(channelCaptor.capture());
            Channel savedChannel = channelCaptor.getValue();

            assertEquals("PUBLIC", savedChannel.getName());
            assertEquals("설명", savedChannel.getDescription());
            assertEquals(Channel.ChannelType.PUBLIC, savedChannel.getType());
        }

        @Test
        @DisplayName("Private 채널을 생성할 수 있다")
        void create_PrivateChannel_ChannelSuccess() {
            UUID id = UUID.randomUUID();
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
                    List.of(id)
            );
            User user = mock(User.class);
            given(userRepository.findById(id)).willReturn(Optional.of(user));
            given(channelRepository.save(any(Channel.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            ChannelDto fakeDto = new ChannelDto(
                    UUID.randomUUID(),
                    PRIVATE,
                    null,
                    null,
                    List.of(),
                    null
            );

            given(channelMapper.toDto(any(Channel.class))).willReturn(fakeDto);

            ChannelDto result = channelService.create(request);

            assertNotNull(result);

            then(channelRepository).should().save(channelCaptor.capture());
            Channel savedChannel = channelCaptor.getValue();

            assertEquals(PRIVATE, savedChannel.getType());
        }

        @Test
        @DisplayName("유저 아이디가 없으면 Private 채널 생성을 실패해야 한다")
        void create_whenUserIdNotExist_ChannelFail() {
            UUID userId = UUID.randomUUID();
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
                    List.of(userId)
            );

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> channelService.create(request),
                    "유저를 찾을 수 없습니다."
            );

            assertEquals("유저를 찾을 수 없습니다.", exception.getMessage());

        }

    }

    @Nested
    @DisplayName("수정 테스트")
    class UpdateTest {

        @Test
        @DisplayName("채널 제목을 수정할 수 있다.")
        void update_whenWriteChannelName_ChannelSuccess() {
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
                    "new",
                    null
            );
            Channel channel = new Channel(PUBLIC, "old", "description");
            UUID channelId = channel.getId();

            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

            channelService.update(channelId, request);

            assertEquals("new", channel.getName());
            assertEquals("description", channel.getDescription());
        }

        @Test
        @DisplayName("채널 아이디가 없으면 실패해야 한다.")
        void update_whenChannelIdNotExist_ChannelFail() {
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
                    "new",
                    null
            );
            UUID channelId = UUID.randomUUID();

            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            ChannelNotFoundException exception = assertThrows(
                    ChannelNotFoundException.class,
                    () -> channelService.update(channelId, request),
                    "채널을 찾을 수 없습니다."

            );
            assertEquals("채널을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("Private 채널은 수정할 경우 실패해야 한다.")
        void update_PrivateChannel_ThrowsException() {
            UUID channelId = UUID.randomUUID();
            Channel privateChannel = Channel.createPrivateChannel();
            ReflectionTestUtils.setField(privateChannel, "id", channelId);

            given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(
                    "newName",
                    "newDesc");

            assertThrows(PrivateChannelUpdateException.class, () -> {
                channelService.update(channelId, request);
            });
        }
    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

        @Test
        @DisplayName("채널 아이디가 있으면 채널을 삭제할 수 있다.")
        void delete_whenChannelIdExists_ChannelSuccess() {
            UUID ChannelId = UUID.randomUUID();

            given(channelRepository.existsById(ChannelId)).willReturn(true);

            channelService.delete(ChannelId);

            then(channelRepository).should().deleteById(ChannelId);
        }

        @Test
        @DisplayName("채널 아이디가 없으면 실패해야한다.")
        void delete_whenNotChannelIdExists_ChannelFail() {
            UUID ChannelId = UUID.randomUUID();

            given(channelRepository.existsById(ChannelId)).willReturn(false);

            ChannelNotFoundException exception = assertThrows(
                    ChannelNotFoundException.class,
                    () -> channelService.delete(ChannelId),
                    "채널을 찾을 수 없습니다."
            );

            assertEquals("채널을 찾을 수 없습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("유저아이디 찾기 테스트")
    class FindByUserIdTest {

        @Test
        @DisplayName("유저아이디가 있으면 찾을 수 있다.")
        void find_whenAllByUserId_ChannelSuccess() {
            User user = new User("username", "email", "password", null);
            Channel channel = new Channel(PUBLIC, "name", "description");

            given(readStatusRepository.findAllByUserId(user.getId()))
                    .willReturn(List.of(new ReadStatus(user, channel, Instant.now())));
            given(channelRepository.findAllByTypeOrIdIn(any(ChannelType.class), anyList()))
                    .willReturn(List.of(channel));

            ChannelDto channelDto = new ChannelDto(
                    channel.getId(),
                    PUBLIC,
                    "name",
                    "description",
                    List.of(),
                    null);
            given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

            List<ChannelDto> result = channelService.findAllByUserId(user.getId());

            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("유저 아이디가 없으면 빈 목록을 반환햐애 한다.")
        void find_whenNotUserId_EmptyChannel() {
            UUID userId = UUID.randomUUID();

            given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
            given(channelRepository.findAllByTypeOrIdIn(any(ChannelType.class), anyList()))
                    .willReturn(List.of());

            List<ChannelDto> result = channelService.findAllByUserId(userId);

            assertTrue(result.isEmpty());
        }
    }

    @Test
    @DisplayName("채널 상세 조회 성공")
    void find_ChannelSuccess() {
        UUID id = UUID.randomUUID();
        Channel channel = Channel.createPublicChannel("name", "desc");

        ChannelDto mockDto = new ChannelDto(
                id,
                PUBLIC,
                "name",
                "desc",
                List.of(),
                Instant.now()
        );

        given(channelRepository.findById(id)).willReturn(Optional.of(channel));
        given(channelMapper.toDto(channel)).willReturn(mockDto);

        ChannelDto result = channelService.find(id);

        assertNotNull(result);
        assertEquals("name", result.name());
        assertEquals(id, result.id());

        then(channelRepository).should().findById(id);
    }
}