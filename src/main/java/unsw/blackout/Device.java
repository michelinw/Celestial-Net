package unsw.blackout;

import java.util.ArrayList;

import unsw.utils.Angle;

public class Device extends BaseEntity {
    public Device(String deviceId, String type, Angle position) {
        super(deviceId, type, 69911, position);
        this.setMaxFiles(Integer.MAX_VALUE);
        this.setMaxRecvBandwidth(Integer.MAX_VALUE);
        this.setMaxSendBandwidth(Integer.MAX_VALUE);
        this.setMaxStorage(Integer.MAX_VALUE);
    }

    public void addFile(File file) {
        FileManager fileManager = this.getFileManager();
        fileManager.addFile(file);
    }

    @Override
    public int remainingBandwidth(String direction) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasStorage(int newFileSize) {
        return true;
    }

    @Override
    public void simulate() {
        this.normalTransferFile(this);
        this.deviceToTeleportSatTransfer();

    }

    /**
     * Special file transfer for teleporting satellites that will change file
     * content if teleportation occurs during transfer.
     */
    private void deviceToTeleportSatTransfer() {
        FileManager fileManager = this.getFileManager();
        ArrayList<File> sendFiles = fileManager.getInProgressFiles("SEND");
        if (!sendFiles.isEmpty()) {
            for (File file : sendFiles) {
                BaseEntity entity = file.getRelatedBaseEntity();

                // checking if teleportsat is teleported. If yes, then reset device file
                if (entity instanceof TeleportingSatellite && ((TeleportingSatellite) entity).getTeleported()) {
                    String content = file.getContent();
                    content = content.replace("t", "");
                    file.setContent(content);
                    file.setCompletedSize(0);
                    file.setRelatedBaseEntity(null);
                    file.setDirection("");
                    fileManager.updateFile(file.getFilename(), file);
                }
            }
        }
    }
}
