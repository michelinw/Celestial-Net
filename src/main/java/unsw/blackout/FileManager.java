package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;

public class FileManager {
    private Map<String, File> filesMap;

    public FileManager() {
        filesMap = new HashMap<>();
    }

    public void addFile(File file) {
        filesMap.put(file.getFilename(), file);
    }

    public File getFile(String fileName) {
        return filesMap.get(fileName);
    }

    public Map<String, FileInfoResponse> getFiles() {
        Map<String, FileInfoResponse> files = new HashMap<String, FileInfoResponse>();
        for (String key : filesMap.keySet()) {
            File file = filesMap.get(key);
            files.put(file.getFilename(), new FileInfoResponse(file.getFilename(), file.getData(), file.getFileSize(),
                    file.getIsFileComplete()));
        }
        return files;
    }

    public ArrayList<File> getFilesList() {
        ArrayList<File> filesList = new ArrayList<File>();
        for (String key : filesMap.keySet()) {
            filesList.add(filesMap.get(key));
        }

        return filesList;
    }

    public File updateFile(String fileName, File file) {
        return filesMap.put(fileName, file);
    }

    public void removeFile(String fileName) {
        filesMap.remove(fileName);
    }

    public ArrayList<File> getInProgressFiles(String direction) {
        ArrayList<File> arrayList = new ArrayList<File>();
        for (String key : filesMap.keySet()) {
            File file = filesMap.get(key);
            if (file.getDirection().equals(direction) && !file.getIsFileComplete()
                    && !"transient".equals(file.getStatus())) {
                arrayList.add(file);
            }
        }
        return arrayList;
    }

    public int getInProgressFileCount(String direction) {
        return this.getInProgressFiles(direction).size();
    }

    public ArrayList<File> getInTransientFiles() {
        ArrayList<File> arrayList = new ArrayList<File>();
        for (String key : filesMap.keySet()) {
            File file = filesMap.get(key);
            if (!file.getIsFileComplete() && "transient".equals(file.getStatus())) {
                arrayList.add(file);
            }
        }
        return arrayList;
    }

    public int getTotalFileSize() {
        int size = 0;
        for (String key : filesMap.keySet()) {
            File file = filesMap.get(key);
            size += file.getFileSize();
        }
        return size;
    }

    public int getNonTransientFileSize() {
        int size = 0;
        for (String key : filesMap.keySet()) {
            File file = filesMap.get(key);
            if (!file.getStatus().equals("transient")) {
                size += file.getFileSize();
            }
        }
        return size;
    }
}
