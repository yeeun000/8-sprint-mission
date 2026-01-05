package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public abstract class FileRepository<T extends Serializable> {


  private final Map<UUID, T> duplication = new HashMap<>();
  private final File file;

  public FileRepository(String filePath, String fileName) {
    this.file = Paths.get(filePath, fileName).toFile();
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    loadFromFile();
  }

  public void loadFromFile() {
    if (!file.exists()) {
      return;
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      Map<UUID, T> list = (Map<UUID, T>) ois.readObject();
      if (list != null) {
        duplication.putAll(list);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to load file repository: " + e.getMessage(), e);
    }
  }

  public void saveFile() {
    try (FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(duplication);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to save file repository: " + e.getMessage(), e);
    }
  }

  protected Map<UUID, T> getFile() {
    return duplication;
  }

}
