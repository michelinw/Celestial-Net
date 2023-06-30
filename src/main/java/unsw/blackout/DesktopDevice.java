package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    private static final int MAX_RANGE = 200000;

    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
        this.setMaxRange(MAX_RANGE);
    }
}
