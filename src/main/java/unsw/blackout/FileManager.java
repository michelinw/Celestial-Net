package unsw.blackout;

import java.util.ArrayList;

public class FileManager {
    private ArrayList<File> files;

    public FileManager(String name, String content) {
        files = new ArrayList<>();
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(String filename) {
        // Find the file with the specified name and remove it
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (file.getFilename().equals(filename)) {
                files.remove(i);
                break;
            }
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public File getFile(String filename) {
        // Find the file with the specified name and return it
        for (File file : files) {
            if (file.getFilename().equals(filename)) {
                return file;
            }
        }
        return null;
    }

    public List<String> getFileNames() {
        List<String> fileNames = new List<>();
        for (File file : files) {
            fileNames.add(file.getFilename());
        }
        return fileNames;
    }
}
