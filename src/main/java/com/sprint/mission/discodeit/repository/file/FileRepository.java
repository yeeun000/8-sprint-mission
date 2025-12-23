package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public abstract class FileRepository<T extends Serializable> {


    private Map<UUID, T> duplication = new HashMap<>();
    private File file;

    public FileRepository(String filePath, String fileName) {
        this.file = Paths.get(filePath, fileName).toFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        loadFromFile();
    }

    public void loadFromFile() {
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, T> list = (Map<UUID, T>) ois.readObject();
            duplication.putAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(duplication);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Map<UUID, T> getFile() {
        return duplication;
    }

}
