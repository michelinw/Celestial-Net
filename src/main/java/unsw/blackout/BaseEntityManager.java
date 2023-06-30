package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;

public class BaseEntityManager {
    private ArrayList<BaseEntity> entities;

    public BaseEntityManager() {
        entities = new ArrayList<BaseEntity>();
    }

    public void addEntity(BaseEntity entity) {
        entities.add(entity);
    }

    public void removeEntity(String id) {
        for (BaseEntity entity : entities) {
            if (entity.getId().equals(id)) {
                entities.remove(entity);
                break;
            }
        }
    }

    public ArrayList<BaseEntity> getEntities() {
        return entities;
    }

    public BaseEntity getEntity(String id) {
        for (BaseEntity entity : entities) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        Satellite satellite = null;

        switch (type) {
        case "StandardSatellite":
            satellite = new StandardSatellite(satelliteId, type, height, position);
            break;
        case "RelaySatellite":
            satellite = new RelaySatellite(satelliteId, type, height, position);
            break;
        case "TeleportingSatellite":
            satellite = new TeleportingSatellite(satelliteId, type, height, position);
            break;
        case "ElephantSatellite":
            satellite = new ElephantSatellite(satelliteId, type, height, position);
            break;
        default:
            break;
        }

        if (satellite != null) {
            this.addEntity(satellite);
        }
    }

    public void addSatellite(Satellite satellite) {
        this.addEntity(satellite);
    }

    public void removeSatellite(String satelliteId) {
        this.removeEntity(satelliteId);
    }

    public ArrayList<Satellite> getSatellites() {
        ArrayList<Satellite> satellites = new ArrayList<Satellite>();

        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Satellite) {
                satellites.add((Satellite) entity);
            }
        }

        return satellites;
    }

    public Satellite getSatellite(String satelliteId) {
        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Satellite && entity.getId().equals(satelliteId)) {
                return (Satellite) entity;
            }
        }
        return null;
    }

    public List<String> getSatelliteIds() {
        List<String> satelliteIds = new ArrayList<>();
        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Satellite) {
                satelliteIds.add(entity.getId());
            }
        }
        return satelliteIds;
    }

    public void createDevice(String deviceId, String type, Angle position) {
        Device device = null;
        switch (type) {
        case "HandheldDevice":
            device = new HandheldDevice(deviceId, type, position);
            break;
        case "DesktopDevice":
            device = new DesktopDevice(deviceId, type, position);
            break;
        case "LaptopDevice":
            device = new LaptopDevice(deviceId, type, position);
            break;
        default:
            break;
        }

        if (device != null) {
            this.addEntity(device);
        }
    }

    public void addDevice(Device device) {
        this.addEntity(device);
    }

    public void removeDevice(String deviceId) {
        this.removeEntity(deviceId);
    }

    public ArrayList<Device> getDevices() {
        ArrayList<Device> devices = new ArrayList<Device>();

        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Device) {
                devices.add((Device) entity);
            }
        }

        return devices;
    }

    public Device getDevice(String deviceId) {
        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Device && entity.getId().equals(deviceId)) {
                return (Device) entity;
            }
        }
        return null;
    }

    public List<String> getDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        for (BaseEntity entity : this.getEntities()) {
            if (entity instanceof Device) {
                deviceIds.add(entity.getId());
            }
        }
        return deviceIds;
    }
}
