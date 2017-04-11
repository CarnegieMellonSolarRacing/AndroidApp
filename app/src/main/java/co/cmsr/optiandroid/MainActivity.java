package co.cmsr.optiandroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    public final String ACTION_USB_PERMISSION = "co.cmsr.optiandroid.USB_PERMISSION";
    Button startButton, sendButton, clearButton, stopButton;
    TextView textView, dataOutput;
    EditText editText;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    DataParser dataParser;
    boolean connectedToArduino;

    public static final String TAG = "AndroidOpti";

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            dataParser.onDataReceived(arg0);
            DataPacket dp = dataParser.getDataPacket();

            if (dp != null) {
                // New datapacket received!
                setOutputDisplay(dp.toString());
            }
        }
    };
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    openConnection();
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                tryConnectToArduino();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                closeConnection();
            }
        }

        ;
    };

    private void openConnection() {
        connection = usbManager.openDevice(device);
        serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
        if (serialPort != null) {
            if (serialPort.open()) { //Set Serial Connection Parameters.
                serialPort.setBaudRate(9600);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serialPort.read(mCallback);

                onConnectionOpened();
            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
            }
        } else {
            Log.d("SERIAL", "PORT IS NULL");
        }
    }

    private void closeConnection() {
        serialPort.close();

        onConnectionClosed();
    }

    private void onConnectionOpened() {
        startButton.setBackgroundColor(Color.GREEN);
        startButton.setText("Disconnect from Arduino");

        connectedToArduino = true;
    }

    private void onConnectionClosed() {
        startButton.setBackgroundColor(Color.RED);
        startButton.setText("Connect to Arduino");

        connectedToArduino = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        dataOutput = (TextView) findViewById(R.id.OutputDisplay);

        onConnectionClosed();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);

        Initialize();
    }

    protected void Initialize() {
        dataParser = new DataParser();
    }

    private void tryConnectToArduino() {
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341 || deviceVID == 0x2a03)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }
    }

    public void onConnectButtonClicked(View view) {
        if (connectedToArduino) {
            closeConnection();
        } else {
            tryConnectToArduino();
        }
    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");
    }

    public void onClickClear(View view) {
        textView.setText(" ");
    }

    private void setOutputDisplay(String s) {
        final TextView display = dataOutput;
        final CharSequence output = s;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                display.setText(output);
            }
        });
    }

    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }

}
