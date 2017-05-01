/**
 * Created by jonbu on 4/30/2017.
 */
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import co.cmsr.optiandroid.communication.DataProcessor;
import co.cmsr.optiandroid.datastructures.DataPacket;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataProcessorTest {
    public static int DEFAULT_NUM_CURRENTS = 3;
    public static int DEFAULT_CALIBRATION_WINDOW_SIZE = 2;
    public static double[] DEFAULT_CURRENT_SLOPES = {1.0, 1.0, 1.0};

    public DataProcessor dataProcessor;

    public void makeDefaultDataProcessor() {
        dataProcessor = new DataProcessor(
                DEFAULT_NUM_CURRENTS,
                DEFAULT_CALIBRATION_WINDOW_SIZE,
                DEFAULT_CURRENT_SLOPES);
    }

    @Test
    public void underCalibrationWindow_noCurrentProcessing() {
        makeDefaultDataProcessor();

        DataPacket dataPacket = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(1.0, 2.0, 3.0),
                Arrays.asList(5.0, 1.0));
        dataProcessor.onDataPacketReceievd(dataPacket);

        Double[] expected = {1.0, 2.0, 3.0};
        assertArrayEquals(expected, dataPacket.currents.toArray());

        assertFalse(dataProcessor.isCalibrated());
    }

    @Test
    public void atCalibrationWindow_noCurrentProcessing() {
        makeDefaultDataProcessor();

        DataPacket dataPacketOne = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(1.0, 2.0, 3.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        DataPacket dataPacketTwo = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(4.0, 5.0, 6.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        dataProcessor.onDataPacketReceievd(dataPacketOne);
        dataProcessor.onDataPacketReceievd(dataPacketTwo);

        Double[] expectedOne = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedOne, dataPacketOne.currents.toArray());

        Double[] expectedTwo = {4.0, 5.0, 6.0};
        assertArrayEquals(expectedTwo, dataPacketTwo.currents.toArray());

        assertTrue(dataProcessor.isCalibrated());
    }

    @Test
    public void outsideCalibrationWindow_noCurrentProcessing() {
        makeDefaultDataProcessor();

        DataPacket dataPacketOne = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(1.0, 2.0, 3.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        DataPacket dataPacketTwo = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(4.0, 5.0, 6.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        DataPacket dataPacketThree = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(7.0, 3.5, 6.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);

        dataProcessor.onDataPacketReceievd(dataPacketOne);
        dataProcessor.onDataPacketReceievd(dataPacketTwo);
        dataProcessor.onDataPacketReceievd(dataPacketThree);

        assertTrue(dataProcessor.isCalibrated());

        Double[] expectedOne = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedOne, dataPacketOne.currents.toArray());

        Double[] expectedTwo = {4.0, 5.0, 6.0};
        assertArrayEquals(expectedTwo, dataPacketTwo.currents.toArray());

        Double[] expectedThree = {4.5, 0.0, 1.5};
        assertArrayEquals( expectedThree, dataPacketThree.currents.toArray());
    }

    @Test
    public void slopeAndIntercepts() {
        dataProcessor = new DataProcessor(
                DEFAULT_NUM_CURRENTS,
                2 /* calibration window size */,
                new double[] { 0.25, 0.5, 1.5 });

        DataPacket dataPacketOne = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(1.0, 2.0, 3.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        DataPacket dataPacketTwo = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(4.0, 5.0, 6.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        dataProcessor.onDataPacketReceievd(dataPacketOne);
        dataProcessor.onDataPacketReceievd(dataPacketTwo);

        assertTrue(dataProcessor.isCalibrated());

        DataPacket dataPacketThree = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(5.0, 8.0, 6.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        dataProcessor.onDataPacketReceievd(dataPacketThree);

        Double[] expected = {2.5 * 0.25, 4.5 * 0.5, 1.5 * 1.5};
        assertArrayEquals(expected, dataPacketThree.currents.toArray());
    }

    @Test
    public void simpleSlopes() {
        dataProcessor = new DataProcessor(
                DEFAULT_NUM_CURRENTS,
                1 /* calibration window size */,
                new double[] { 0.5, .25, 2.0 });

        DataPacket dataPacketOne = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(0.0, 0.0, 0.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        dataProcessor.onDataPacketReceievd(dataPacketOne);

        assertTrue(dataProcessor.isCalibrated());

        DataPacket dataPacketTwo = new DataPacket(
                Arrays.asList(23.0) /* temps */,
                Arrays.asList(4.0, 6.0, 2.0) /* currents */,
                Arrays.asList(5.0, 1.0) /* voltages */);
        dataProcessor.onDataPacketReceievd(dataPacketTwo);

        Double[] expected = {2.0, 6.0 * 0.25, 4.0};
        assertArrayEquals(expected, dataPacketTwo.currents.toArray());
    }
}
