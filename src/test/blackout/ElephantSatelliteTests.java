// package blackout;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestInstance.Lifecycle;

// import unsw.blackout.BlackoutController;
// import unsw.response.models.FileInfoResponse;
// import unsw.utils.Angle;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

// import java.util.Arrays;

// @TestInstance(value = Lifecycle.PER_CLASS)
// public class ElephantSatelliteTests {
// @Test
// public void testTransience() {
// BlackoutController controller = new BlackoutController();

// controller.createSatellite("Satellite", "ElephantSatellite", 90000,
// Angle.fromDegrees(60));
// controller.createDevice("Device", "DesktopDevice", Angle.fromDegrees(90));
// String test =
// "filefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefile";
// controller.addFileToDevice("Device", "newFile", test);

// assertDoesNotThrow(() -> controller.sendFile("newFile", "Device",
// "Satellite"));
// controller.simulate(2);
// assertEquals(new FileInfoResponse("newFile", "filefilefilefilefile",
// test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));
// assertListAreEqualIgnoringOrder(Arrays.asList(),
// controller.communicableEntitiesInRange("Satellite"));

// controller.simulate(120);
// assertListAreEqualIgnoringOrder(Arrays.asList(),
// controller.communicableEntitiesInRange("Satellite"));
// assertEquals(new FileInfoResponse("newFile", "filefilefilefilefile",
// test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));

// controller.simulate(55);
// assertListAreEqualIgnoringOrder(Arrays.asList(),
// controller.communicableEntitiesInRange("Satellite"));
// assertEquals(new FileInfoResponse("newFile", "filefilefilefilefile",
// test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));

// controller.simulate();
// assertListAreEqualIgnoringOrder(Arrays.asList("Device"),
// controller.communicableEntitiesInRange("Satellite"));
// assertEquals(new FileInfoResponse("newFile",
// "filefilefilefilefilefilefilefilefilefile", test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));

// controller.simulate();
// assertListAreEqualIgnoringOrder(Arrays.asList("Device"),
// controller.communicableEntitiesInRange("Satellite"));
// assertEquals(new FileInfoResponse("newFile",
// "filefilefilefilefilefilefilefilefilefilefilefilefilefilefile",
// test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));

// controller.simulate();
// assertListAreEqualIgnoringOrder(Arrays.asList("Device"),
// controller.communicableEntitiesInRange("Satellite"));
// assertEquals(new FileInfoResponse("newFile", test, test.length(), true),
// controller.getInfo("Satellite").getFiles().get("newFile"));
// }

// @Test
// public void testRemoveFile() {
// BlackoutController controller = new BlackoutController();

// controller.createSatellite("Satellite", "ElephantSatellite", 91799,
// Angle.fromDegrees(52));
// controller.createDevice("Device", "DesktopDevice", Angle.fromDegrees(90));

// String test =
// "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
// controller.addFileToDevice("Device", "newFile", test);

// assertDoesNotThrow(() -> controller.sendFile("newFile", "Device",
// "Satellite"));
// controller.simulate(5);

// assertEquals(new FileInfoResponse("newFile", "testtesttesttesttest",
// test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile"));

// controller.createDevice("DeviceA", "DesktopDevice", Angle.fromDegrees(48));
// controller.addFileToDevice("DeviceA", "newFile2", test);

// assertDoesNotThrow(() -> controller.sendFile("newFile2", "DeviceA",
// "Satellite"));

// assertEquals(new FileInfoResponse("newFile2", "", test.length(), false),
// controller.getInfo("Satellite").getFiles().get("newFile2"));
// assertEquals(null,
// controller.getInfo("Satellite").getFiles().get("newFile"));
// }
// }
