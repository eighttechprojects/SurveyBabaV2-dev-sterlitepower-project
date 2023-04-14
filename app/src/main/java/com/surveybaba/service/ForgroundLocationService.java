package com.surveybaba.service;

import static com.surveybaba.Database.DataBaseHelper.DELETE_TABLE_WALKING_TRACKING;
import static com.volly.URL_Utility.APP_VERSION;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionProvider;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.BuildConfig;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Modules.GPSTrackingActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.TrackingStatusData;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ForgroundLocationService extends Service implements LocationAssistant.Listener, WSResponseInterface {

    public static final String START_FOREGROUND_ACTION = "provider" + ".startforeground";
    public static final String STOP_FOREGROUND_ACTION = "provider" + ".stopforeground";
    public static final String ACTION_SEND_NO_LOCATION_ALERT = BuildConfig.APPLICATION_ID + ".SEND_NO_LOCATION_ALERT";
    public static final String TAG = "ForgroundLocationService";
    public static final String CHANNEL_ID = "channel_01";
    public static final int    FOREGROUND_NOTIFICATION_ID = 1;
    public static final int    SCHEDULED_SECONDS = 10; // 10 sec!
    public static final int    SLEEP_SECONDS = 2000;
    public static final int    SEC = 60 * 1000;

    double MIN_DISTANCE_UPDATE = 1d; // in mtrs
    public int TRACKING_INTERVAL = 10000; // min

    private boolean isScheduledStart;
    // Change by Rahul Suthar
    private boolean isDistanceOn = false;
    private boolean isFirstTimeStart = false;

    private ScheduledExecutorService scheduledExecutorService;
    // Location
    private Location currentLocation;
    private LocationAssistant assistant;
    int COUNTER_FAILED_TRIALS = 0;
    int COUNTER_MAX_TRIALS = 1;
    // Database
    private DataBaseHelper dataBaseHelper;
    // Change by Rahul Suthar
    ArrayList<Location> locationArrayList;
    Handler handler;
    Runnable runnableLocation;

    private static final float move1MeterLat = 0.000098f;
    private static final float move1MeterLon = 0.000098f;
    private static final float minRadiusZone = 2.5f;

    ArrayList<LatLng> polylineWalkingLatLngList = new ArrayList<>();
    ArrayList<TrackingStatusData> localTrackingList = new ArrayList<>();
    TrackingStatusData trackingStatusData;


//------------------------------------------------------- Location Actions -------------------------------------------------------------------------------------------------------------------------------------------------

    public static IntentFilter getLocationActions() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SEND_NO_LOCATION_ALERT);
        return intentFilter;
    }

//------------------------------------------------------- On Create -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        locationArrayList = new ArrayList<>();
        handler = new Handler();
        initDatabase();
        initLocationHelper();
        //if(SystemUtility.isInternetConnected(this)){
            //readTrackingDataForLocalDataBase();
        //}
    }

//------------------------------------------------------- InitDatabase -------------------------------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {dataBaseHelper = new DataBaseHelper(this);    }

//------------------------------------------------------- init Location Helper -------------------------------------------------------------------------------------------------------------------------------------------------

    private void initLocationHelper() {
       // Log.e(TAG, "initLocationHelper");
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
    }

//------------------------------------------------------- On Start Command -------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals(START_FOREGROUND_ACTION)) {
                Log.e(TAG, "Received Start Foreground Intent");
                // Log.e(TAG, "Received Start Foreground Service");
                startForeground(FOREGROUND_NOTIFICATION_ID, getNotification(getResources().getString(R.string.app_name)));
                stopForeground(true);
                startForeground(FOREGROUND_NOTIFICATION_ID, getNotification(getResources().getString(R.string.app_name)));
            }
            else if (intent.getAction().equals(STOP_FOREGROUND_ACTION)) {
                Log.e(TAG, "Received Stop Foreground Service");
                //Log.e(TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
        }

        startUpdateSchedule();
        checkPermission();
        return START_STICKY;
    }

//------------------------------------------------------- Check Permission -------------------------------------------------------------------------------------------------------------------------------------------------

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          //  Log.e(TAG, "Location Permission not granted");
            stopForeground(true);
            stopSelf();
        } else {
//            locationHelper.onStart();
            assistant.start();
        }
    }

