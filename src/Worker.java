import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Worker {
    private StringBuilder logs;

    public Worker() {
        this.logs = new StringBuilder();
    }

    public void createDir(File file) {
        if (file.mkdir()) {
            logs.append("Каталог ").append(file.getName()).append(" был создан успешно \n");
        } else {
            logs.append("Не удалось создать каталог ").append(file.getName()).append("\n");
        }
    }

    public void createFile(File file) {
        try {
            if (file.createNewFile()) {
                logs.append("Файл ").append(file.getName()).append(" был создан успешно\n");
            }
        } catch (IOException e) {
            logs.append("Не удалось создать файл ").append(file.getName())
                    .append(" по причине ").append(e.getMessage()).append("\n");
        }
    }

    public void deleteFile(File file) {
        if (file.delete()) {
            logs.append("Файл ").append(file.getName()).append(" был удален \n");
        } else {
            logs.append("Не удалось удалить файл ").append(file.getName()).append("\n");
        }
    }

    public void getLogs(File file) {
        String stringLogs = logs.toString();
        logs.delete(0, logs.length());
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(stringLogs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame(File path, GameProgress gp) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
            logs.append("Создан файл сохранения ").append(path.getName()).append("\n");
        } catch (IOException e) {
            logs.append("Не удалось создать файл сохранения ").append(path.getName())
                    .append(" по причине ").append(e.getMessage()).append("\n");
        }
    }

    public void zipSaves(File zipPath, File... path) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            for (File file : path) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zos.write(buffer);
                    zos.closeEntry();
                    logs.append("Файл сохранения ").append(file.getName())
                            .append(" добавлен в ").append(zipPath.getName()).append("\n");
                } catch (IOException e) {
                    logs.append("Не удалось добавить файл сохранения ").append(file.getName())
                            .append(" в ").append(zipPath.getName()).append(" по причине ")
                            .append(e.getMessage()).append("\n");
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        logs.append("Не удалось добавить файл сохранения ").append(file.getName())
                                .append(" в ").append(zipPath.getName()).append(" по причине ")
                                .append(e.getMessage()).append("\n");
                    }
                }

            }
        } catch (IOException e) {
            logs.append("Не удалось создать архив ").append(zipPath.getName())
                    .append(" по причине ").append(e.getMessage()).append("\n");
        }
    }

    public void unZipSaves(File zipPath) {
        try (FileInputStream fis = new FileInputStream(zipPath);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fos = new FileOutputStream(zipPath.getParent() + "/" + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
                logs.append("Файл ").append(name).append(" извлечен из архива ")
                        .append(zipPath.getName()).append("\n");
            }
        } catch (IOException e) {
            logs.append("Не удалось извлечь из архива ").append(zipPath.getName())
                    .append(" по причине ").append(e.getMessage()).append("\n");
        }
    }

    public String readFile(File file) {
        GameProgress gameProgress;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
            logs.append("Файл ").append(file.getName()).append(" прочитан\n");
        } catch (IOException | ClassNotFoundException e) {
            logs.append("Файл ").append(file.getName()).append(" не прочитан по причине ")
                    .append(e.getMessage()).append("\n");
            return e.getMessage();
        }
        return gameProgress.toString();
    }
}
