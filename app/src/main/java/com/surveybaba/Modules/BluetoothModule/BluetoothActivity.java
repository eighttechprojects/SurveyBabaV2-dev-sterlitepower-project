package com.surveybaba.Modules.BluetoothModule;

import static com.surveybaba.Utilities.Utility.BLUETOOTH_CONNECTED;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_CONNECTING;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_DEVICE_CONNECTED;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_DEVICE_DISCONNECT;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_DEVICE_FOUND;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_DISCONNECTED;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_DISCONNECTING;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_OFF;
import static com.surveybaba.Utilities.Utility.BLUETOOTH_ON;
import static com.surveybaba.service.BluetoothDeviceService.START_FOREGROUND_BLUETOOTH_DEVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.surveybaba.ADAPTER.BluetoothDeviceAdapter;
import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.service.BluetoothDeviceService;
import com.volly.BaseApplication;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
        //implements  BluetoothDeviceAdapter.BluetoothDeviceListener, BluetoothBroadcastListener {

    // TAG
    public static final String TAG = "Bluetooth";
    // Activity
    Activity mActivity;
    // base Application
    BaseApplication baseApplication;
//    //Bluetooth Constants
//    public static final int START_LISTENING         = 1;
//    public static final int START_CONNECTING        = 2;
//    public static final int START_CONNECTED         = 3;
//    public static final int START_CONNECTION_FAILED = 4;
//    public static final int START_MESSAGE_RECEIVED  = 5;
//    public static final int BluetoothRequestCode = 11;
//    public static final String LISTENING = "Listening";
//    public static final String CONNECTING = "Connecting";
//    public static final String CONNECTED = "Connected";
//    public static final String CONNECTION_FAILED = "Connection Failed";
//
//    // Bluetooth Adapter
//    BluetoothAdapter bluetooth;
//    // RecycleView
//    RecyclerView bluetoothDeviceListView;
//    // Linear Layout
//    LinearLayout deviceNotFoundLayout;
//    // Bluetooth Device Adapter
//    BluetoothDeviceAdapter bluetoothDeviceAdapter;
//    // Button
//    Button pairDeviceButton;
//    // TextView
//    TextView tv;
//    // ProgressBar
//    private ProgressDialog progressDialog;
//
//    BluetoothSocket mmSocket;
//    BluetoothDevice mmDevice;
//    OutputStream mmOutputStream;
//    InputStream mmInputStream;
//    Thread workerThread;
//    byte[] readBuffer;
//    int readBufferPosition;
//    int counter;
//    volatile boolean stopWorker;
//
//    // Broadcast Receiver
//    BroadcastReceiver bluetoothConnectivityReceiver;
//
//    // DGPS Receiver Data
//    public static final String $GPGGA = "$GPGGA";
//    public static final String $GPRMC = "$GPRMC";
//    public static final String $GPGSA = "$GPGSA";
//    public static final String $GPGSV = "$GPGSV";
//    public static final String $GPGLL = "$GPGLL";


//------------------------------------------------------- onCreated -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // Activity
        mActivity = this;
        // init
        init();
        // Permission
        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }
//        // Device not Found Layout
//        showDeviceNotFoundLayout(true);
//        // Bluetooth Adapter
//        bluetooth = BluetoothAdapter.getDefaultAdapter();
//        // Bluetooth On
//        BluetoothOn();
//        // pair Device Button
//        pairDeviceButton.setOnClickListener(view -> Utility.reDirectToBluetoothSetting(mActivity));
//        // Bluetooth Connectivity Receiver
//        registerBluetoothConnectivityReceiver();

    }

//------------------------------------------------------- init -----------------------------------------------------------------------------------------------------------------------------

    private void init(){
//        // List View
//        bluetoothDeviceListView = findViewById(R.id.bluetoothDeviceListView);
//        // Linear Layout
//        deviceNotFoundLayout    = findViewById(R.id.deviceNotFoundLayout);
//        // Button
//        pairDeviceButton        = findViewById(R.id.pairDeviceButton);
        // TextView
      //  tv                      = findViewById(R.id.tv);
    }

