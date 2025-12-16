//package com.sprint.mission.discodeit.repository.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public class FileChannelRepository extends FileRepository<Channel> implements ChannelRepository {
//
//
//    public FileChannelRepository() {
//        super("src/main/java/com/sprint/mission/discodeit/service/data/channel.ser");
//    }
//
//    @Override
//    public void add(Channel channel) {
//        getFile().put(channel.getId(), channel);
//        saveFile();
//    }
//
//    @Override
//    public List<Channel> findAll() {
//        return getFile().values().stream().toList();
//    }
//
//
//    @Override
//    public Channel findId(UUID channelId) {
//        boolean find = getFile().containsKey(channelId);
//        if (find) {
//            saveFile();
//            return getFile().get(channelId);
//        } else return null;
//    }
//
//    @Override
//    public void remove(UUID channelId) {
//        getFile().remove(channelId);
//        saveFile();
//    }
//
//
//}
