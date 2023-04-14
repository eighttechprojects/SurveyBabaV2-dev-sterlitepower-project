package com.surveybaba.Modules.BluetoothModule;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.surveybaba.Utilities.Utility;
import com.volly.BaseApplication;

public class BluetoothBroadCastReceiver extends BroadcastReceiver {

    // TAG
    public static final String TAG = "Bluetooth";
    // Interface
    private final BluetoothBroadcastListener bluetoothBroadcastListener;
    BaseApplication baseApplication;
//------------------------------------------------------- Constructor ---------------------------------------------------------------------------------------------------------------------------

    public BluetoothBroadCastReceiver(BluetoothBroadcastListener bluetoothBroadcastListener){
        this.bluetoothBroadcastListener = bluetoothBroadcastListener;
    }

//------------------------------------------------------- onReceive ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        // Bluetooth Connection
        switch (action){
            // Bluetooth State Change
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                switch(state) {

                    case BluetoothAdapter.STATE_ON:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_ON);
                        break;

                    case BluetoothAdapter.STATE_OFF:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_OFF);
                        break;
                }
                break;
            // Bluetooth Connection State Change
            case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                final int state1 = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE,BluetoothAdapter.ERROR);
                switch(state1) {

                    case BluetoothAdapter.STATE_CONNECTING:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_CONNECTING);
                        break;

                    case BluetoothAdapter.STATE_CONNECTED:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_CONNECTED);
                        break;

                    case BluetoothAdapter.STATE_DISCONNECTING:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DISCONNECTING);
                        break;

                    case BluetoothAdapter.STATE_DISCONNECTED:
                        bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DISCONNECTED);
                        break;
                }
                break;

            // Bluetooth Device
            case BluetoothDevice.ACTION_FOUND:
                bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DEVICE_FOUND);
                break;

            case BluetoothDevice.ACTION_ACL_CONNECTED:
                Log.e(TAG,"Bluetooth Broadcast Receiver: "+ "Connected To " + bluetoothDevice.getName() );
                bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DEVICE_CONNECTED);
                break;

            case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DEVICE_DISCONNECT_REQUEST);
                break;

            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                Log.e(TAG,"Bluetooth Broadcast Receiver: "+ "DisConnected From " + bluetoothDevice.getName() );
                bluetoothBroadcastListener.BluetoothListener(Utility.BLUETOOTH_DEVICE_DISCONNECT);
                break;
        }

    }
}