//------------------------------------------------------- Menu -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
//
////------------------------------------------------------- Bluetooth -----------------------------------------------------------------------------------------------------------------------------------
//
//    @SuppressLint("MissingPermission")
//    private void BluetoothOn(){
//        if(bluetooth == null){
//            Toast.makeText(this, "Bluetooth does not support on this device", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Bluetooth does not support on this device");
//        }
//        else{
//            if(!bluetooth.isEnabled()){
//                Log.e(TAG,"Bluetooth Off");
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(intent,BluetoothRequestCode);
//            }
//            else{
//                Log.e(TAG,"Bluetooth On");
//                BluetoothDevicePairList();
//            }
//        }
//    }
//
////------------------------------------------------------- Bluetooth Device Pair List -----------------------------------------------------------------------------------------------------------------------------------
//
//    @SuppressLint("MissingPermission")
//    private void BluetoothDevicePairList(){
//        Set<BluetoothDevice> bluetoothDevices = bluetooth.getBondedDevices();
//        if(bluetoothDevices.size() > 0) {
//            Log.e(TAG,"Bluetooth Device Pair Size: "+ bluetoothDevices.size());
//            // Show Device Pair List View
//            showBluetoothDeviceListView(true);
//            // Do not Show Device Not Found Layout
//            showDeviceNotFoundLayout(false);
//            // Set Bluetooth Device Adapter
//            setBluetoothDeviceAdapter(bluetoothDevices);
//        }
//        else
//        {
//            Log.e(TAG,"No Device Pair");
//            // Do not Show Device List View
//            showBluetoothDeviceListView(false);
//            // Show Device not Found Layout
//            showDeviceNotFoundLayout(true);
//        }
//    }
//
//    private void setBluetoothDeviceAdapter(Set<BluetoothDevice> bluetoothDevices){
//        ArrayList<BluetoothDevice> bluetoothDeviceList = new ArrayList<>(bluetoothDevices);
//        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(mActivity,bluetoothDeviceList,this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        bluetoothDeviceListView.setLayoutManager(linearLayoutManager);
//        bluetoothDeviceListView.setAdapter(bluetoothDeviceAdapter);
//    }
//
////------------------------------------------------------- Bluetooth Device Click -----------------------------------------------------------------------------------------------------------------------------------
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void onBluetoothDeviceClick(BluetoothDevice bluetoothDevice) {
//       // showProgressBar("Connecting...");
//        Log.e(TAG,"Bluetooth Device Start Details  ---------------------------------------------------------------------------------");
//        Log.e(TAG,"Bluetooth Device Name:    " + bluetoothDevice.getName());
//        Log.e(TAG, "Bluetooth Device Address: " + bluetoothDevice.getAddress());
//        Log.e(TAG, "Bluetooth Device Type:    " + bluetoothDevice.getType());
//        Log.e(TAG, "Bluetooth Device Alias:   " + bluetoothDevice.getAlias());
//        Log.e(TAG, "Bluetooth Device Class:   " + bluetoothDevice.getBluetoothClass().getDeviceClass());
//        Log.e(TAG,"Bluetooth Device End Details  -----------------------------------------------------------------------------------");
//
////        try{
////            //Log.e(TAG, "UnPair Device: " + removePair(bluetoothDevice));
////            //Log.e(TAG,"Pair Device: "    + createdPair(bluetoothDevice));
////        }
////        catch (Exception e){
////            Log.e(TAG, e.getMessage());
////        }
//
//        try{
//            startConnectingBluetooth(bluetoothDevice);
//        }catch (IOException e){
//            dismissProgressBar();
//            Log.e(TAG, e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
////------------------------------------------------------- Bluetooth -----------------------------------------------------------------------------------------------------------------------------------
//
//    public void startConnectingBluetooth(BluetoothDevice mmDevice) throws IOException
//    {
//        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
//        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//        mmSocket.connect();
//        mmOutputStream = mmSocket.getOutputStream();
//        mmInputStream  = mmSocket.getInputStream();
//
//        if (mmSocket.isConnected()) {
//            dismissProgressBar();
//            receiverDataForBluetooth();
//            Utility.getOKDialogBox(mActivity, "Device Connected", dialog -> {
//                baseApplication.startBluetoothDeviceService();
//                reDirectToDashBoard();
//                dialog.dismiss();
//            });
//        }
//        else{
//            dismissProgressBar();
//        }
//    }
//
////    private void pairToDevice(BluetoothDevice device){
////        try{
////          Method m = device.getClass().getMethod("createBond",(Class[]) null);
////          m.invoke(device,(Object[]) null);
////        }catch (Exception e){
////            Log.e(TAG, e.getMessage());
////        }
////    }
//
////    public boolean createdPair(BluetoothDevice btDevice) throws Exception
////    {
////        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
////        Method createBondMethod = class1.getMethod("createBond");
////        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
////        return returnValue.booleanValue();
////    }
////    public boolean removePair(BluetoothDevice btDevice) throws Exception
////    {
////        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
////        Method createBondMethod = class1.getMethod("removeBond");
////        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
////        return returnValue.booleanValue();
////    }
//
////    private void unpairToDevice(BluetoothDevice device) {
////        try {
////            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
////            m.invoke(device, (Object[]) null);
////        } catch (Exception e) {
////            Log.e(TAG, e.getMessage());
////        }
////    }
//
//    public void receiverDataForBluetooth()
//    {
//        final Handler handler = new Handler();
//        final byte delimiter = 10; //This is the ASCII code for a newline character
//        stopWorker = false;
//        readBufferPosition = 0;
//        readBuffer = new byte[1024];
//        // Thread
//        workerThread = new Thread(() -> {
//            while(!Thread.currentThread().isInterrupted() && !stopWorker)
//            {
//                try
//                {
//                    int bytesAvailable = mmInputStream.available();
//                    if(bytesAvailable > 0)
//                    {
//                        byte[] packetBytes = new byte[bytesAvailable];
//                        mmInputStream.read(packetBytes);
//
//                        for(int i=0;i<bytesAvailable;i++)
//                        {
//                            byte b = packetBytes[i];
//                            if(b == delimiter)
//                            {
//                                byte[] encodedBytes = new byte[readBufferPosition];
//                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                final String data = new String(encodedBytes, StandardCharsets.US_ASCII);
//                                readBufferPosition = 0;
//
//                                handler.post(() -> {
//                                    // here we get the current data!
//                                    if(!Utility.isEmptyString(data)){
//                                        // $GPRMC Data
//                                        if(data.startsWith($GPRMC)) {
//                                            Log.e(TAG, "Data Receiver: " + data);
//                                            read$GPRMC(data);
//                                        }
//                                        // $GPGLL Data
//                                        if(data.startsWith($GPGLL)){
//                                            Log.e(TAG, "Data Receiver: " + data);
//                                        }
//                                    }
//
//                                });
//                            }
//                            else
//                            {
//                                readBuffer[readBufferPosition++] = b;
//                            }
//                        }
//                    }
//                }
//                catch (IOException ex)
//                {
//                    stopWorker = true;
//                }
//            }
//        });
//        workerThread.start();
//    }
//
//    @SuppressLint("SetTextI18n")
//    private void read$GPRMC(String data){
//        /*
//           Data:-  $GPRMC,225446,A,4916.45,N,12311.12,W,000.5,054.7,191194,020.3,E*68
//
//           [0] - >  $GPRMC       Recommended minimum specific GPS/Transit data
//           [1] - >  225446       Time of fix 22:54:46 UTC
//           [2] - >  A            Navigation receiver warning A = OK, V = warning
//           [3] - >  4916.45      Latitude 49 deg. 16.45 min North
//           [4] - >  N            North
//           [5] - >  12311.12     Longitude 123 deg. 11.12 min West
//           [6] - >  W            West
//           [7] - >  000.5        Speed over ground, Knots
//           [8] - >  054.7        Course Made Good, True
//           [9] - >  191194       Date of fix  19 November 1994
//           [10] - > 020.3,E      Magnetic variation 20.3 deg East
//           [11] - > *68          mandatory checksum
//        */
//        if(!Utility.isEmptyString(data)){
//            String[] str = data.split(",");
//            boolean isDataWarning = false;
//            String timeInUTC = "";
//            String latitude  = "";
//            String longitude = "";
//            String date      = "";
//            StringBuilder sb = new StringBuilder();
//            // Time in UTC
//            if(!Utility.isEmptyString(str[1])){
//                timeInUTC = str[1];
//            }
//            // Data Warning
//            if(!Utility.isEmptyString(str[2])){
//                if(str[2].equalsIgnoreCase("A")){
//                    isDataWarning = true;
//                }
//                else if(str[2].equalsIgnoreCase("V")){
//                    isDataWarning = false;
//                }
//                else{
//                    isDataWarning = false;
//                }
//            }
//            // now warning then
//            if(isDataWarning){
//                // Latitude
//                if(!Utility.isEmptyString(str[3])){
//                    latitude = str[3];
//                    Log.e(TAG, "Lat: "+latitude);
//                }
//                else{
//                    latitude = "";
//                }
//                // Longitude
//                if(!Utility.isEmptyString(str[5])){
//                    longitude = str[5];
//                    Log.e(TAG, "Long: "+longitude);
//                }
//                else{
//                    longitude = "";
//                }
//                // Date
//                if(!Utility.isEmptyString(str[9])) {
//                    date = str[9];
//                    Log.e(TAG, "Date: "+date);
//                }
//                else{
//                    date = "";
//                }
//            }
//            else{
//                Log.e(TAG, "Warning Data Fetch Data no Receive...");
//            }
//
//
//
//        }
//    }
//
//    public void closeConnectingBluetooth() throws IOException
//    {
//        stopWorker = true;
//        if(mmOutputStream != null){
//            mmOutputStream.close();
//        }
//        if(mmInputStream != null){
//            mmInputStream.close();
//        }
//
//    }
//

//------------------------------------------------------- onActivityResult ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == BluetoothRequestCode){
//            if(resultCode == RESULT_OK){
//                Toast.makeText(BluetoothActivity.this, "Bluetooth On", Toast.LENGTH_SHORT).show();
//                BluetoothOn();
//            }
//            if(resultCode == RESULT_CANCELED){
//                finish();
//                Toast.makeText(BluetoothActivity.this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
//
////------------------------------------------------------- visible ---------------------------------------------------------------------------------------------------------------------------
//
//    private void showBluetoothDeviceListView(boolean toVisible) {
//        bluetoothDeviceListView.setVisibility(toVisible ? View.VISIBLE : View.GONE);
//    }
//
//    private void showDeviceNotFoundLayout(boolean toVisible){
//        deviceNotFoundLayout.setVisibility(toVisible ? View.VISIBLE : View.GONE);
//    }

//------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"On Resume");
//
//        if(bluetooth.isEnabled()){
//            BluetoothDevicePairList();
//        }
    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"On Pause");
    }

