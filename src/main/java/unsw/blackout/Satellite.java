package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;
import static unsw.utils.MathsHelper.CLOCKWISE;

public abstract class Satellite extends BaseEntity {
    private int direction;
    private double linearVelocity;
    private String[] supportedDevices;

    public Satellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        this.direction = CLOCKWISE;
    }

    public Angle getAngularVelocity() {
        return Angle.fromRadians(this.linearVelocity / this.getHeight());
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void standardSatMovement() {
        Angle currentAngle = this.getPosition();
        Angle newAngle = currentAngle.subtract(this.getAngularVelocity());

        if (newAngle.compareTo(Angle.fromDegrees(0)) == -1) {
            newAngle = newAngle.add(Angle.fromDegrees(360));
        }

        this.setPosition(newAngle);
    }

    public boolean supportsDevice(Device device) {
        boolean isSupported = false;
        for (String supportedDevice : this.supportedDevices) {
            if (supportedDevice.equals(device.getType())) {
                isSupported = true;
                break;
            }
        }
        return isSupported;
    }

    @Override
    public int remainingBandwidth(String direction) {
        FileManager fm = this.getFileManager();
        int fileCount = fm.getInProgressFileCount(direction);
        if (direction.equals("SEND")) {
            if (fileCount < this.getMaxSendBandwidth()) {
                return (int) (this.getMaxSendBandwidth() / (fileCount + 1));
            } else {
                return 0;
            }
        } else if (direction.equals("RECV")) {
            if (fileCount < this.getMaxRecvBandwidth()) {
                return (int) (this.getMaxRecvBandwidth() / (fileCount + 1));
            } else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public boolean hasStorage(int newFileSize) {
        FileManager fm = this.getFileManager();
        int totalFileCount = fm.getFiles().size();
        if (totalFileCount < this.getMaxFiles()) {
            if (fm.getTotalFileSize() + newFileSize < this.getMaxStorage()) {
                return true;
            }
        }
        return false;
    }

    public boolean isVisible(Device device) {
        return MathsHelper.isVisible(this.getHeight(), this.getPosition(), device.getPosition());
    }

    public boolean isVisible(Satellite otherSatellite) {
        return MathsHelper.isVisible(this.getHeight(), this.getPosition(), otherSatellite.getHeight(),
                otherSatellite.getPosition());
    }

    public void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public void setSupportedDevices(String[] supportedDevices) {
        this.supportedDevices = supportedDevices;
    }

    public double getLinearVelocity() {
        return linearVelocity;
    }

    public String[] getSupportedDevices() {
        return supportedDevices;
    }

    public abstract void simulate();

    public boolean supportSatellite(Satellite satellite) {
        if (this instanceof ElephantSatellite) {
            return !(satellite instanceof TeleportingSatellite);
        } else if (this instanceof TeleportingSatellite) {
            return !(satellite instanceof ElephantSatellite);
        }
        return true;
    }
}
