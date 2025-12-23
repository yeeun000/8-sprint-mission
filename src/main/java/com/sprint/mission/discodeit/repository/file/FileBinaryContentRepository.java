package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository extends FileRepository<BinaryContent> implements BinaryContentRepository {

    public FileBinaryContentRepository(
            @Value("${discodeit.repository.file-directory}") String filePath
    ) {
        super(filePath, "binaryContent.ser");
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        getFile().put(binaryContent.getId(), binaryContent);
        saveFile();
        return binaryContent;
    }


    @Override
    public void deleteById(UUID id) {
        getFile().remove(id);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(getFile().get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return getFile().values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();

    }
}
