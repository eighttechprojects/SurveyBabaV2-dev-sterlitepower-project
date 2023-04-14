package com.surveybaba.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.surveybaba.R;
import com.surveybaba.Utilities.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class BluetoothDeviceService extends Service {

    // TAG
    private static final String TAG  ="BluetoothService";
    // Static
    public  static final String START_FOREGROUND_BLUETOOTH_DEVICE = "provider" + ".startforegroundbluetoothdevice";
    public  static final String STOP_FOREGROUND_BLUETOOTH_DEVICE  = "provider" + ".stopforegroundbluetoothdevice";
    public  static final String CHANNEL_ID = "channel_02";
    public  static final int    FOREGROUND_NOTIFICATION_ID = 3;
    // UUID
    public static final UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

    // Bluetooth Socket
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    // DGPS Receiver Data
    public static final String $GPGGA = "$GPGGA";
    public static final String $GPRMC = "$GPRMC";
    public static final String $GPGSA = "$GPGSA";
    public static final String $GPGSV = "$GPGSV";
    public static final String $GPGLL = "$GPGLL";

//------------------------------------------------------- onCreate -------------------------------------------------------------------------------------------------------------------------------------------------

    public BluetoothDeviceService() {
    }

//------------------------------------------------------- onCreate -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
    }

//------------------------------------------------------- onCreate -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals(START_FOREGROUND_BLUETOOTH_DEVICE)) {
                Log.e(TAG, "Start Foreground Bluetooth Device Service");
                startForeground(FOREGROUND_NOTIFICATION_ID, getNotification());

                //connectingDevice();
            }
            else if (intent.getAction().equals(STOP_FOREGROUND_BLUETOOTH_DEVICE)) {
                Log.e(TAG, "Stop Foreground Bluetooth Device Service");
                stopBluetoothDeviceService();
            }


        }
        return START_STICKY;
    }

//------------------------------------------------------- Bluetooth -------------------------------------------------------------------------------------------------------------------------------------------------

    private void connectingDevice(){

        if(mmDevice != null){
            try {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream  = mmSocket.getInputStream();

                if(mmSocket.isConnected()){
                    receiverDataForBluetooth();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.e(TAG,"Device not Found");
        }

    }

    public void receiverDataForBluetooth()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        // Thread
        workerThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted() && !stopWorker)
            {
                try
                {
                    int bytesAvailable = mmInputStream.available();
                    if(bytesAvailable > 0)
                    {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInputStream.read(packetBytes);

                        for(int i=0;i<bytesAvailable;i++)
                        {
                            byte b = packetBytes[i];
                            if(b == delimiter)
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, StandardCharsets.US_ASCII);
                                readBufferPosition = 0;

                                handler.post(() -> {
                                    // here we get the current data!
                                    if(!Utility.isEmptyString(data)){
                                        // $GPRMC Data
                                        if(data.startsWith($GPRMC)) {
                                            Log.e(TAG, "Data Receiver: " + data);
                                            read$GPRMC(data);
                                        }
                                        // $GPGLL Data
                                        if(data.startsWith($GPGLL)){
                                            Log.e(TAG, "Data Receiver: " + data);
                                        }
                                    }

                                });
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                }
                catch (IOException ex)
                {
                    stopWorker = true;
                }
            }
        });
        workerThread.start();
    }

    @SuppressLint("SetTextI18n")
    private void read$GPRMC(String data){
        /*
           Data:-  $GPRMC,225446,A,4916.45,N,12311.12,W,000.5,054.7,191194,020.3,E*68

           [0] - >  $GPRMC       Recommended minimum specific GPS/Transit data
           [1] - >  225446       Time of fix 22:54:46 UTC
           [2] - >  A            Navigation receiver warning A = OK, V = warning
           [3] - >  4916.45      Latitude 49 deg. 16.45 min North
           [4] - >  N            North
           [5] - >  12311.12     Longitude 123 deg. 11.12 min West
           [6] - >  W            West
           [7] - >  000.5        Speed over ground, Knots
           [8] - >  054.7        Course Made Good, True
           [9] - >  191194       Date of fix  19 November 1994
           [10] - > 020.3,E      Magnetic variation 20.3 deg East
           [11] - > *68          mandatory checksum
        */
        if(!Utility.isEmptyString(data)){
            String[] str = data.split(",");
            boolean isDataWarning = false;
            String timeInUTC = "";
            String latitude  = "";
            String longitude = "";
            String date      = "";

            // Time in UTC
            if(!Utility.isEmptyString(str[1])){
                timeInUTC = str[1];
            }
            // Data Warning
            if(!Utility.isEmptyString(str[2])){
                if(str[2].equalsIgnoreCase("A")){
                    isDataWarning = true;
                }
            }
            // now warning then
            if(isDataWarning){
                //tv.setText("\n Data Read...");
                // Latitude
                if(!Utility.isEmptyString(str[3])){
                    latitude = str[3];
                    Log.e(TAG, "Lat: "+latitude);
                    //tv.setText("\nLat: " + latitude);
                }
                else{
                    latitude = "";
                    //tv.setText("\nLat: " + latitude);
                }
                // Longitude
                if(!Utility.isEmptyString(str[5])){
                    longitude = str[5];
                    Log.e(TAG, "Long: "+longitude);
                    //tv.setText("\nLong: " + longitude);
                }
                else{
                    longitude = "";
                    //tv.setText("\nLong: " + longitude);
                }
                // Date
                if(!Utility.isEmptyString(str[9])) {
                    date = str[9];
                    //tv.setText("\nDate: " + date);
                }
                else{
                    date = "";
                    //tv.setText("\nDate: " + date);
                }
            }
            else{
                //tv.setText("\nWarning" );
                Log.e(TAG, "Warning Data Fetch Data no Receive...");
            }


        }
    }

    public void closeConnectingBluetooth() throws IOException
    {
        stopWorker = true;
        if(mmOutputStream != null){
            mmOutputStream.close();
        }
        if(mmInputStream != null){
            mmInputStream.close();
        }
    }

//------------------------------------------------------- Notification -------------------------------------------------------------------------------------------------------------------------------------------------

    @SuppressLint("ObsoleteSdkInt")
    private Notification getNotification() {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            mChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Notification!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.app_icon_new2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon_new2))
                .setContentTitle("Bluetooth Device")
                .setContentText("Bluetooth Device Connected")
                .setSound(sound);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        builder.setAutoCancel(true);
        // here we again call For groundLocationService to turn off the tracking!
        Intent exitIntent = new Intent(this, BluetoothDeviceService.class);
        exitIntent.setAction(STOP_FOREGROUND_BLUETOOTH_DEVICE);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pexitIntent = PendingIntent.getService(this, 0, exitIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.addAction(R.mipmap.app_icon_new2, getResources().getString(R.string.text_exit).toUpperCase(), pexitIntent);
        }
        return builder.build();
    }

//------------------------------------------------------- Stop Bluetooth Device Service -------------------------------------------------------------------------------------------------------------------------------------------------

    private void stopBluetoothDeviceService()  {
        Log.e(TAG, "Stop Foreground Bluetooth Device Service");
        try {
            closeConnectingBluetooth();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopForeground(true);
        stopSelf();
    }

//------------------------------------------------------- onBind -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}