package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

public class SatelliteManager {
    private ArrayList<Satellite> satellites;

    public SatelliteManager() {
        satellites = new ArrayList<>();
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
    }

    public void removeSatellite(String satelliteId) {
        // Find the satellite with the specified ID and remove it
        for (int i = 0; i < satellites.size(); i++) {
            Satellite satellite = satellites.get(i);
            if (satellite.getSatelliteId().equals(satelliteId)) {
                satellites.remove(i);
                break;
            }
        }
    }

    public ArrayList<Satellite> getSatellites() {
        return satellites;
    }

    public Satellite getSatellite(String satelliteId) {
        // Find the satellite with the specified ID and return it
        for (Satellite satellite : satellites) {
            if (satellite.getSatelliteId().equals(satelliteId)) {
                return satellite;
            }
        }
        return null;
    }

    public List<String> getSatelliteIds() {
        List<String> satelliteIds = new ArrayList<>();
        for (Satellite satellite : satellites) {
            satelliteIds.add(satellite.getSatelliteId());
        }
        return satelliteIds;
    }
}
