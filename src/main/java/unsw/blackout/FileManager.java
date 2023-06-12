package unsw.blackout;

// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import unsw.response.models.FileInfoResponse;

public class FileManager {
    private Map<String, FileInfoResponse> files;


    public FileManager() {
        files = new HashMap<>();
    }

    public void addFile(File file) {
        files.put(file.getFilename(), new FileInfoResponse(file.getFilename(), file.getData(), file.getFileSize(),
                file.getIsFileComplete()));
    }

    // public void removeFile(String filename) {
    //     // Find the file with the specified name and remove it
    //     for (int i = 0; i < files.size(); i++) {
    //         File file = files.get(i);
    //         if (file.getFilename().equals(filename)) {
    //             files.remove(i);
    //             break;
    //         }
    //     }
    // }

    public Map<String, FileInfoResponse> getFiles() {
        return files;
    }

    // public File getFile(String filename) {
    //     // Find the file with the specified name and return it
    //     for (File file : files) {
    //         if (file.getFilename().equals(filename)) {
    //             return file;
    //         }
    //     }
    //     return null;
    // }
}