//------------------------------------------------------- start Update Schedule -------------------------------------------------------------------------------------------------------------------------------------------------

    public void startUpdateSchedule() {

        // if (!isScheduledStart) {
        //     Log.e(TAG, "isScheduledStart "+isScheduledStart);

        runnableLocation = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"Runable");
                // Get User Details Form Server
                // Is Internet Connected or not!
                if(SystemUtility.isInternetConnected(ForgroundLocationService.this)){
              //      Log.e(TAG, "--------------------------------------------------------------------------------------------------------------------");
                    Log.e(TAG, "Tracking Online Mode");
                    getUserTrackStatus();
                   // readTrackingDataForLocalDataBase();
                }
                // Internet is not Connected!
                else{
             //       Log.e(TAG, "--------------------------------------------------------------------------------------------------------------------");
                    Log.e(TAG, "Tracking Offline Mode");
                    //Successs!
                    if(Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING)){
                        // here distance is zero/0 then we have to take interval function
                        if(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE).equals("0")){
                            isDistanceOn = false;
                            String tracking_interval = Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_INTERVAL);
                            TRACKING_INTERVAL = Integer.parseInt(tracking_interval) * SEC;
                        }
                        // here distance is not zero then we have to take distance function
                        else{
                            isDistanceOn = true; // Distance is Off
                            TRACKING_INTERVAL = 10000; // -> 10 Sec
                            MIN_DISTANCE_UPDATE = Double.parseDouble(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE));
                        }
                    }
                    else{
                        //Log.e(TAG,"User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
                        TRACKING_INTERVAL = 10000; // -> 10 Sec
                    }

                    if(currentLocation!=null)
                    {
                        //Log.e(TAG, "LAT: "+currentLocation.getLatitude()+", LONG: "+currentLocation.getLongitude() +", Accuracy: "+currentLocation.getAccuracy());
                        shareUserTracking(currentLocation);
                        COUNTER_FAILED_TRIALS = 0;
                        currentLocation = null;
                    }
                    else
                    {
                        Log.e(TAG, "Location not found after "+TRACKING_INTERVAL + " secs");
                        if(COUNTER_FAILED_TRIALS < COUNTER_MAX_TRIALS) {
                            COUNTER_FAILED_TRIALS++;
                        }
                        else
                        {
                            Log.e(TAG, "Revoke Location");
                           // sendBroadcast(new Intent(ACTION_SEND_NO_LOCATION_ALERT));
                        }
                    }
                    handler.postDelayed(runnableLocation,TRACKING_INTERVAL);
                }
//                if(Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING)){
//                    // here distance is zero/0 then we have to take interval function
//                    if(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE).equals("0")){
//                        isDistanceOn = false;
//                        String tracking_interval = Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_INTERVAL);
//                        TRACKING_INTERVAL = Integer.parseInt(tracking_interval) * SEC;
//                    }
//                    // here distance is not zero then we have to take distance function
//                    else{
//                        isDistanceOn = true; // Distance is Off
//                        TRACKING_INTERVAL = 10000; // -> 10 Sec
//                        MIN_DISTANCE_UPDATE = Double.parseDouble(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE));
//                    }
//                }
//                else{
//                    //Log.e(TAG,"User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
//                    TRACKING_INTERVAL = 10000; // -> 10 Sec
//                }
//
//                if(currentLocation!=null)
//                {
//                    //Log.e(TAG, "LAT: "+currentLocation.getLatitude()+", LONG: "+currentLocation.getLongitude() +", Accuracy: "+currentLocation.getAccuracy());
//                    shareUserTracking(currentLocation);
//                    COUNTER_FAILED_TRIALS = 0;
//                    currentLocation = null;
//                }
//                else
//                {
//                    Log.e(TAG, "Location not found after "+TRACKING_INTERVAL + " secs");
//                    if(COUNTER_FAILED_TRIALS < COUNTER_MAX_TRIALS) {
//                        COUNTER_FAILED_TRIALS++;
//                    }
//                    else
//                    {
//                        Log.e(TAG, "Revoke Location");
//                        sendBroadcast(new Intent(ACTION_SEND_NO_LOCATION_ALERT));
//                    }
//                }
//                handler.postDelayed(this,TRACKING_INTERVAL);
//                // Here we have to Check that Tracking is Start or not!
//                if(Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING)) {
//
//                    Log.e(TAG,"User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
//                    // here distance is zero/0 then we have to take interval function
//                    if(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE).equals("0")){
//                        isDistanceOn = false;
//                        String tracking_interval = Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_INTERVAL);
//                        TRACKING_INTERVAL = Integer.parseInt(tracking_interval) * SEC;
//                        Log.e(TAG,"Tracking Interval: " + TRACKING_INTERVAL);
//                    }
//                    // here distance is not zero then we have to take distance function
//                    else{
//                        isDistanceOn = true; // Distance is Off
//                        TRACKING_INTERVAL = 10000; // -> 10 Sec
//                        MIN_DISTANCE_UPDATE = Double.parseDouble(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE));
//                        Log.e(TAG,"Tracking Distance: " + MIN_DISTANCE_UPDATE);
//                    }
//                }
//                else{
//                    Log.e(TAG,"User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
//                    TRACKING_INTERVAL = 10000; // -> 10 Sec
//                }
//
//                if(currentLocation!=null)
//                {
//                    //Log.e(TAG, "LAT: "+currentLocation.getLatitude()+", LONG: "+currentLocation.getLongitude() +", Accuracy: "+currentLocation.getAccuracy());
//                    shareUserTracking(currentLocation);
//                    COUNTER_FAILED_TRIALS = 0;
//                    currentLocation = null;
//                }
//                else
//                {
//                    Log.e(TAG, "Location not found after "+TRACKING_INTERVAL + " secs");
//                    if(COUNTER_FAILED_TRIALS < COUNTER_MAX_TRIALS) {
//                        COUNTER_FAILED_TRIALS++;
//                    }
//                    else
//                    {
//                        Log.e(TAG, "Revoke Location");
//                        sendBroadcast(new Intent(ACTION_SEND_NO_LOCATION_ALERT));
//                    }
//
//                }
                //handler.postDelayed(this,TRACKING_INTERVAL);
            }
        };
        handler.postDelayed(runnableLocation,TRACKING_INTERVAL);// by default 10 sec set!

     //   scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
       // Log.e(TAG,"Scheduled Executor: "+ TRACKING_INTERVAL);
       // scheduledExecutorService.scheduleWithFixedDelay(runnableLocation, SCHEDULED_SECONDS/2, SCHEDULED_SECONDS, TimeUnit.SECONDS);

        // Log.e(TAG, "Schedule Start");
        // isScheduledStart = true;
        //  }
