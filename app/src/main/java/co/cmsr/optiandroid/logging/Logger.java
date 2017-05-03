package co.cmsr.optiandroid.logging;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by jonbuckley on 4/27/17.
 */

public class Logger {
    public static final String STORAGE_DIR_NAME = "TrialLogs";

    public static File getStorageDir() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), STORAGE_DIR_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                System.out.println("failed to create directory");
            }
        }

        return mediaStorageDir;
    }

    public static boolean writeToFile(String name, String data) {
        File storageDir = getStorageDir();
        File newLogFile = new File(storageDir, name.replaceAll("\\W+", ""));

        try {
            FileOutputStream fos = new FileOutputStream(newLogFile, true);
            fos.write(data.getBytes());
            fos.close();

            return true;
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        // Error
        return false;
    }

}
