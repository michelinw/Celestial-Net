package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private DeviceManager devices;
    private SatelliteManager satellites;

    // creates a new device with the specified id, type and position
    public void createDevice(String deviceId, String type, Angle position) {
        protected Device device = new Device(deviceId, type, position);
        devices.addDevice(device);
    }

    public void removeDevice(String deviceId) {
        devices.removeDevice(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        protected Satellite satellite = new Satellite(satelliteId, type, height, position);
        satellites.addSatellite(satellite);
    }

    public void removeSatellite(String satelliteId) {
        sattelites.removeSatellite(satelliteId);
    }

    public List<String> listDeviceIds() {
        protected List<String> deviceIds = devices.getDevicesIds();
    }

    public List<String> listSatelliteIds() {
        protected List<String> satelliteIds = satellites.getSatellitesIds();
        return satelliteIds;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        protected Device device = devices.getDevice(deviceId);
        protected File file = new File(filename, content);
        device.addFile(file);
    }

    public EntityInfoResponse getInfo(String id) {
        return null;
    }

    public void simulate() {
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        return new ArrayList<>();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