//        else
//        {
//            Log.e(TAG, "isScheduledStart "+isScheduledStart);
//        }
    }

//---------------------------------------------------------- Get User TRACK Status -------------------------------------------------------------------------------------------------------------------------------------------------

    private void getUserTrackStatus() {
        Map<String, String> params = new HashMap<String, String>();
        String data = "";
        JSONObject jsonObject = new JSONObject();

        if(!Utility.isEmptyString(Utility.getSavedData(this, Utility.LOGGED_USERID)) ){
            try {
                jsonObject.put(URL_Utility.PARAM_USED_ID,  Utility.getSavedData(this, Utility.LOGGED_USERID));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                data =  AESCrypt.encrypt(jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("data",data);
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK_STATUS;
            BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING_STATUS, params, false, false);

        }
    }

//------------------------------------------------------- Share User Tracking -------------------------------------------------------------------------------------------------------------------------------------------------

    private void shareUserTracking(Location currentLocation) {
            if (Utility.getBooleanSavedData(this, Utility.IS_USER_TRACKING)) {
                // Now here we check that tracking by Interval or distance!
                try {
                    // case 3:- get current date and time
                    // Current Date
                    Date currentDate  = Utility.getCurrentDate();
                    // get tracking details
                    String starting_date = Utility.getSavedData(this, Utility.TRACKING_START_DATE);
                    String ending_date   = Utility.getSavedData(this, Utility.TRACKING_END_DATE);
                    // Start Tracking Date
                    Date startingTrackingDate = Utility.convertIntoTrackingDateFormat(starting_date);
                    // End Tracking Date
                    Date endingTrackingDate   = Utility.convertIntoTrackingDateFormat(ending_date);
                    // case 1 :- when current date is b/w start and end date
                    if(startingTrackingDate.before(currentDate) && endingTrackingDate.after(currentDate)){
                        // Here we check time
                        // Find Tracking Time and Start Tracking
                        Log.e(TAG,"Current Date is B/W Start and End Date");
                        TrackingTime(currentLocation);
                    }
                    // case 2 :- when current date is at start
                    else if(startingTrackingDate.equals(currentDate)){
                        // Here we check time
                        // Find Tracking Time and Start Tracking
                        Log.e(TAG,"Current Date is same as Start Date");
                        TrackingTime(currentLocation);
                    }
                    else if(endingTrackingDate.equals(currentDate)){
                        Log.e(TAG,"Current Date is same as End Date");
                        TrackingTime(currentLocation);
                    }
                    else if(startingTrackingDate.equals(currentDate) && endingTrackingDate.equals(currentDate)){
                        Log.e(TAG,"Current Date is same as Start and End Date");
                        TrackingTime(currentLocation);
                    }
                    else{
                        TRACKING_INTERVAL = 10000;
                       Log.e(TAG,"Tracking Not Started");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

//------------------------------------------------------- Tracking Time -------------------------------------------------------------------------------------------------------------------------------------------------

    private void TrackingTime(Location currentLocation) throws ParseException {

        String starting_time = Utility.getSavedData(this, Utility.TRACKING_START_TIME);
        String ending_time = Utility.getSavedData(this, Utility.TRACKING_END_TIME);
        // Current Time
        Date currentTime = Utility.getCurrentTime();
        // Start Tracking Time
        Date startingTrackingTime = Utility.convertIntoTrackingTimeFormat(starting_time);
        // End Tracking Time
        Date endingTrackingTime   = Utility.convertIntoTrackingTimeFormat(ending_time);

        // case 1:- when current time is b/w start and end time
        if(startingTrackingTime.before(currentTime) && endingTrackingTime.after(currentTime)){
            // Start Tracking
            // When Internet is Connected!
            Log.e(TAG,"Current Time is B/W Start and End Time");
            if(SystemUtility.isInternetConnected(this)) {
                saveTrackingToServer(currentLocation);
            }
            // When Internet is not Connected!
            else{
                saveTrackingToLocalDatabase(currentLocation);
            }
        }
        // case 2:- when current time is at start
        else if(startingTrackingTime.equals(currentTime)){

            Log.e(TAG,"Current Time is same as Start Time");
            if(SystemUtility.isInternetConnected(this)) {
                saveTrackingToServer(currentLocation);
            }
            // When Internet is not Connected!
            else{
                saveTrackingToLocalDatabase(currentLocation);
            }
        }
        else if(endingTrackingTime.equals(currentTime)){
            Log.e(TAG,"Current Time is same as End Time");
            if(SystemUtility.isInternetConnected(this)) {
                saveTrackingToServer(currentLocation);
            }
            // When Internet is not Connected!
            else{
                saveTrackingToLocalDatabase(currentLocation);
            }
        }
        else{
            TRACKING_INTERVAL = 10000;
            Log.e(TAG,"Tracking not Started");
        }
    }

//------------------------------------------------------- Save Tracking To Server -------------------------------------------------------------------------------------------------------------------------------------------------

    private boolean isInsideCircle(LatLng prev, LatLng  cur){
        float[] distance = new float[2];
        //  start lat, start lon , end lat, end lon
        Location.distanceBetween(prev.latitude, prev.longitude, cur.latitude, cur.longitude, distance);
        return !(distance[0] > minRadiusZone);
    }

    private void saveTrackingToServer(Location currentLocation){

        // Distance Method is on!
        if(isDistanceOn){
           // Log.e(TAG,"Tracking By Distance Online");
            if(locationArrayList != null && locationArrayList.size() > 0){
                boolean hasEnoughDistance;
                LatLng cur_latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                LatLng prev_latLng = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude(),locationArrayList.get(locationArrayList.size() - 1).getLongitude());
                double distance = SphericalUtil.computeDistanceBetween(cur_latLng, prev_latLng);
                hasEnoughDistance = (distance > MIN_DISTANCE_UPDATE); // -> min distance update set at -> 10 meter
                // If Distance is Enough Then We have to Trigger/ Send Data To Server!
                if(hasEnoughDistance){
                    LatLng enoughDistanceLocation = cur_latLng;
//
//                    // case 1:- At Slop Left Side
//                    LatLng prev1 = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude() + (move1MeterLat * MIN_DISTANCE_UPDATE) , locationArrayList.get(locationArrayList.size() - 1).getLongitude() + (move1MeterLon * MIN_DISTANCE_UPDATE) );
//                    // case 2:- At Same Line
//                    LatLng prev2 = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude() , locationArrayList.get(locationArrayList.size() - 1).getLongitude() + (move1MeterLon * MIN_DISTANCE_UPDATE) );
//                    // case 3:- At Slop Right Side
//                    LatLng prev3 = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude() - (move1MeterLat * MIN_DISTANCE_UPDATE) , locationArrayList.get(locationArrayList.size() - 1).getLongitude() - (move1MeterLon * MIN_DISTANCE_UPDATE) );
//                    // case 1:-
//                    if(isInsideCircle(prev1,cur_latLng)){
//                        // prev1 set!
//                        enoughDistanceLocation = new LatLng(prev1.latitude,prev1.longitude);
//                    }
//                    // case 2:-
//                    else if(isInsideCircle(prev2,cur_latLng)){
//                        enoughDistanceLocation = new LatLng(prev2.latitude,prev2.longitude);
//                    }
//                    // case 3:-
//                    else if(isInsideCircle(prev3,cur_latLng)){
//                        enoughDistanceLocation = new LatLng(prev3.latitude,prev3.longitude);
//                    }
//                    // default value;
//                    else{
//                        enoughDistanceLocation = new LatLng(prev2.latitude,prev2.longitude);
//                    }
                    locationArrayList.add(currentLocation);
                    Log.e(TAG,"Has Enough Distance "+ MIN_DISTANCE_UPDATE);

                    Map<String, String> params = new HashMap<>();
                    String data = "";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(URL_Utility.PARAM_USED_ID, Utility.getSavedData(this, Utility.LOGGED_USERID));
                        jsonObject.put(URL_Utility.PARAM_LATITUDE, String.valueOf(enoughDistanceLocation.latitude));
                        jsonObject.put(URL_Utility.PARAM_LONGITUDE, String.valueOf(enoughDistanceLocation.longitude));
                        String date_time = getCurrentDateAndTime();
                        jsonObject.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, date_time);
                        jsonObject.put(URL_Utility.PARAM_UNIQUE_NUMBER, String.valueOf(Utility.getToken()));
                        jsonObject.put(URL_Utility.PARAM_VERSION, APP_VERSION);
                        Log.e(TAG,"Lat: " + enoughDistanceLocation.latitude +" Lon: "+ enoughDistanceLocation.longitude + " Date: "+ date_time);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, jsonObject.toString());
                    try {
                        data =  AESCrypt.encrypt(jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    params.put("data",data);
                      URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK;
                    BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
                    if(locationArrayList.size() > 2){
                        locationArrayList.remove(0);
                    }

                }
                else{
                    Log.e(TAG, "Not Enough Distance " + MIN_DISTANCE_UPDATE);
                }
            }
            else{
                if (locationArrayList != null) {
                    locationArrayList.add(currentLocation);
                }
            }
        }
        else{
            Log.e(TAG,"Tracking By Interval Online");
            // Set Data to Server!
//            Map<String, String> params = new HashMap<>();
//            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK;
//            params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(this, Utility.LOGGED_USERID));
//            params.put(URL_Utility.PARAM_LATITUDE, String.valueOf(currentLocation.getLatitude()));
//            params.put(URL_Utility.PARAM_LONGITUDE, String.valueOf(currentLocation.getLongitude()));
//            String date_time = getCurrentDateAndTime();
//            params.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, date_time);
//            params.put(URL_Utility.PARAM_VERSION, APP_VERSION);
//            Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
//

            Map<String, String> params = new HashMap<>();
            String data = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(this, Utility.LOGGED_USERID));
                jsonObject.put(URL_Utility.PARAM_LATITUDE, String.valueOf(currentLocation.getLatitude()));
                jsonObject.put(URL_Utility.PARAM_LONGITUDE, String.valueOf(currentLocation.getLongitude()));
                String date_time = getCurrentDateAndTime();
                jsonObject.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, date_time);
                jsonObject.put(URL_Utility.PARAM_UNIQUE_NUMBER, 	String.valueOf(Utility.getToken()));
                jsonObject.put(URL_Utility.PARAM_VERSION, APP_VERSION);
                Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, jsonObject.toString());
            try {
                data =  AESCrypt.encrypt(jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("data",data);
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK;
            BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
        }

//        // Distance Method is on!
//        if(isDistanceOn){
//            Log.e(TAG,"Tracking By Distance Online");
//            if(locationArrayList != null && locationArrayList.size() > 0){
//                boolean hasEnoughDistance;
//                LatLng cur_latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//                LatLng prev_latLng = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude(),locationArrayList.get(locationArrayList.size() - 1).getLongitude());
//                double distance = SphericalUtil.computeDistanceBetween(cur_latLng, prev_latLng);
//                hasEnoughDistance = (distance > MIN_DISTANCE_UPDATE); // -> min distance update set at -> 10 meter
//                // If Distance is Enough Then We have to Trigger/ Send Data To Server!
//                if(hasEnoughDistance){
//                    // trigger to server!
//                    locationArrayList.add(currentLocation);
//                    if(locationArrayList.size() > 2){
//                        locationArrayList.remove(0);
//                    }
//                    Log.e(TAG,"Has Enough Distance "+ MIN_DISTANCE_UPDATE);
//                    Map<String, String> params = new HashMap<>();
//                    URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK;
//                    params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(this, Utility.LOGGED_USERID));
//                    params.put(URL_Utility.PARAM_LATITUDE, String.valueOf(currentLocation.getLatitude()));
//                    params.put(URL_Utility.PARAM_LONGITUDE, String.valueOf(currentLocation.getLongitude()));
//                    String date_time = getCurrentDateAndTime();
//                    params.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, date_time);
//                    params.put(URL_Utility.PARAM_VERSION, APP_VERSION);
//                    Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
//                    BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
//                }
//                else{
//                    Log.e(TAG, "Not Enough Distance " + MIN_DISTANCE_UPDATE);
//                }
//            }
//            else{
//                if (locationArrayList != null) {
//                    locationArrayList.add(currentLocation);
//                }
//            }
//        }
//        else{
//            Log.e(TAG,"Tracking By Interval Online");
//            // Set Data to Server!
//            Map<String, String> params = new HashMap<>();
//            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK;
//            params.put(URL_Utility.PARAM_LOGIN_USER_ID, Utility.getSavedData(this, Utility.LOGGED_USERID));
//            params.put(URL_Utility.PARAM_LATITUDE, String.valueOf(currentLocation.getLatitude()));
//            params.put(URL_Utility.PARAM_LONGITUDE, String.valueOf(currentLocation.getLongitude()));
//            String date_time = getCurrentDateAndTime();
//            params.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, date_time);
//            params.put(URL_Utility.PARAM_VERSION, APP_VERSION);
//            Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
//            BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
//        }

    }

//------------------------------------------------------- Save Tracking To Local Database -------------------------------------------------------------------------------------------------------------------------------------------------

    private void saveTrackingToLocalDatabase(Location currentLocation){
        Log.e(TAG,"Store Data to Local Database");

        // Distance Method is on!
        if(isDistanceOn){
            Log.e(TAG,"Tracking By Distance offline");
            if(locationArrayList != null && locationArrayList.size() > 0){
                boolean hasEnoughDistance;
                LatLng cur_latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                LatLng prev_latLng = new LatLng(locationArrayList.get(locationArrayList.size() - 1).getLatitude(),locationArrayList.get(locationArrayList.size() - 1).getLongitude());
                double distance = SphericalUtil.computeDistanceBetween(cur_latLng, prev_latLng);
                hasEnoughDistance = (distance > MIN_DISTANCE_UPDATE); // -> min distance update set at -> 10 meter

                // If Distance is Enough Then We have to Trigger/ Send Data To Server!
                if(hasEnoughDistance){
                    locationArrayList.add(currentLocation);
                    if(locationArrayList.size() > 2){
                        locationArrayList.remove(0);
                    }
                    // trigger to server!
                    Log.e(TAG,"Has Enough Distance "+ MIN_DISTANCE_UPDATE);
                    String date_time = getCurrentDateAndTime();
                    dataBaseHelper.insertTrackingStatus(
                            // User ID
                            Utility.getSavedData(this, Utility.LOGGED_USERID),
                            // Lat
                            String.valueOf(currentLocation.getLatitude()),
                            // Lon
                            String.valueOf(currentLocation.getLongitude()),
                            // Created Date
                            date_time,
                            // App Version
                            APP_VERSION);
                   Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
                    Log.e(TAG,"Store Data to Local Database Successfully");
                }
                else{
                    Log.e(TAG,"Has Enough Distance "+ MIN_DISTANCE_UPDATE);
                }
            }
            else{
                if (locationArrayList != null) {
                    locationArrayList.add(currentLocation);
                }
            }
        }
        else{
           // Log.e(TAG,"Tracking By Interval offline");
            String date_time = getCurrentDateAndTime();
            dataBaseHelper.insertTrackingStatus(
                    // User ID
                    Utility.getSavedData(this, Utility.LOGGED_USERID),
                    // Lat
                    String.valueOf(currentLocation.getLatitude()),
                    // Lon
                    String.valueOf(currentLocation.getLongitude()),
                    // Created Date
                    date_time,
                    // App Version
                    APP_VERSION);
            Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude() + " Date: "+ date_time);
            Log.e(TAG,"Store Data to Local Database Successfully");
        }
    }

//------------------------------------------------------- Get Current Date and Time -------------------------------------------------------------------------------------------------------------------------------------------------

    private String getCurrentDateAndTime(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.TRACKING_CREATED_DATE_FORMAT);
        String date_time =   simpleDateFormat.format(calendar.getTime());
        return date_time;
    }

//------------------------------------------------------- On Success Response -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {
        if(response!=null) {
            //Log.e(TAG, "onSuccessResponse: " + response);
        }
        // Change by Rahul Suthar
        // Read Data to for Server
        if (responseCode == URL_Utility.ResponseCode.WS_USER_TRACK_STATUS){
            String res = AESCrypt.decryptResponse(response);
            Log.e(TAG,"Response: "+ res);
            if(!res.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(res);
                    String msg = mLoginObj.optString("status");
                    if (msg.equalsIgnoreCase("Success")){

//                        if(trackingStatusData != null && trackingStatusData.getId() != null){
//                            // then
//                            if(dataBaseHelper.getTrackingStatusDetails().size() > 0){
//                                dataBaseHelper.deleteTrackingStatusData(trackingStatusData.getId());
//                            }
//                            SyncLocalTrackingData();
//                        }

                        JSONObject mOArray = new JSONObject(mLoginObj.optString("data"));
                        if(mOArray.length() > 0){
                            String user_tracking = mOArray.optString("user_tracking");
                            String start_date = mOArray.optString("start_date");
                            String end_date = mOArray.optString("end_date");
                            String start_time = mOArray.optString("start_time");
                            String end_time = mOArray.optString("end_time");
                            String distance = mOArray.optString("distance");
                            String interval = mOArray.optString("interval");

                            Utility.saveData(this, Utility.IS_USER_TRACKING, !Utility.isEmptyString(user_tracking) && user_tracking.equalsIgnoreCase("Yes"));
                            Utility.saveData(this, Utility.TRACKING_START_DATE, start_date);
                            Utility.saveData(this, Utility.TRACKING_END_DATE, end_date);
                            Utility.saveData(this, Utility.TRACKING_START_TIME, start_time);
                            Utility.saveData(this, Utility.TRACKING_END_TIME, end_time);
                            Utility.saveData(this, Utility.TRACKING_DISTANCE, distance);
                            Utility.saveData(this, Utility.TRACKING_INTERVAL, interval);
                        }
                        //JSONArray dataArray = new JSONArray(mLoginObj.optString("data"));
                        //if (dataArray.length() > 0) {
                            //for (int i = 0; i < dataArray.length(); i++) {
                              //  JSONObject mOArray = dataArray.getJSONObject(i);



//                                // here we have to Add over Data!
//                                Log.e(TAG,
//                                        "{" + "\n" +
//                                                "Tracking Status: " + Utility.getBooleanSavedData(this, Utility.IS_USER_TRACKING) + "\n" +
//                                                "Start Date:      " + Utility.getSavedData(this, Utility.TRACKING_START_DATE) + "\n" +
//                                                "End Date:        " + Utility.getSavedData(this, Utility.TRACKING_END_DATE) + "\n" +
//                                                "Start Time:      " + Utility.getSavedData(this, Utility.TRACKING_START_TIME) + "\n" +
//                                                "End Time:        " + Utility.getSavedData(this, Utility.TRACKING_END_TIME) + "\n" +
//                                                "Distance:        " + Utility.getSavedData(this, Utility.TRACKING_DISTANCE) + "\n" +
//                                                "Interval:        " + Utility.getSavedData(this, Utility.TRACKING_INTERVAL) + "\n" +
//                                                "}");

                                // Add over Data!
                                // Here we have to Check that Tracking is Start or not!
                         //   }
                       // }

                    }
                    else {
                        Utility.saveData(ForgroundLocationService.this,Utility.IS_USER_TRACKING,false);
                      //  JSONObject mOArray = new JSONObject(mLoginObj.optString("data"));
                        Log.e(TAG, msg);
                    }

                    //Successs!
                    if (Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING)) {
                        // here distance is zero/0 then we have to take interval function
                        if (Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE).equals("0")) {
                            isDistanceOn = false;
                            String tracking_interval = Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_INTERVAL);
                            TRACKING_INTERVAL = Integer.parseInt(tracking_interval) * SEC;
                        }
                        // here distance is not zero then we have to take distance function
                        else {
                            isDistanceOn = true; // Distance is Off
                            TRACKING_INTERVAL = 10000; // -> 10 Sec
                            MIN_DISTANCE_UPDATE = Double.parseDouble(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE));
                        }
                    }
                    else {
                        Log.e(TAG,"User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
                        TRACKING_INTERVAL = 10000; // -> 10 Sec
                    }

                    if (currentLocation != null) {
                        Log.e(TAG, "LAT: "+currentLocation.getLatitude()+", LONG: "+currentLocation.getLongitude() +", Accuracy: "+currentLocation.getAccuracy());
                        shareUserTracking(currentLocation);
                        COUNTER_FAILED_TRIALS = 0;
                        currentLocation = null;
                    }
                    else {
                        Log.e(TAG, "Location not found after " + TRACKING_INTERVAL + " secs");
                        if (COUNTER_FAILED_TRIALS < COUNTER_MAX_TRIALS) {
                            COUNTER_FAILED_TRIALS++;
                        } else {
                            Log.e(TAG, "Revoke Location");
                         //   sendBroadcast(new Intent(ACTION_SEND_NO_LOCATION_ALERT));
                        }
                    }

                    handler.postDelayed(runnableLocation, TRACKING_INTERVAL);

                }
                catch (JSONException e) {
                    // e.printStackTrace();
                    Utility.saveData(this,Utility.IS_USER_TRACKING,false);
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        // Fetch Data to server
//        if(responseCode == URL_Utility.ResponseCode.WS_USER_TRACK){
//            Log.e(TAG,"Store Data to Server Successfully");
//        }
        //
        if(responseCode == URL_Utility.ResponseCode.WS_USER_TRACK_LOCAL){

        }
    }

