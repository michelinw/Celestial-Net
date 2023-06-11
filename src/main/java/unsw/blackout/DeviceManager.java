package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

public class DeviceManager {
    private ArrayList<Device> devices;

    public DeviceManager() {
        devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void removeDevice(String deviceId) {
        // Find the device with the specified ID and remove it
        for (int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            if (device.getId().equals(deviceId)) {
                devices.remove(i);
                break;
            }
        }
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public Device getDevice(String deviceId) {
        // Find the device with the specified ID and return it
        for (Device device : devices) {
            if (device.getId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    public List<String> getDeviceIds() {
        List<String> deviceIds = new List<>();
        for (Device device : devices) {
            deviceIds.add(device.getId());
        }
        return deviceIds;
    }
}
