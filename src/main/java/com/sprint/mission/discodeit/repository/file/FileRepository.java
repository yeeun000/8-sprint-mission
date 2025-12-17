package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public abstract class FileRepository<T> {
    private Map<UUID, T> duplication = new HashMap<>();
    private File file;

    public FileRepository(String filePath) {
        this.file = new File(filePath);
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
            System.out.println(" 불러오기 오류");
        }
    }

    public void saveFile() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(duplication);
        } catch (IOException e) {
            System.out.println("저장 오류");
        }
    }

    protected Map<UUID, T> getFile() {
        return duplication;
    }

}
