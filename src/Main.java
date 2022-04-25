import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Worker worker = new Worker();
        File mainDirectory = new File("C://Games");
        File srcDir = new File(mainDirectory, "src");
        File resDir = new File(mainDirectory, "res");
        File saveGamesDir = new File(mainDirectory, "savegames");
        File tempDir = new File(mainDirectory, "temp");
        worker.createDir(srcDir);
        worker.createDir(resDir);
        worker.createDir(saveGamesDir);
        worker.createDir(tempDir);
        File mainDir = new File(srcDir, "main");
        File testDir = new File(srcDir, "test");
        worker.createDir(mainDir);
        worker.createDir(testDir);
        File mainFile = new File(mainDir, "Main.java");
        File utilsFile = new File(mainDir, "Utils.java");
        worker.createFile(mainFile);
        worker.createFile(utilsFile);
        File drawablesDir = new File(resDir, "drawables");
        File vectorsDir = new File(resDir, "vectors");
        File iconsDir = new File(resDir, "icons");
        worker.createDir(drawablesDir);
        worker.createDir(vectorsDir);
        worker.createDir(iconsDir);
        File tempFile = new File(tempDir, "temp.txt");
        worker.createFile(tempFile);
        worker.getLogs(tempFile);

        GameProgress gp1 = new GameProgress(100, 1, 20, 2.0);
        GameProgress gp2 = new GameProgress(150, 3, 25, 2.5);
        GameProgress gp3 = new GameProgress(175, 2, 26, 3.1);
        File save1 = new File(saveGamesDir, "save_1.dat");
        File save2 = new File(saveGamesDir, "save_2.dat");
        File save3 = new File(saveGamesDir, "save_3.dat");

        worker.saveGame(save1, gp1);
        worker.saveGame(save2, gp2);
        worker.saveGame(save3, gp3);
        worker.getLogs(tempFile);


        File zipArch = new File(saveGamesDir, "saves.zip");
        worker.zipSaves(zipArch, save1, save2, save3);
        worker.deleteFile(save1);
        worker.deleteFile(save2);
        worker.deleteFile(save3);
        worker.getLogs(tempFile);

        worker.unZipSaves(zipArch);
        FilenameFilter filenameFilter = (dir, name) -> name.endsWith("dat");
        for (File file : saveGamesDir.listFiles(filenameFilter)) {
            System.out.println(worker.readFile(file));
        }

        worker.getLogs(tempFile);

    }

}
