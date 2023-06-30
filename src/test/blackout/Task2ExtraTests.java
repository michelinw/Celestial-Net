package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

// import java.util.Arrays;

// import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExtraTests {
    // @Test
    // public void testEntitiesInRangeRelay() {
    // BlackoutController controller = new BlackoutController();
    // controller.createSatellite("Satellite1", "StandardSatellite", 5000 +
    // RADIUS_OF_JUPITER, Angle.fromDegrees(320));
    // controller.createSatellite("Satellite2", "StandardSatellite", 5000 +
    // RADIUS_OF_JUPITER, Angle.fromDegrees(240));
    // controller.createSatellite("Satellite3", "RelaySatellite", 5000 +
    // RADIUS_OF_JUPITER, Angle.fromDegrees(280));
    // controller.createSatellite("Satellite4", "RelaySatellite", 5000 +
    // RADIUS_OF_JUPITER, Angle.fromDegrees(240));
    // controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(330));
    // controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(230));

    // System.out.println(controller.communicableEntitiesInRange("Satellite1"));

    // assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "Satellite2",
    // "Satellite3", "Satellite4"),
    // controller.communicableEntitiesInRange("Satellite1"));

    // assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite3",
    // "Satellite1", "Satellite4"),
    // controller.communicableEntitiesInRange("Satellite2"));
    // }
    @Test
    public void testStandardMovement() {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "StandardSatellite", 200 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(300), 200 + RADIUS_OF_JUPITER,
                "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(297.95), 200 + RADIUS_OF_JUPITER,
                "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testRelay345Start() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(345));

        // moves in positive direction
        assertEquals(
                new EntityInfoResponse("Satellite1", Angle.fromDegrees(345), 100 + RADIUS_OF_JUPITER, "RelaySatellite"),
                controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(346.23), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(347.46), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(348.69), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(354.84), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(0.99), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(153);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(188.78), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(190.00), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(188.77), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testNegativeRelayMovement() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(293.85), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    // relay moves 1.2275738 degrees per tick
    @Test
    public void testRelayGeneral() {
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(140));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(138.77), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(
                new EntityInfoResponse("Satellite1", Angle.fromDegrees(140), 100 + RADIUS_OF_JUPITER, "RelaySatellite"),
                controller.getInfo("Satellite1"));
        controller.simulate(50);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(179.28), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingTeleportOnNextTick() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(179.9));

        controller.simulate();
        Angle clockwiseOnMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnMovement.compareTo(Angle.fromDegrees(360)) == 0);
    }

    @Test
    public void testTeleporting180Start() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(180));

        controller.simulate(2);
        Angle clockwiseOnMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnMovement.compareTo(Angle.fromDegrees(182)) == -1);
    }
}
