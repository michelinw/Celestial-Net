package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public class BlackoutController {
    private DeviceManager devices = new DeviceManager();
    private SatelliteManager satellites = new SatelliteManager();

    // creates a new device with the specified id, type and position
    public void createDevice(String deviceId, String type, Angle position) {
        Device device = new Device(deviceId, type, position);
        devices.addDevice(device);
    }

    public void removeDevice(String deviceId) {
        devices.removeDevice(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite = new Satellite(satelliteId, type, height, position);
        satellites.addSatellite(satellite);
    }

    public void removeSatellite(String satelliteId) {
        satellites.removeSatellite(satelliteId);
    }

    public List<String> listDeviceIds() {
        List<String> deviceIds = devices.getDeviceIds();
        return deviceIds;
    }

    public List<String> listSatelliteIds() {
        List<String> satelliteIds = satellites.getSatelliteIds();
        return satelliteIds;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        Device device = devices.getDevice(deviceId);
        File file = new File(filename, content);
        device.addFile(file);
    }

    public EntityInfoResponse getInfo(String id) {
        if (devices.getDeviceIds().contains(id)) {
            Device device = devices.getDevice(id);
            return new EntityInfoResponse(device.getDeviceId(), device.getPosition(), RADIUS_OF_JUPITER,
                    device.getType(), device.getFileManager().getFiles());
        } else if (satellites.getSatelliteIds().contains(id)) {
            Satellite satellite = satellites.getSatellite(id);
            return new EntityInfoResponse(satellite.getSatelliteId(), satellite.getPosition(),
                    satellite.getHeight(), satellite.getType());
        }
        return null;
    }

    public void simulate() {
        if (devices.getDeviceIds().size() == 0 && satellites.getSatelliteIds().size() == 0) {
            return;
        }

        for (Device device : devices.getDevices()) {
            device.simulate();
        }

        for (Satellite satellite : satellites.getSatellites()) {
            satellite.simulate();
        }

        // for (Device device : devices.getDevices()) {
        //     device.updateFileTransfer();
        // }
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
