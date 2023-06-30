package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
    private static final int MAX_RANGE = 100000;

    public LaptopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
        this.setMaxRange(MAX_RANGE);
    }
}