//------------------------------------------------------- On Error Response -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {

        try{
           // String res = AESCrypt.decryptResponse(error.getMessage());
            // Log.e(TAG,"Response: "+ res);
           // if(!res.equals("")) {
                //  if(error!=null)
                //Log.e(TAG, "onErrorResponse: "+(!Utility.isEmptyString(error.getMessage())?error.getMessage():""));
                // Error in Get Tracking Details
                if (responseCode == URL_Utility.ResponseCode.WS_USER_TRACK_STATUS) {
                    // Add over Data!
                    // Here we have to Check that Tracking is Start or not!
                    if (Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING)) {

                        Log.e(TAG, "User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
                        // here distance is zero/0 then we have to take interval function
                        if (Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE).equals("0")) {
                            isDistanceOn = false;
                            String tracking_interval = Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_INTERVAL);
                            TRACKING_INTERVAL = Integer.parseInt(tracking_interval) * SEC;
                            Log.e(TAG, "Tracking Interval: " + TRACKING_INTERVAL);
                        }
                        // here distance is not zero then we have to take distance function
                        else {
                            isDistanceOn = true; // Distance is Off
                            TRACKING_INTERVAL = 10000; // -> 10 Sec
                            MIN_DISTANCE_UPDATE = Double.parseDouble(Utility.getSavedData(ForgroundLocationService.this, Utility.TRACKING_DISTANCE));
                            Log.e(TAG, "Tracking Distance: " + MIN_DISTANCE_UPDATE);
                        }
                    } else {
                        Log.e(TAG, "User Tracking: " + Utility.getBooleanSavedData(ForgroundLocationService.this, Utility.IS_USER_TRACKING));
                        TRACKING_INTERVAL = 10000; // -> 10 Sec
                    }
                    if (currentLocation != null) {
                        Log.e(TAG, "LAT: "+currentLocation.getLatitude()+", LONG: "+currentLocation.getLongitude() +", Accuracy: "+currentLocation.getAccuracy());
                        shareUserTracking(currentLocation);
                        COUNTER_FAILED_TRIALS = 0;
                        currentLocation = null;
                    }
                    else {
                        Log.e(TAG, "Location not found after " + TRACKING_INTERVAL + " secs");
                        if (COUNTER_FAILED_TRIALS < COUNTER_MAX_TRIALS) {
                            COUNTER_FAILED_TRIALS++;
                        } else {
                            Log.e(TAG, "Revoke Location");
                           // sendBroadcast(new Intent(ACTION_SEND_NO_LOCATION_ALERT));
                        }

                    }
                    handler.postDelayed(runnableLocation, TRACKING_INTERVAL);

                }
            }

        catch (Exception e){
            Log.e(TAG, e.getMessage());
            // e.printStackTrace();
        }


        // Error in Push Tracking Details
        if(responseCode == URL_Utility.ResponseCode.WS_USER_TRACK){
            Log.e(TAG,"Store Data to Server not Successfully");
        }
    }


