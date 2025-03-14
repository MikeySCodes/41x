package com.example.ble101;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PsoCCapsenseLedService extends Service {
    private final static String TAG = PsoCCapsenseLedService.class.getSimpleName();

    // Bluetooth objects that we need to interact with
    private static BluetoothManager mBluetoothManager;
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothLeScanner mLEScanner;
    private static BluetoothDevice mLeDevice;
    private static BluetoothGatt mBluetoothGatt;

    // Bluetooth characteristics we need to read/write
    private static BluetoothGattCharacteristic mLedCharacteristic;
    private static BluetoothGattCharacteristic mCapsenseCharacteristic;
    private static BluetoothGattDescriptor mCapSenseCccd;

    // UUIDs for the service and characteristics that the custom CapSenseLED service uses
    private final static String capsenseLedServiceUUID = "05FF7545-2829-42A4-B1B8-5E4434E061F0";
    public final static String LedCharacteristicUUID = "BA8C5B3B-3A3B-46D7-85B2-847F1409F861";
    public final static String capsenseCharacteristicUUID = "0CED5B5A-7344-4BF2-85DC-CB04D94214F2";

    // Variables to keep track of the LED switch state and CapSense Value
    private static boolean mLedSwitchState = false;
    private static String mCapSenseValue = "-1"; // The no-touch value

    // Actions used during broadcasts to the main activity
    public final static String ACTION_BLESCAN_CALLBACK = "com.example.ble101.ACTION_BLESCAN_CALLBACK";
    public final static String ACTION_CONNECTED = "com.example.ble101.ACTION_CONNECTED";
    public final static String ACTION_DISCONNECTED = "com.example.ble101.ACTION_DISCONNECTED";
    public final static String ACTION_SERVICES_DISCOVERED = "com.example.ble101.ACTION_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_RECEIVED = "com.example.ble101.ACTION_DATA_RECEIVED";

    public PsoCCapsenseLedService() {
    }

    /**
     * This is a binder for the PsoCCapsenseLedService
     */
    public class LocalBinder extends Binder {
        PsoCCapsenseLedService getService() {
            return PsoCCapsenseLedService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // The BLE close method is called when we unbind the service to free up the resources.
        close();
        return super.onUnbind(intent);
    }

    /**
     * Closes the Bluetooth GATT connection and cleans up resources.
     */
    public void close() {
        if (mBluetoothGatt != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                } else {
                    Log.e(TAG, "BLUETOOTH_CONNECT permission not granted. Cannot close GATT.");
                }
            } else {
                // Older versions don't need BLUETOOTH_CONNECT permission
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        }
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // Bluetooth Manager
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter");
            return false;
        }

        return true;
    }

    /**
     * Scans for BLE devices
     */
    public void scan() {
        /* Scan for devices and look for one with the services that we want */
        UUID capsenseLedService = UUID.fromString(capsenseLedServiceUUID);
        UUID[] capsenseLedServiceArray = {capsenseLedService};

        ScanSettings settings;
        List<ScanFilter> filters;
        mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        filters = new ArrayList<>();
        // We will scan just for the CAR's UUID
        ParcelUuid PUuid = new ParcelUuid(capsenseLedService);
        ScanFilter filter = new ScanFilter.Builder().setServiceUuid(PUuid).build();
    }
}