//------------------------------------------------------- on Destroy ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Bluetooth
     //   unregisterBluetoothConnectivityReceiver();
    }

//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() { finish(); }

//
////---------------------------------------------------------- Progress Bar ------------------------------------------------------------
//
//    private void dismissProgressBar() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//    }
//
//    private void showProgressBar(String message) {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setCancelable(false);
//            progressDialog.setMessage(message);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();
//        }
//        else {
//            progressDialog.setMessage(message);
//        }
//    }
//
//
////------------------------------------------------------- reDirect ---------------------------------------------------------------------------------------------------------------------------
//
//    private void reDirectToDashBoard(){
//        startActivity(new Intent(mActivity, DashBoardBNActivity.class));
//    }
//
////------------------------------------------------------- Bluetooth BroadCast Receiver ---------------------------------------------------------------------------------------------------------------------------
//
//    // Register BluetoothConnectivityReceiver
//    protected void registerBluetoothConnectivityReceiver(){
//        bluetoothConnectivityReceiver = new BluetoothBroadCastReceiver(this);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        registerReceiver(bluetoothConnectivityReceiver,filter);
//    }
//    // UnRegister BluetoothConnectivityReceiver
//    protected void unregisterBluetoothConnectivityReceiver(){
//        try{
//            unregisterReceiver(bluetoothConnectivityReceiver);
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }
//    }
//
////------------------------------------------------------- Bluetooth BroadCast Receiver Listener ---------------------------------------------------------------------------------------------------------------------------
//
//    @Override
//    public void BluetoothListener(String value) {
//        Log.e(TAG,"Bluetooth Broadcast Receiver: "+value);
//
////        switch (value){
////
////            case BLUETOOTH_ON:
////                break;
////
////            case BLUETOOTH_OFF:
////                break;
////
////            case BLUETOOTH_CONNECTING:
////                break;
////
////            case BLUETOOTH_CONNECTED:
////                break;
////
////            case BLUETOOTH_DISCONNECTING:
////                break;
////
////            case BLUETOOTH_DISCONNECTED:
////                break;
////
////            case BLUETOOTH_DEVICE_FOUND:
////                break;
////
////            case BLUETOOTH_DEVICE_CONNECTED:
////                break;
////
////            case BLUETOOTH_DEVICE_DISCONNECT:
////                break;
////        }
//
//    }
}