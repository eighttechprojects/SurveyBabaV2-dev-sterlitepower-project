package com.surveybaba.LocationAssist;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;

public class LocationService extends Service {

    private static final int SET_INTERVAL = 10000; // 10 sec
    private static final int FAST_INTERVAL = 10000; // 10 sec
    private static final float SET_SMALLEST_DISTANCE = 3f; // 10 sec

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public LocationService(){
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    @Override
    public void onCreate() {
        super.onCreate();

        new Notification();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChanel() ;
        else startForeground(1, new Notification());


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000); // 30sec delay
        locationRequest.setFastestInterval(5000); // 20 sec delay
        locationRequest.setSmallestDisplacement(2f);// 3m mini distance.
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location =  locationResult.getLastLocation();
                try{

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        startLocationUpdates();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChanel() {
        String notificationChannelId = "Location channel id";
        String channelName = "Location Service";

        NotificationChannel chan = new NotificationChannel(
                notificationChannelId,
                channelName,
                NotificationManager.IMPORTANCE_NONE
        );
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = getSystemService(NotificationManager.class);

        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, notificationChannelId);

        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Location Service")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


}

