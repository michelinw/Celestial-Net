package unsw.blackout;

import java.util.ArrayList;

import unsw.utils.Angle;
import static unsw.utils.MathsHelper.getDistance;
import static unsw.utils.MathsHelper.isVisible;

public abstract class BaseEntity {
    private String id;
    private double height;
    private String type;
    private Angle position;
    private int maxRange;
    private FileManager fileManager;
    private int maxFiles;
    private int maxStorage;
    private int maxRecvBandwidth;
    private int maxSendBandwidth;

    public BaseEntity(String id, String type, double height, Angle position) {
        this.id = id;
        this.type = type;
        this.height = height;
        this.position = position;
        this.fileManager = new FileManager();
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return position;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxRange(int range) {
        this.maxRange = range;
    }

    public ArrayList<File> getFiles() {
        return fileManager.getFilesList();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public abstract int remainingBandwidth(String direction);

    public abstract boolean hasStorage(int newFileSize);

    public abstract void simulate();

    /**
     * Method to determine whether this entity is in range of another sender
     *
     * @param sender
     * @return
     */
    public boolean isCommunicable(BaseEntity sender) {
        // Device is sending to a satellite)
        if (sender instanceof Device) {
            return (getDistance(this.getHeight(), this.getPosition(), sender.getPosition()) < sender.getMaxRange()
                    && isVisible(this.getHeight(), this.getPosition(), sender.getPosition()));
            // Satellite is sending to a device
        } else if (sender instanceof Satellite && this instanceof Device) {
            return (getDistance(sender.getHeight(), sender.getPosition(), this.getPosition()) < sender.getMaxRange()
                    && isVisible(sender.getHeight(), sender.getPosition(), this.getPosition()));
            // Satellite is sending to a satellite
        } else {
            return (getDistance(this.getHeight(), this.getPosition(), sender.getHeight(), sender.getPosition()) < sender
                    .getMaxRange()
                    && (isVisible(this.getHeight(), this.getPosition(), sender.getHeight(), sender.getPosition())));
        }
    }

    protected void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    protected void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    protected void setMaxRecvBandwidth(int maxRecvBandwidth) {
        this.maxRecvBandwidth = maxRecvBandwidth;
    }

    protected void setMaxSendBandwidth(int maxSendBandwidth) {
        this.maxSendBandwidth = maxSendBandwidth;
    }

    protected int getMaxFiles() {
        return maxFiles;
    }

    protected int getMaxStorage() {
        return maxStorage;
    }

    protected int getMaxRecvBandwidth() {
        return maxRecvBandwidth;
    }

    protected int getMaxSendBandwidth() {
        return maxSendBandwidth;
    }

    /**
     * queues a file to be sent to the toId entity. This happens before simulation.
     *
     * @param file
     * @param toId
     */
    public void queueSendFile(File file, BaseEntity toId) {
        FileManager fm = this.getFileManager();
        File file1;
        file1 = fm.getFile(file.getFilename());
        if (file1 != null & "transient".equals(file1.getStatus())) {
            // file using in elephant sate to restart transfer
            file1.setStatus("");
        } else {
            file1 = new File(file.getFilename(), file.getContent(), 0, "SEND", toId);
        }
        fm.updateFile(file1.getFilename(), file1);
    }

    /**
     * queues a file to be received by the fromId entity. This happens before
     * simulation.
     *
     * @param file
     * @param fromId
     */
    public void queueRecieveFile(File file, BaseEntity fromId) {
        FileManager fm = this.getFileManager();
        File file1;

        file1 = fm.getFile(file.getFilename());
        if (file1 != null && "transient".equals(file1.getStatus())) {
            // file using in elephant sate to restart transfer
            file1.setStatus("");
            fm.updateFile(file1.getFilename(), file1);
        } else {
            file1 = new File(file.getFilename(), file.getContent(), 0, "RECV", fromId);
            fm.addFile(file1);
        }
    }

    public boolean isFileExist(String filename) {
        FileManager fm = this.getFileManager();
        return fm.getFiles().containsKey(filename);
    }

    /**
     * If the file is transient, do not remove it and instead just add it to
     * destination. Otherwise remove if out of range. Conditions of range are
     * defined in the actual transfer file method below.
     *
     * @param satellite
     * @param fm
     * @param file
     */
    private void removeOutRangeFile(Satellite satellite, FileManager fm, File file) {
        if (satellite instanceof ElephantSatellite) {
            file.setStatus("transient");
            fm.updateFile(file.getFilename(), file);
        } else {
            fm.removeFile(file.getFilename());
        }
    }

    /**
     * transfers a file with the related entity (can be receiving or sending files)
     *
     * @param entity
     */
    protected void normalTransferFile(BaseEntity entity) {
        FileManager fm = this.getFileManager();
        ArrayList<File> recvFiles = fm.getInProgressFiles("RECV");
        if (!recvFiles.isEmpty()) {
            int recvBandwidth = this.getMaxRecvBandwidth() / recvFiles.size();
            for (File file : recvFiles) {
                BaseEntity fromEntity = file.getRelatedBaseEntity();

                // check out of range
                if ((entity instanceof Device) && (fromEntity instanceof Satellite)) {
                    Satellite satellite = (Satellite) fromEntity;
                    if (!satellite.isVisible((Device) entity)) {
                        removeOutRangeFile(satellite, fm, file);
                        continue;
                    }
                }

                if ((entity instanceof Satellite)) {
                    if (fromEntity instanceof Satellite) {
                        Satellite satellite = (Satellite) fromEntity;
                        if (!satellite.isVisible((Satellite) entity)) {
                            removeOutRangeFile(satellite, fm, file);
                            continue;
                        }
                    }
                    if (fromEntity instanceof Device) {
                        Satellite satellite = (Satellite) entity;
                        if (!satellite.isVisible((Device) fromEntity)) {
                            removeOutRangeFile(satellite, fm, file);
                            continue;
                        }
                    }
                }

                // checking upload and download Bandwidth limit
                if (fromEntity instanceof Satellite) {
                    FileManager entityfm = fromEntity.getFileManager();
                    int fileCount = entityfm.getInProgressFileCount("SEND");
                    if (fileCount == 0)
                        fileCount = 1;
                    int sendBandwidth = ((Satellite) fromEntity).getMaxSendBandwidth() / fileCount;
                    recvBandwidth = Math.min(sendBandwidth, recvBandwidth);
                }

                int newSize = file.getCompletedSize() + recvBandwidth;
                if (newSize > file.getFileSize()) {
                    newSize = file.getFileSize();
                }
                file.setCompletedSize(newSize);
                fm.updateFile(file.getFilename(), file);

                FileManager entityfm = fromEntity.getFileManager();
                File fromFile = entityfm.getFile(file.getFilename());
                fromFile.setCompletedSize(newSize);
                entityfm.updateFile(fromFile.getFilename(), fromFile);
            }
        }

        ArrayList<File> sendFiles = fm.getInProgressFiles("SEND");
        if (!sendFiles.isEmpty()) {
            int sendBandwidth = this.getMaxSendBandwidth() / sendFiles.size();
            for (File file : sendFiles) {
                BaseEntity toEntity = file.getRelatedBaseEntity();

                // checking upload and download speed limit
                if (toEntity instanceof Satellite) {
                    FileManager relatedEntityfm = toEntity.getFileManager();
                    int fileCount = relatedEntityfm.getInProgressFileCount("RECV");
                    if (fileCount == 0)
                        fileCount = 1;
                    int relatedEntityBandwidth = ((Satellite) toEntity).getMaxSendBandwidth() / fileCount;
                    sendBandwidth = Math.min(relatedEntityBandwidth, sendBandwidth);
                }

                int newSize = file.getCompletedSize() + sendBandwidth;
                if (newSize > file.getFileSize()) {
                    newSize = file.getFileSize();
                }
                file.setCompletedSize(newSize);
                fm.updateFile(file.getFilename(), file);

                FileManager relatedEntityfm = toEntity.getFileManager();
                File relatedFile = relatedEntityfm.getFile(file.getFilename());
                relatedFile.setCompletedSize(newSize);
                relatedEntityfm.updateFile(relatedFile.getFilename(), relatedFile);
            }
        }

    }
}
