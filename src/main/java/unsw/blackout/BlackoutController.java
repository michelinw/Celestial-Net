package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.response.models.EntityInfoResponse;
// import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private BaseEntityManager entities = new BaseEntityManager();

    public void createDevice(String deviceId, String type, Angle position) {
        entities.createDevice(deviceId, type, position);
    }

    public void removeDevice(String deviceId) {
        entities.removeDevice(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        entities.createSatellite(satelliteId, type, height, position);
    }

    public void removeSatellite(String satelliteId) {
        entities.removeSatellite(satelliteId);
    }

    public List<String> listDeviceIds() {
        return entities.getDeviceIds();
    }

    public List<String> listSatelliteIds() {
        return entities.getSatelliteIds();
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        Device device = entities.getDevice(deviceId);
        File file = new File(filename, content, content.length());
        device.getFileManager().addFile(file);
    }

    public EntityInfoResponse getInfo(String id) {

        BaseEntity entity = entities.getEntity(id);
        if (entity != null) {
            return new EntityInfoResponse(entity.getId(), entity.getPosition(), entity.getHeight(), entity.getType(),
                    entity.getFileManager().getFiles());
        }
        return null;
    }

    public void simulate() {
        if (entities.getEntities().isEmpty()) {
            return;
        }

        for (BaseEntity entity : entities.getEntities()) {
            entity.simulate();
        }
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * Intended relay recursion of communicableEntities to be able to access
     * satellites or devices far away using relay satellites.
     *
     * @param communicableEntities
     * @param id
     * @return
     */

    public List<String> communicableWithRelayRecursion(List<String> communicableEntities, String id) {

        if (entities.getDeviceIds().contains(id)) {
            Device device = entities.getDevice(id);

            // device to satellite
            for (Satellite satellite : entities.getSatellites()) {
                if (satellite.isVisible(device) && satellite.supportsDevice(device)
                        && !communicableEntities.contains(satellite.getId())) {
                    communicableEntities.add(satellite.getId());
                    if (satellite.getType().equals("RelaySatellite")) {
                        communicableWithRelayRecursion(communicableEntities, id);
                    }
                }
            }
        } else {
            Satellite satellite = entities.getSatellite(id);

            for (Satellite otherSatellite : entities.getSatellites()) {
                if (!otherSatellite.getId().equals(id) && satellite.isVisible(otherSatellite)
                        && !communicableEntities.contains(otherSatellite.getId())
                        && satellite.supportSatellite(otherSatellite)) {
                    communicableEntities.add(otherSatellite.getId());
                    if (otherSatellite.getType().equals("RelaySatellite")) {
                        communicableWithRelayRecursion(communicableEntities, id);
                    }
                }
            }

            for (Device device : entities.getDevices()) {
                if (satellite.isCommunicable(device) && satellite.supportsDevice(device)
                        && !communicableEntities.contains(device.getId())) {
                    communicableEntities.add(device.getId());
                }
            }
        }

        return communicableEntities;
    }

    public List<String> communicableEntitiesInRange(String id) {
        ArrayList<String> communicableEntities = new ArrayList<String>();
        communicableWithRelayRecursion(communicableEntities, id);
        return communicableEntities;
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        BaseEntity fromEntity = entities.getEntity(fromId);
        FileManager fromFiles = fromEntity.getFileManager();

        int sendBandwidth = 0;
        int recvBandwidth = 0;

        if (fromEntity != null) {
            if (fromEntity.isFileExist(fileName)) {
                sendBandwidth = fromEntity.remainingBandwidth("SEND");
                if (sendBandwidth > 0) {
                    BaseEntity toEntity = entities.getEntity(toId);
                    recvBandwidth = toEntity.remainingBandwidth("RECV");
                    if (recvBandwidth > 0) {
                        if (!toEntity.isFileExist(fileName)) {
                            File newfile = fromFiles.getFile(fileName);
                            if (toEntity.hasStorage(newfile.getFileSize())) {
                                File file = fromFiles.getFile(fileName);
                                fromEntity.queueSendFile(file, toEntity);
                                toEntity.queueRecieveFile(file, fromEntity);
                            } else {
                                throw new FileTransferException.VirtualFileNoStorageSpaceException(
                                        "No storage in id(" + toId + ")");
                            }
                        } else {
                            throw new FileTransferException.VirtualFileAlreadyExistsException(
                                    "File has already existed in id(" + toId + ")");
                        }
                    } else {
                        throw new FileTransferException.VirtualFileNoBandwidthException(
                                "No bandwith to receive file for id(" + toId + ")");
                    }
                } else {
                    throw new FileTransferException.VirtualFileNoBandwidthException(
                            "No bandwith to send file for id(" + toId + ")");
                }

            } else {
                throw new FileTransferException.VirtualFileNotFoundException(
                        "File does not exist in id(" + fromId + ")");
            }
        }
    }

    // Task 3a
    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        // todo
        createDevice(deviceId, type, position);
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
