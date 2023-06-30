package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    private static final int MAX_RANGE = 50000;

    public HandheldDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
        this.setMaxRange(MAX_RANGE);
    }

}
