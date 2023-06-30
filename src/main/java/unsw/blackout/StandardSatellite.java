package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    private static final double SAT_LINEAR_VELOCITY = 2500;
    private static final String[] SUPPORT_DEVICE = {
            "HandheldDevice", "LaptopDevice"
    };
    private static final int MAX_FILES = 3;
    private static final int MAX_STORAGE = 80;
    private static final int MAX_RECV_BANDWIDTH = 1;
    private static final int MAX_SEND_BANDWIDTH = 1;
    private static final int MAX_COMM_RANGE = 150000;

    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        this.setLinearVelocity(SAT_LINEAR_VELOCITY);
        this.setSupportedDevices(SUPPORT_DEVICE);
        this.setMaxFiles(MAX_FILES);
        this.setMaxRecvBandwidth(MAX_RECV_BANDWIDTH);
        this.setMaxSendBandwidth(MAX_SEND_BANDWIDTH);
        this.setMaxStorage(MAX_STORAGE);
        this.setMaxRange(MAX_COMM_RANGE);
    }

    public void simulate() {
        this.standardSatMovement();
        this.normalTransferFile(this);
    }
}