//------------------------------------------------------- Get Notification -------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * this method get Notification object which help to notify user as foreground service
     *
     * @param notificationDetails
     * @return
     */
    private Notification getNotification(String notificationDetails) {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_MIN);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // When user click on Notification it redirect to Splash Activity!
//        Intent notificationIntent = new Intent(getApplicationContext(), SplashActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(SplashActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Notification!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), getNotificationIcon()))
                .setContentTitle(notificationDetails)
                .setContentText(getResources().getString(R.string.msg_service));
        //.setContentIntent(notificationPendingIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        builder.setAutoCancel(true);

        // here we again call For groundLocationService to turn off the tracking!
        Intent exitIntent = new Intent(this, ForgroundLocationService.class);
        exitIntent.setAction(STOP_FOREGROUND_ACTION);
        PendingIntent pexitIntent = PendingIntent.getService(this, 0, exitIntent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.addAction(R.mipmap.ic_launcher, getResources().getString(R.string.text_exit).toUpperCase(), pexitIntent);
        }

        return builder.build();
    }


//------------------------------------------------------- Notification Icon -------------------------------------------------------------------------------------------------------------------------------------------------

    // Notification Icon
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.app_icon_new2 : R.mipmap.app_icon_new2;
    }

//-------------------------------------------------------Location Service -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        Log.e(TAG, "onNeedLocationPermission");
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        Log.e(TAG, "onExplainLocationPermission");
        assistant.requestLocationPermission();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        Log.e(TAG, "onLocationPermissionPermanentlyDeclined");
    }

    @Override
    public void onNeedLocationSettingsChange() {
        Log.e(TAG, "onNeedLocationSettingsChange");
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        Log.e(TAG, "onFallBackToSystemSettings");
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        currentLocation = location;

        if(Utility.getBooleanSavedData(this,Utility.WALKING)){
           // Log.e("WALKING","ON");
            boolean EnoughDistance;
            LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            if (polylineWalkingLatLngList.size() > 0) {
                // distance in meter!
                double distance = SphericalUtil.computeDistanceBetween(currentLatLng, polylineWalkingLatLngList.get(polylineWalkingLatLngList.size() - 1));
                EnoughDistance = (distance > 5d); // -> min distance update set at -> 10 meter
            }
            else{
                EnoughDistance = true;
            }
            if(EnoughDistance)
            {
                polylineWalkingLatLngList.add(currentLatLng);
                dataBaseHelper.insertWalkingTracking(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()) );
              //  Log.e(TAG,"Store Data to Local Database Successfully");
            }
        }
        else{
            polylineWalkingLatLngList.clear();
            //Log.e("WALKING","OFF");
        }

