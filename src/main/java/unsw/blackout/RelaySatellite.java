package unsw.blackout;

import unsw.utils.Angle;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;
import static unsw.utils.MathsHelper.CLOCKWISE;

public class RelaySatellite extends Satellite {
    private static final double SAT_LINEAR_VELOCITY = 1500;
    private static final String[] SUPPORT_DEVICE = {
            "HandheldDevice", "LaptopDevice"
    };
    private static final int MAX_FILES = Integer.MAX_VALUE;
    private static final int MAX_STORAGE = Integer.MAX_VALUE;
    private static final int MAX_RECV_BANDWIDTH = Integer.MAX_VALUE;
    private static final int MAX_SEND_BANDWIDTH = Integer.MAX_VALUE;
    private static final int MAX_COMM_RANGE = 300000;

    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        this.setLinearVelocity(SAT_LINEAR_VELOCITY);
        this.setSupportedDevices(SUPPORT_DEVICE);
        this.setMaxFiles(MAX_FILES);
        this.setMaxRecvBandwidth(MAX_RECV_BANDWIDTH);
        this.setMaxSendBandwidth(MAX_SEND_BANDWIDTH);
        this.setMaxStorage(MAX_STORAGE);
        this.setMaxRange(MAX_COMM_RANGE);
        this.setDirection(CLOCKWISE);
    }

    public void simulate() {
        Angle currentAngle = this.getPosition();
        Angle angularVelocity = this.getAngularVelocity();
        if (currentAngle.compareTo(Angle.fromDegrees(140)) == -1
                || currentAngle.compareTo(Angle.fromDegrees(345)) != -1) {
            this.setDirection(ANTI_CLOCKWISE);
            Angle newAngle = currentAngle.add(angularVelocity);

            if (newAngle.compareTo(Angle.fromDegrees(360)) == 1) {
                newAngle = newAngle.subtract(Angle.fromDegrees(360));
            }
            this.setPosition(newAngle);
        } else if (currentAngle.compareTo(Angle.fromDegrees(345)) == -1
                && currentAngle.compareTo(Angle.fromDegrees(190)) == 1) {
            this.setDirection(CLOCKWISE);
            Angle newAngle = currentAngle.subtract(angularVelocity);
            this.setPosition(newAngle);
        } else if (this.getDirection() == CLOCKWISE) {
            this.setPosition(currentAngle.subtract(angularVelocity));
        } else {
            this.setPosition(currentAngle.add(angularVelocity));
        }
    }
}
