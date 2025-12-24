package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository extends FileRepository<Channel> implements ChannelRepository {


    public FileChannelRepository(
            @Value("${discodeit.repository.file-directory}") String filePath
    ) {
        super(filePath, "channel.ser");
    }

    @Override
    public Channel save(Channel channel) {
        getFile().put(channel.getId(), channel);
        saveFile();
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return getFile().values().stream().toList();
    }


    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(getFile().get(id));
    }

    @Override
    public void deleteById(UUID id) {
        getFile().remove(id);
        saveFile();
    }


}
