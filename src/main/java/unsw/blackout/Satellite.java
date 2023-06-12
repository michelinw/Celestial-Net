package unsw.blackout;

import unsw.utils.Angle;

public class Satellite {
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;

    public Satellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    public String getSatelliteId() {
        return satelliteId;
    }

    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative");
        }
        this.height = height;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Satellite [satelliteId=" + satelliteId + ", type=" + type + ", height=" + height + ", position="
                + position + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Satellite))
            return false;
        Satellite other = (Satellite) obj;
        if (satelliteId == null) {
            if (other.satelliteId != null)
                return false;
        } else if (!satelliteId.equals(other.satelliteId))
            return false;
        return true;
    }

    public void simulate() {
        // todo: simulate movement of satellite
        double currentAngle = this.getPosition().toDegrees();

        double newAngle = currentAngle + 1;
        if (newAngle > 360) {
            newAngle = newAngle - 360;
        }

        if (newAngle < 0) {
            newAngle = newAngle + 360;
        }

        if (newAngle == 360) {
            newAngle = 0;
        }

        if (this.type == "StandardSatellite") {
            double angularVelocity = 2500 / (2 * Math.PI * this.height);
        }

        this.setPosition(newAngle);
    }
}