//        dataBaseHelper.insertWalkingTracking(
//                // Lat
//                String.valueOf(currentLocation.getLatitude()),
//                // Lon
//                String.valueOf(currentLocation.getLongitude()) );
//        Log.e(TAG,"Lat: " + currentLocation.getLatitude() +" Lon: "+ currentLocation.getLongitude());
//        Log.e(TAG,"Store Data to Local Database Successfully");


    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        Log.e(TAG, "onMockLocationsDetected");
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
        Log.e(TAG, "onError type: "+type!=null?type.name():"");
        Log.e(TAG, "onError message: "+message!=null?message:"");
    }


//------------------------------------------------------- OnDestroy -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onDestroy() {
//        locationHelper.onStop();
        Log.e(TAG, "onDestroy");
        assistant.stop();
        stopUpdateSchedule();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

//------------------------------------------------------- stop Update Schedule -------------------------------------------------------------------------------------------------------------------------------------------------

    public void stopUpdateSchedule() {
        //  if (isScheduledStart) {
//        Log.e(TAG, "Schedule Stop");
//        scheduledExecutorService.shutdown(); // Disable new tasks from being submitted
//        // Wait a while for existing tasks to terminate
//        try {
//            if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                scheduledExecutorService.shutdownNow(); // Cancel currently executing
//                // tasks
//                // Wait a while for tasks to respond to being cancelled
//                if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS))
//                    Log.e(TAG, "Pool did not terminate");
//
//            }
//        } catch (InterruptedException e) {
//            Log.e(TAG, "" + e);
//            // (Re-)Cancel if current thread also interrupted
//            scheduledExecutorService.shutdownNow();
//            // Preserve interrupt status
//            Thread.currentThread().interrupt();
//        }
//        isScheduledStart = false;
//        isFirstTimeStart = false;
        //  }


    }

