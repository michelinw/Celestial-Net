package unsw.blackout;

import unsw.utils.Angle;
import java.util.ArrayList;

import static unsw.utils.MathsHelper.CLOCKWISE;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;

public class TeleportingSatellite extends Satellite {
    private static final double SAT_LINEAR_VELOCITY = 1000;
    private static final String[] SUPPORT_DEVICE = {
            "HandheldDevice", "LaptopDevice", "DesktopDevice"
    };
    private static final int MAX_FILES = Integer.MAX_VALUE;
    private static final int MAX_STORAGE = 200;
    private static final int MAX_RECV_BANDWIDTH = 15;
    private static final int MAX_SEND_BANDWIDTH = 10;
    private static final int MAX_COMM_RANGE = 200000;
    private boolean teleported;

    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        this.setLinearVelocity(SAT_LINEAR_VELOCITY);
        this.setSupportedDevices(SUPPORT_DEVICE);
        this.setMaxFiles(MAX_FILES);
        this.setMaxRecvBandwidth(MAX_RECV_BANDWIDTH);
        this.setMaxSendBandwidth(MAX_SEND_BANDWIDTH);
        this.setMaxStorage(MAX_STORAGE);
        this.setMaxRange(MAX_COMM_RANGE);
        this.setDirection(ANTI_CLOCKWISE);
        this.teleported = false;
    }

    public boolean getTeleported() {
        return teleported;
    }

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    public void simulate() {
        Angle currentAngle = this.getPosition();
        Angle angularVelocity = this.getAngularVelocity();
        Angle newAngle = new Angle();

        this.setTeleported(false);

        if (this.getDirection() == CLOCKWISE) {
            newAngle = currentAngle.subtract(angularVelocity);
            if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
                newAngle = newAngle.add(Angle.fromDegrees(360));
            }
            if (newAngle.compareTo(Angle.fromDegrees(180)) != 1
                    && currentAngle.compareTo(Angle.fromDegrees(180)) == 1) {
                newAngle = Angle.fromRadians(0.0);
                this.setDirection(ANTI_CLOCKWISE);
                this.setTeleported(true);
            }
        } else {
            newAngle = this.getPosition().add(angularVelocity);
            if (newAngle.compareTo(Angle.fromDegrees(360)) == 1) {
                newAngle = newAngle.subtract(Angle.fromDegrees(360));
            }
            if (newAngle.compareTo(Angle.fromDegrees(180)) != -1
                    && currentAngle.compareTo(Angle.fromDegrees(180)) == -1) {
                newAngle = Angle.fromDegrees(360);
                this.setDirection(CLOCKWISE);
                this.setTeleported(true);
            }
        }
        this.setPosition(newAngle);
        if (this.getTeleported()) {
            this.teleportTransfer();
        } else {
            this.normalTransferFile(this);
        }
    }

    // deal with Sat-Sat and Sat-Dev
    private void teleportTransfer() {
        FileManager fm = this.getFileManager();

        // recv file should be excluded from device
        ArrayList<File> recvFiles = fm.getInProgressFiles("RECV");
        if (!recvFiles.isEmpty()) {
            for (File file : recvFiles) {
                BaseEntity relatedEntity = file.getRelatedBaseEntity();
                // only considering receive from Satellite
                if (relatedEntity instanceof Satellite) {

                    // all files will be tranfered by removing remaing "t"
                    String fileContent = file.getContent();
                    String doneContent = fileContent.substring(0, file.getCompletedSize());
                    String remainingContent = fileContent.substring(file.getCompletedSize());
                    remainingContent = remainingContent.replace("t", "");

                    file.setContent(doneContent + remainingContent);
                    file.setCompletedSize(file.getContent().length());
                    fm.updateFile(file.getFilename(), file);
                } else {
                    // if its from device, remove the file from sat
                    fm.removeFile(file.getFilename());
                }
            }
        }

        // send file fully considered
        ArrayList<File> sendFiles = fm.getInProgressFiles("SEND");
        if (!sendFiles.isEmpty()) {
            for (File file : sendFiles) {
                String fileContent = file.getContent();
                String doneContent = fileContent.substring(0, file.getCompletedSize());
                String remainingContent = fileContent.substring(file.getCompletedSize());
                remainingContent = remainingContent.replace("t", "");
                file.setContent(doneContent + remainingContent);
                file.setCompletedSize(file.getContent().length());
                fm.updateFile(file.getFilename(), file);
            }
        }
    }
}
