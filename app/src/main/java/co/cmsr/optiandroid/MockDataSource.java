package co.cmsr.optiandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import co.cmsr.optiandroid.datastructures.DataPacket;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class MockDataSource {
    Random random;
    Thread transmitThread;
    DataManager dataManager;
    boolean shouldTransmit;
    volatile Object mutex;

    public MockDataSource(final DataManager dataManager) {
        random = new Random();
        this.dataManager = dataManager;
        shouldTransmit = false;
        mutex = new Object();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (mutex) {
                        while (!shouldTransmit) {
                            try {
                                mutex.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Now we have a lock on mutex.
                        transmitMockPacket(dataManager);

                        try {
                            // sleep for half a second.
                            Thread.sleep((long) 500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            private void transmitMockPacket(DataManager dm) {
                DataPacket dp = samplePacket();
                dm.onParsedPacket(dp);
            }

            private DataPacket samplePacket() {
                List<Double> currents = new ArrayList<Double>();
                currents.add(new Double(random.nextDouble()));
                currents.add(new Double(random.nextDouble()));
                currents.add(new Double(random.nextDouble()));
                currents.add(new Double(random.nextDouble()));

                List<Double> temps = new ArrayList<Double>();
                temps.add(new Double(40.0));//(random.nextDouble() * 10) + 15));
                temps.add(new Double((random.nextDouble() * 25) + 15));

                List<Double> voltages = new ArrayList<Double>();
                voltages.add(new Double(random.nextDouble() * 17));
                voltages.add(new Double(random.nextDouble() * 17));
                voltages.add(new Double(random.nextDouble() * 17));
                voltages.add(new Double(random.nextDouble() * 17));

                DataPacket dp = new DataPacket(temps, currents, voltages);

                return dp;
            }
        };
        transmitThread = new Thread(r);
        pauseTransmitting();

        transmitThread.start();
    }

    public void startTransmitting() {
        synchronized (mutex) {
            shouldTransmit = true;
        }
    }

    public void pauseTransmitting() {
        synchronized (mutex) {
            shouldTransmit = false;
        }
    }
}