//------------------------------------------------------- On Bind -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

//------------------------------------------------------- Read Local DataBase -------------------------------------------------------------------------------------------------------------------------------------------------
//
//    private void readTrackingDataForLocalDataBase(){
//
//        ArrayList<TrackingStatusData> trackinglist = dataBaseHelper.getTrackingStatusDetails();
//        if(trackinglist.size() > 0){
//            localTrackingList = dataBaseHelper.getTrackingStatusDetails();
//        }
//
//    }
//
//    private void SyncLocalTrackingData(){
//        if(localTrackingList != null && localTrackingList.size() > 0){
//            trackingStatusData = localTrackingList.get(0);
//            localTrackingList.remove(0);
//            SyncTrackingDataToServer(trackingStatusData);
//        }
//    }
//
//    private void SyncTrackingDataToServer(TrackingStatusData trackingStatusData){
//            if(SystemUtility.isInternetConnected(ForgroundLocationService.this)) {
//                  Map<String, String> params = new HashMap<>();
//                String data = "";
//                    JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put(URL_Utility.PARAM_LOGIN_USER_ID, trackingStatusData.getUser_id());
//                    jsonObject.put(URL_Utility.PARAM_LATITUDE, trackingStatusData.getLatitude());
//                    jsonObject.put(URL_Utility.PARAM_LONGITUDE, trackingStatusData.getLongitude());
//                    jsonObject.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, trackingStatusData.getCreated_date());
//                    jsonObject.put(URL_Utility.PARAM_VERSION, trackingStatusData.getVersion());
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    data = AESCrypt.encrypt(jsonObject.toString());
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                params.put("data",data);
//                URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK_LOCAL;
//                BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
//            }
//
//    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /*private void restartService() {
        stopMyService();
        startMyService();
    }*/
    /*public void startMyService() {
        Intent intent = new Intent(this, ForgroundLocationService.class);
        intent.setAction(START_FOREGROUND_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer
                .MAX_VALUE)) {
            if (service!=null && serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void stopMyService() {
        if (isMyServiceRunning(ForgroundLocationService.class)) {
            Intent intent = new Intent(this, ForgroundLocationService.class);
            intent.setAction(STOP_FOREGROUND_ACTION);
            stopService(intent);
        }
    }*/
}
