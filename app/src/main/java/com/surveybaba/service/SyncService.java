package com.surveybaba.service;

import static com.surveybaba.Utilities.Utility.getUploadFilePath;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.android.volley.VolleyError;
import com.fileupload.AndroidMultiPartEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.InternetConnection.ConnectivityReceiver;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.OnlineLayerModel;
import com.surveybaba.model.ProjectLayerModel;
import com.surveybaba.model.TrackingStatusData;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncService extends Service implements WSResponseInterface {

    // TAG
    private static final String TAG  ="SyncService";
    // Static
    public  static final String START_FOREGROUND_SYNC = "provider" + ".startforegroundsync";
    public  static final String STOP_FOREGROUND_SYNC  = "provider" + ".stopforegroundsync";
    public  static final String CHANNEL_ID = "channel_02";
    public  static final int    FOREGROUND_SYNC_NOTIFICATION_ID = 2;
    public  static final int    SYNC_SUCCESSFULLY_NOTIFICATION_ID = 3;
    public  static final String CHANNEL_ID3 = "channel_03";

    // Database
    private DataBaseHelper dataBaseHelper;
    NotificationCompat.Builder builder;
    // Broadcast Receiver
    BroadcastReceiver broadcastReceiver;

    // Sync Data --------------------------------------------------------------------------------------------------------
    long totalSize = 0;
    // Boolean
    private static boolean isOnlineLayerSync = true;
    private static boolean isCameraSync      = true;
    private static boolean isGPSTrackingSync = true;
    private static boolean isFormSync        = true;
    private static boolean isTrackingSync    = true;
    private static boolean isTimeLineSync    = true;
    private static boolean isMapCameraSync   = true;
    private static boolean isGISCameraSync   = true;
    public static  boolean isFileUpload      = true;
    public static  boolean isCameraUpload    = true;
    public static  boolean isVideoUpload     = true;
    public static  boolean isAudioUpload     = true;
    // String
    public static final String TYPE_FILE   = "file";
    public static final String TYPE_CAMERA = "cameraUploader";
    public static final String TYPE_VIDEO  = "videoUploader";
    public static final String TYPE_AUDIO  = "audioUploader";

    // Online Layer
    private ArrayList<String> syncOnlineLayerList = new ArrayList<>();
    private String syncProjectLayerModel;
    // GPS Tracking Sync Data
    private ArrayList<GpsTrackingModule> syncGPSTrackingList = new ArrayList<>();
    private GpsTrackingModule gpsSyncTrackingModule;
    // Form Sync Data
    private ArrayList<FormDataModel> syncFormDataList =  new ArrayList<>();
    private FormDataModel formDataModel;
    // Tracking Sync Data
    private ArrayList<TrackingStatusData> syncTrackingList = new ArrayList<>();
    private TrackingStatusData trackingStatusModel;


//------------------------------------------------------- onCreate -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();

        // network check broadcastReceiver!
        broadcastReceiver = new ConnectivityReceiver() {
            @Override
            protected void onNetworkChange(String alert) {
                if(Utility.NO_NETWORK_CONNECTED.equals(alert)){
                    ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
                    ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
                    ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
                    ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
                    ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
                    ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
                    ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
                    ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();
                  //  ArrayList<ProjectLayerModel>  onlineLayerList = new ArrayList<>();//dataBaseHelper.getGISSurveyOnlineLayersList();

                    if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0 ){
                        Toast.makeText(SyncService.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        stopSyncService();
                        Log.e(TAG, "Sync Service No Data Found in Local DataBase");
                    }
                    else{
                        Toast.makeText(SyncService.this, "Sync Fail", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SyncService.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        stopService();
                    }
                }
            }
        };
        registerNetworkBroadcast();

    }

//------------------------------------------------------- onStartCommand -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals(START_FOREGROUND_SYNC)) {
                Log.e(TAG, "Start Foreground Sync Service");
                startForeground(FOREGROUND_SYNC_NOTIFICATION_ID, getNotification());
                Sync();
            }
            else if (intent.getAction().equals(STOP_FOREGROUND_SYNC)) {
                Log.e(TAG, "Stop Foreground Sync Service");
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;

    }

//------------------------------------------------------- InitDatabase -------------------------------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {dataBaseHelper = new DataBaseHelper(this);    }

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
        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.app_icon_new2)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon_new2))
                .setContentTitle("Sync")
                .setContentText("Data Sync Start Please Wait.....")
                .setSound(sound);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        builder.setAutoCancel(true);

        // here we again call For groundLocationService to turn off the tracking!
        Intent exitIntent = new Intent(this, SyncService.class);
        exitIntent.setAction(STOP_FOREGROUND_SYNC);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pexitIntent = PendingIntent.getService(this, 0, exitIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.addAction(R.mipmap.ic_launcher, getResources().getString(R.string.text_exit).toUpperCase(), pexitIntent);
        }

        return builder.build();
    }

    private void getSyncSuccessfullyNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID3, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            mChannel.enableVibration(true);
            manager.createNotificationChannel(mChannel);
        }


        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_new2)
                    .setContentTitle("Sync")
                    .setContentText("Data Sync Successfully")
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID3 )
                     .setSound(sound)
                    .build();
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID3, "Sync", NotificationManager.IMPORTANCE_DEFAULT);
                mChannel.setVibrationPattern(new long[]{0,1000,500,1000});
                mChannel.enableVibration(true);
                manager.createNotificationChannel(mChannel);
        }
        else{
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_new2)
                    .setContentTitle("Sync")
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setVibrate(new long[]{1000,500,1000,500})
                    .setContentText("Data Sync Successfully")
                    .build();
        }
        manager.notify(SYNC_SUCCESSFULLY_NOTIFICATION_ID,notification);
    }

    private void getSyncFailNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID3, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            mChannel.enableVibration(true);
            manager.createNotificationChannel(mChannel);
        }

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_new2)
                    .setContentTitle("Sync")
                    .setContentText("Data Sync Fail")
                    .setSound(sound)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID3 )
                    .build();
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID3, "Sync", NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            mChannel.enableVibration(true);
            manager.createNotificationChannel(mChannel);
        }
        else{
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_new2)
                    .setContentTitle("Sync")
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setVibrate(new long[]{1000,500,1000,500})
                    .setContentText("Data Sync Successfully")
                    .build();
        }
        manager.notify(SYNC_SUCCESSFULLY_NOTIFICATION_ID,notification);
    }

//------------------------------------------------------- onBind -------------------------------------------------------------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//------------------------------------------------------- onDestroy -------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }

//------------------------------------------------------- Sync Data -----------------------------------------------------------------------------------------------------------------------------------------
    // Sync
    private void Sync(){
        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
        ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();
        // Online Layer List!
        ArrayList<ProjectLayerModel> list  = dataBaseHelper.getGISSurveyLayersList();
        ArrayList<String> onlineLayerList = new ArrayList<>();
        if(list.size() > 0){
            JSONArray jsonArray = new JSONArray();
            for(int i=0; i<list.size(); i++){
                Log.e(TAG,"");
                Log.e(TAG, "custom Made " + list.get(i).isCustomMade());
                // Change Happen then
                if(list.get(i).isCustomMade()){
                    try{
                        JSONObject j1 = new JSONObject();
                        j1.put("layer_id",list.get(i).getLayerID());
                        j1.put("user_id",Utility.getSavedData(this,Utility.LOGGED_USERID));

                        JSONObject j = new JSONObject(list.get(i).getLatLong());
                        j1.put("latlong",j);
                        jsonArray.put(j1);
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
            Log.e(TAG,"Array -> "+ jsonArray.toString());
            if(jsonArray.length() > 0){
                onlineLayerList.add(jsonArray.toString());
            }
        }

        // here when there is not data present in local database then logout directly!
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0 && onlineLayerList.size() == 0){
            Log.e(TAG, "Sync Service No Data Found in Local DataBase");
            stopSyncService();
        }
        else
        {
            Toast.makeText(this, "Sync Started", Toast.LENGTH_SHORT).show();
            // local Database contain some Data!
            Log.e(TAG, "Sync Service Local Database Contain some Data");
            Log.e(TAG, (cameraImageList.size() > 0 ? "Sync Service Camera    Data present"    : "Sync Service Camera Data not Present"));
            Log.e(TAG, (timeLineList.size()    > 0 ? "Sync Service TimeLine  Data present"    : "Sync Service TimeLine Data not Present"));
            Log.e(TAG, (gpsTrackingList.size() > 0 ? "Sync Service Gps Tracking Data present" : "Sync Service Gps Tracking Data not Present"));
            Log.e(TAG, (trackingList.size()    > 0 ? "Sync Service Tracking  Data present"    : "Sync Service Tracking Data not Present"));
            Log.e(TAG, (formDataList.size()    > 0 ? "Sync Service Form      Data present"    : "Sync Service Form Data not Present"));
            Log.e(TAG, (mapCameraList.size()   > 0 ? "Sync Service MapCamera Data present"    : "Sync Service Map Camera Data not Present"));
            Log.e(TAG, (gisCameraList.size()   > 0 ? "Sync Service GISCamera Data present"    : "Sync Service GIS Camera Data not Present"));
            Log.e(TAG, (gisFormDataList.size() > 0 ? "Sync Service GIS Form  Data present"    : "Sync Service GIS Form Data not Present"));
            Log.e(TAG, (onlineLayerList.size() > 0 ? "Sync Service OnlineLayer Data present"  : "Sync Service OnlineLayer Data not Present"));

            if(cameraImageList.size() > 0 || gpsTrackingList.size() > 0 || formDataList.size() > 0 || trackingList.size() > 0 || timeLineList.size() > 0 || mapCameraList.size() > 0 || gisCameraList.size() > 0 || onlineLayerList.size() > 0){

                // Camera Data!
                if(cameraImageList.size() > 0){
                    Log.e(TAG, "Sync Service Camera On");
                    new SyncService.CameraSyncToServe(this).execute();
                }
                else
                {
                    isCameraSync = true;
                }

                // GPS Tracking Data!
                if(gpsTrackingList.size() > 0){
                    Log.e(TAG, "Sync Service GPS Tracking On");
                    syncGPSTrackingList = dataBaseHelper.getGpsTracking();
                    SyncGPSTrackingDetails();
                }
                else{
                    isGPSTrackingSync = true;
                }

                // Online Layer
                if(onlineLayerList.size() > 0){
                    Log.e(TAG,"Sync Service OnlineLayer On");
                    syncOnlineLayerList = onlineLayerList;// new ArrayList<>();//dataBaseHelper.getGISSurveyOnlineLayersList();
                    SyncOnlineLayerDetails();
                }
                else{
                    isOnlineLayerSync = true;
                }

                // Form Data!
                if(formDataList.size() > 0){
                    Log.e(TAG, "Sync Service Form On");
                    syncFormDataList = dataBaseHelper.getProjectFormList();
                    if(gisFormDataList.size() > 0){
                        syncFormDataList.addAll(dataBaseHelper.getGISSurveyFormList());
                    }
                    Log.e(TAG, "Sync Form Size: "+ syncFormDataList.size());
                    SyncFormDataDetails();
                }
                else{
                    isFormSync = true;
                }

                // Tracking Data!
                if(trackingList.size() > 0){
                    Log.e(TAG, "Sync Service Tracking On");
                    syncTrackingList = dataBaseHelper.getTrackingStatusDetails();
                    SyncTrackingDataDetails();
                }
                else{
                    isTrackingSync = true;
                }

                // Time Line Data!
                if(timeLineList.size() > 0){
                    Log.e(TAG, "Sync Service TimeLine On");
                    new SyncService.TimeLineSyncToServe(this).execute();
                }
                else{
                    isTimeLineSync = true;
                }

                // Map  Camera Data!
                if(mapCameraList.size() > 0){
                    Log.e(TAG, "Sync Service MapCamera On");
                    new SyncService.MapCameraSyncToServe(this).execute();
                }
                else{
                    isMapCameraSync = true;
                }

                // GIS  Camera Data!
                if(gisCameraList.size() > 0){
                    Log.e(TAG, "Sync Service GISCamera On");
                    new SyncService.GISCameraSyncToServe(this).execute();
                }
                else{
                    isGISCameraSync = true;
                }

            }
        }
    }

    // Tracking SYNC DATA
    private void SyncTrackingDataDetails(){
        if(syncTrackingList != null && syncTrackingList.size() > 0){
            isTrackingSync = false;
            trackingStatusModel = syncTrackingList.get(0);
            syncTrackingList.remove(0);
            SyncTrackingDataToServer(trackingStatusModel);
        }
        else{
            isTrackingSync = true;
            Log.e(TAG, "Sync Service Tracking Off");
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Sync Data Successfully");
                stopSyncService();
            }
        }
    }
    private void SyncTrackingDataToServer(TrackingStatusData trackingStatusData){
        if(SystemUtility.isInternetConnected(this)) {
            Map<String, String> params = new HashMap<>();
            String data = "";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(URL_Utility.PARAM_LOGIN_USER_ID, trackingStatusData.getUser_id());
                jsonObject.put(URL_Utility.PARAM_LATITUDE, trackingStatusData.getLatitude());
                jsonObject.put(URL_Utility.PARAM_LONGITUDE, trackingStatusData.getLongitude());
                jsonObject.put(URL_Utility.PARAM_TRACKING_CREATED_DATE, trackingStatusData.getCreated_date());
                jsonObject.put(URL_Utility.PARAM_VERSION, trackingStatusData.getVersion());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                data = AESCrypt.encrypt(jsonObject.toString());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            params.put("data",data);
            URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_USER_TRACK_LOCAL;
            BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_USER_TRACKING, params, false, false);
        }

    }

    // FORM SYNC DATA
    private void SyncFormDataDetails(){
        if(syncFormDataList != null && syncFormDataList.size() > 0){
            isFormSync     = false;
            isFileUpload   = true;
            isCameraUpload = true;
            isVideoUpload  = true;
            isAudioUpload  = true;
            formDataModel  = syncFormDataList.get(0);
            syncFormDataList.remove(0);
            SyncFormToServer(formDataModel);
        }
        else{
            isFileUpload   = true;
            isCameraUpload = true;
            isVideoUpload  = true;
            isAudioUpload  = true;
            isFormSync     = true;
            Log.e(TAG, "Sync Service Form Off");
            /// here no Data found in form
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync   && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Sync Data Successfully");
                stopSyncService();
            }
        }
    }
    private void SyncFormToServer(FormDataModel formDataModel){
        Map<String, String> params = new HashMap<>();
        String data = "";
        try {
            data = AESCrypt.encrypt((formDataModel.getFormData()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_LAYER_FORM_UPLOAD, params, false, false);
    }

    // Online Sync Data
    private void SyncOnlineLayerDetails(){
        if(syncOnlineLayerList != null && syncOnlineLayerList.size() > 0){
            isOnlineLayerSync = false;
            syncProjectLayerModel = syncOnlineLayerList.get(0);
            syncOnlineLayerList.remove(0);
            SyncOnlineLayerDataToServe(syncProjectLayerModel);
        }
        else{
            isOnlineLayerSync = true;
            Log.e(TAG, "Sync Service Online Layer Off");
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Sync Data Successfully");
                stopSyncService();
            }
        }
    }

    private void SyncOnlineLayerDataToServe(String bin){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("online_layer",  (bin));
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
     //   Log.e(TAG,""+ params.toString());
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_ONLINE_LAYER;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_ONLINE_LAYER, params, false, false);

    }

    // GPS TRACKING SYNC DATA
    private void SyncGPSTrackingDetails(){
        if(syncGPSTrackingList != null && syncGPSTrackingList.size() > 0){
            isGPSTrackingSync = false;
            gpsSyncTrackingModule = syncGPSTrackingList.get(0);
            syncGPSTrackingList.remove(0);
            SyncGPSTrackingDataToServe(gpsSyncTrackingModule);
        }
        else{
            isGPSTrackingSync = true;
            Log.e(TAG, "Sync Service GPS Tracking Off");
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Sync Data Successfully");
                stopSyncService();
            }
        }
    }
    private void SyncGPSTrackingDataToServe(GpsTrackingModule gpsTrackingModule){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID,  gpsTrackingModule.getUser_id());
            jsonObject.put(URL_Utility.PARAM_DATETIME, gpsTrackingModule.getDatetime());
            jsonObject.put(URL_Utility.PARAM_VERSION,  gpsTrackingModule.getVersion());
            jsonObject.put(URL_Utility.PARAM_WT_TOKEN,    gpsTrackingModule.getToken());
            jsonObject.put(URL_Utility.PARAM_TYPE,     gpsTrackingModule.getType());
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(this,Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.PARAM_LATLON,   gpsTrackingModule.getLatLong());
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_GPS_TRACKING_UPLOAD, params, false, false);
    }


//------------------------------------------------------- onSuccessResponse -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {

        // Tracking UPLOAD Data
        if (responseCode == URL_Utility.ResponseCode.WS_USER_TRACK_LOCAL) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")){
                try {
                    JSONObject mLoginObj = new JSONObject(res);
                    String msg = mLoginObj.optString("status");
                    if (msg.equalsIgnoreCase("Success")) {

                        if (trackingStatusModel != null && trackingStatusModel.getId() != null) {
                            // then
                            if (dataBaseHelper.getTrackingStatusDetails().size() > 0) {
                                dataBaseHelper.deleteTrackingStatusData(trackingStatusModel.getId());
                            }
                            SyncTrackingDataDetails();
                        }
                    }
                    else{
                        Log.e(TAG, "Sync Service Tracking Api status fail / not working");
                        isTrackingSync = true;
                        Utility.saveData(this,Utility.IS_USER_TRACKING,false);
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }

                    }
                }
                catch (JSONException e) {
                    // Fail
                    Log.e(TAG, "Sync Service Tracking Api not working");
                    isTrackingSync = true;
                    Utility.saveData(this,Utility.IS_USER_TRACKING,false);
                    Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                isTrackingSync = true;
                Log.e(TAG, "Sync Service Tracking Api null");
                Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
        }

        // GPS TRACKING UPLOAD DATA
        if (responseCode == URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Sync Service Gps Tracking Status: "+status);
                    // Success
                    if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)) {
                        if(gpsSyncTrackingModule != null && gpsSyncTrackingModule.getId() != null){
                            if(dataBaseHelper.getGpsTracking().size() > 0){
                                dataBaseHelper.deleteGPSTracking(gpsSyncTrackingModule.getId());
                                Log.e(TAG, "Sync Service Delete Gps Tracking Data form Local Database: " + gpsSyncTrackingModule.getId());
                            }
                            SyncGPSTrackingDetails();
                        }
                    }
                    // Duplicate
                    else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                        if(gpsSyncTrackingModule != null && gpsSyncTrackingModule.getId() != null){
                            if(dataBaseHelper.getGpsTracking().size() > 0){
                                dataBaseHelper.deleteGPSTracking(gpsSyncTrackingModule.getId());
                                Log.e(TAG, "Sync Service Delete Duplicate Gps Tracking Data form Local Database: " + gpsSyncTrackingModule.getId());
                            }
                            SyncGPSTrackingDetails();
                        }
                    }
                    // Fail
                    else{
                        Log.e(TAG, "Sync Service Gps Tracking Api status fail / not working");
                        isGPSTrackingSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG,"Sync Service Gps Tracking Api not working");
                    isGPSTrackingSync = true;
                    Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                isGPSTrackingSync = true;
                Log.e(TAG," Sync Service Gps Tracking Api null");
                Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
        }

        // FORM UPLOAD DATA
        if (responseCode == URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD) {
            String res = AESCrypt.decryptResponse(response);
            // Response not Null
            if(!res.equals("")){
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Sync Service Form Data Status : "+ status);
                    // Success
                    if (status.equalsIgnoreCase("Success")){
                        // here form is uploaded know!
                        // here we check is any file need to upload or not!
                       // Log.e(TAG, formDataModel.getFormData());
                        JSONObject mLoginObj = new JSONObject(formDataModel.getFormData());
                        JSONArray jsonResponse = new JSONArray(mLoginObj.getString("form_data"));
                        Gson gson = new Gson();
                        java.lang.reflect.Type listType = new TypeToken<ArrayList<FormDetailData>>() {}.getType();

                        ArrayList<FormDetailData> listFormDetailsData = gson.fromJson(jsonResponse.toString(), listType);
                        //Log.e(TAG, ""+listFormDetailsData.size());
                        boolean isFile   = false;
                        boolean isCamera = false;
                        boolean isVideo  = false;
                        boolean isAudio  = false;
                        HashMap<String,String> fileData       = new HashMap<>();
                        HashMap<String,String> cameraFileData = new HashMap<>();
                        HashMap<String,String> videoFileData  = new HashMap<>();
                        HashMap<String,String> audioFileData  = new HashMap<>();

                        if(listFormDetailsData != null && listFormDetailsData.size() > 0){

                            for(int i = 0; i < listFormDetailsData.size(); i++){
                                // File Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_FILE)){
                                    isFile = true;
                                    Log.e(TAG, "File Contain in Form");
                                    String fileColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG,"File Selected");
                                        fileData.put(fileColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG,"File Not Selected");
                                    }
                                }
                                // Camera Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_CAMERA)) {
                                    isCamera = true;
                                    Log.e(TAG, "Camera Contain in Form");
                                    String cameraColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG, "Camera  File Selected");
                                        cameraFileData.put(cameraColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG, "Camera  File Not Selected");
                                    }
                                }
                                // Video Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_VIDEO)) {
                                    isVideo = true;
                                    Log.e(TAG, "Video Contain in Form");
                                    String videoColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG, "Video File Selected");
                                        videoFileData.put(videoColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG, "Video File Not Selected");
                                    }
                                }
                                // Audio Type Data
                                if (listFormDetailsData.get(i).getType().equals(TYPE_AUDIO)) {
                                    isAudio = true;
                                    Log.e(TAG, "Audio Contain in Form");
                                    String audioColumnName = listFormDetailsData.get(i).getColumn_name();
                                    if(!Utility.isEmptyString(listFormDetailsData.get(i).getFill_value())) {
                                        Log.e(TAG,"Audio File Selected");
                                        audioFileData.put(audioColumnName,getUploadFilePath(listFormDetailsData,i));
                                    }
                                    else{
                                        Log.e(TAG,"Audio File Not Selected");
                                    }
                                }
                            }

                            if (isFile && !fileData.isEmpty() || isCamera && !cameraFileData.isEmpty() || isVideo && !videoFileData.isEmpty() || isAudio && !audioFileData.isEmpty()){

                                if (isFile && !fileData.isEmpty()) {
                                    Log.e(TAG, "User Selected File");
                                    new SyncService.FileUploadServer(this,fileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_FILE).execute();
                                }
                                else{
                                    isFileUpload = true;
                                }

                                if (isCamera && !cameraFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Camera");
                                    new SyncService.FileUploadServer(this,cameraFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_CAMERA).execute();
                                }
                                else{
                                    isCameraUpload = true;
                                }
                                if (isVideo && !videoFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Video");
                                    new SyncService.FileUploadServer(this,videoFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_VIDEO).execute();
                                }
                                else{
                                    isVideoUpload = true;
                                }
                                if (isAudio && !audioFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Audio");
                                    new SyncService.FileUploadServer(this,audioFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_AUDIO).execute();
                                }
                                else{
                                    isAudioUpload = true;
                                }
                            }
                            else{
                                Log.e(TAG,"Sync Service User Submit Normal Form Does not contain any file");
                                if(formDataModel != null && formDataModel.getID() != null){
                                    // then
                                    if(dataBaseHelper.getProjectFormList().size() > 0){
                                        dataBaseHelper.deleteProjectFormDetails(formDataModel.getID());
                                        Log.e(TAG, "Sync Service Delete Form Data: "+ formDataModel.getID());
                                    }
                                    SyncFormDataDetails();
                                }
                            }
                        }
                    }
                    // Duplicate
                    else if(status.equalsIgnoreCase("Duplicate")){
                        if(formDataModel != null && formDataModel.getID() != null) {
                            // then
                            if(dataBaseHelper.getProjectFormList().size() > 0){
                                dataBaseHelper.deleteProjectFormDetails(formDataModel.getID());
                                Log.e(TAG, "Sync Service Delete Form Data: "+ formDataModel.getID());
                            }
                            SyncFormDataDetails();
                        }
                    }
                    // Failure
                    else {
                        isFormSync = true;
                        Log.e(TAG,"Sync Service Form Api status fail / not working");
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e) {
                    Log.e(TAG,e.getMessage());
                    isFormSync = true;
                    Log.e(TAG, "Sync Service Form Api not working");
                    Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();

                }
            }
            // Response Null
            else{
                isFormSync = true;
                Log.e(TAG,"Sync Service Form Api null");
                Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
        }

        // Online Layer Data
        if(responseCode == URL_Utility.ResponseCode.WS_ONLINE_LAYER){
            String res = AESCrypt.decryptResponse(response);
            Log.e(TAG, "Response -> " + res);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Sync Service Online Layer Status: "+status);
                    // Success
                    if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)) {
                        if(!Utility.isEmptyString(syncProjectLayerModel)){
                            if(dataBaseHelper.getGISSurveyLayersList().size() > 0){
                                ArrayList<ProjectLayerModel> list = dataBaseHelper.getGISSurveyLayersList();
                                for(int i=0; i < list.size(); i++){
                                    list.get(i).setCustomMade(false);
                                    ProjectLayerModel bin = list.get(i);
                                  dataBaseHelper.updateOneGISSurveyLayerData(bin);
                                }
                                dataBaseHelper.getGISSurveyLayersList();
                                //Log.e(TAG, "Sync Service Delete Online Layer Data form Local Database: " + syncProjectLayerModel.getID());
                            }
                            SyncOnlineLayerDetails();
                        }
                    }
                    // Duplicate
                    else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
//                        if(syncProjectLayerModel != null && syncProjectLayerModel.getID() != null){
//                            if(dataBaseHelper.getGISSurveyOnlineLayersList().size() > 0){
//                              //  dataBaseHelper.deleteGISSurveyOnlineLayers(syncProjectLayerModel.getID());
//                                syncProjectLayerModel.setIsLayerChange("f");
//                                dataBaseHelper.updateGISSurveyOnlineLayersDataViewOnly(syncProjectLayerModel);
//                                Log.e(TAG, "Sync Service Delete Duplicate Online Layer Data form Local Database: " + syncProjectLayerModel.getID());
//                            }
//                            SyncOnlineLayerDetails();
//                        }
                    }
                    // Fail
                    else{
                        Log.e(TAG, "Sync Service Online Layer Api status fail / not working");
                        isOnlineLayerSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }

                    }
                }
                catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG,"Sync Service Online layer Api not working");
                    isGISCameraSync = true;
                    Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                isOnlineLayerSync = true;
                //stopService();
                Log.e(TAG," Sync Service Online Layer Api null");
                Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
        }
    }

//------------------------------------------------------- onErrorResponse -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        Log.e(TAG, "Error Response Code: "+responseCode+" Error Message: "+error.getMessage());
        Toast.makeText(this, "Sync Fail", Toast.LENGTH_SHORT).show();
        stopService();
    }

//------------------------------------------------------- Form File Upload to Server -----------------------------------------------------------------------------------------------------------------------------------------

    private class FileUploadServer extends AsyncTask<Void, Integer, String> {
        HashMap<String,String> filePathData;
        String form_id;
        String unique_number;
        String type;

        Context context;

        public FileUploadServer(Context context,HashMap<String,String> filePathData, String form_id, String unique_number,String type) {
            this.filePathData = filePathData;
            this.form_id = form_id;
            this.unique_number = unique_number;
            this.type = type;
            this.context = context;

            if(type.equals(TYPE_FILE)){
                Log.e(TAG, "File Type ");
                isFileUpload = false;
            }
            else if(type.equals(TYPE_CAMERA) ){
                Log.e(TAG, "Camera Type ");
                isCameraUpload = false;
            }
            else if(type.equals(TYPE_VIDEO) ){
                Log.e(TAG, "Video Type ");
                isVideoUpload = false;
            }
            else if(type.equals(TYPE_AUDIO) ){
                Log.e(TAG, "Audio Type ");
                isAudioUpload = false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_FORM_FILE_UPLOAD);
            try {

                if(filePathData != null){
                    if(!filePathData.isEmpty()){
                        // outer Loop
                        for(Map.Entry<String,String> entry: filePathData.entrySet()) {
                            String col_name = entry.getKey();
                            // File Path!
                            String[] path = entry.getValue().split(",");
                            for (String filepath : path) {
                                File sourceFile = new File(filepath);
                                String data = "";
                                JSONObject params = new JSONObject();
                                try {
                                    params.put("formID", form_id);
                                    params.put("unique_number", unique_number);
                                    params.put("column_name", col_name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Encrypt Data!
                                data = AESCrypt.encrypt(params.toString());
                                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                                entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                                entity.addPart("data", new StringBody(data));
                                totalSize = entity.getContentLength();
                                httppost.setEntity(entity);
                                HttpResponse response = httpclient.execute(httppost);
                                HttpEntity r_entity = response.getEntity();
                                int statusCode = response.getStatusLine().getStatusCode();

                                if (statusCode == 200) {
                                    responseString = EntityUtils.toString(r_entity);
                                    String res = AESCrypt.decryptResponse(responseString);
                                    if (!res.equals("")) {
                                        try {
                                            JSONObject mLoginObj = new JSONObject(res);
                                            String status = mLoginObj.optString("status");
                                            if (status.equalsIgnoreCase("Success")) {
                                                Log.e("Form", status);
                                            } else {
                                                Log.e(TAG, status);
                                                isFormSync = true;
                                            }
                                        } catch (JSONException e) {
                                            Log.e(TAG,e.getMessage());
                                            isFormSync = true;
                                            stopService();
                                        }
                                    }
                                    else {
                                        isFormSync = true;
                                    }
                                } else {
                                    isFormSync = true;
                                    responseString = "Error occurred! Http Status Code: " + statusCode;
                                    Log.e(TAG, responseString);
                                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                                    stopService();
                                }
                            }
                        }
                    }
                }
                else{
                    isFormSync = true;
                }

            } catch (IOException e) {
                isFormSync = true;
                Log.e(TAG, e.getMessage());
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();

            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
                isFormSync = true;
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")){
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")){

                        switch (type) {
                            case TYPE_FILE:
                                Log.e(TAG, "Sync Service File Type Successfully");
                                isFileUpload = true;
                                break;
                            case TYPE_CAMERA:
                                Log.e(TAG, "Sync Service Camera Type Successfully");
                                isCameraUpload = true;
                                break;
                            case TYPE_VIDEO:
                                Log.e(TAG, "Sync Service Video Type Successfully");
                                isVideoUpload = true;
                                break;
                            case TYPE_AUDIO:
                                Log.e(TAG, "Sync Service Audio Type Successfully");
                                isAudioUpload = true;
                                break;
                        }

                        if((isCameraUpload && isVideoUpload && isAudioUpload && isFileUpload)){
                            Log.e(TAG,"Sync Service Form Upload Successfully");
                            if(formDataModel != null && formDataModel.getID() != null){
                                // then
                                if(dataBaseHelper.getProjectFormList().size() > 0){
                                    dataBaseHelper.deleteProjectFormDetails(formDataModel.getID());
                                    Log.e(TAG,"Sync Service Delete Form Data: "+ formDataModel.getID());
                                }
                                SyncFormDataDetails();
                            }
                        }
                    }
                    else{
                        Log.e(TAG,status);
                        isFormSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }

                } catch (JSONException e) {
                    isFormSync = true;
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                Log.e(TAG, response);
                isFormSync = true;
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();

            }
            super.onPostExecute(result);
        }
    }

    private class CameraSyncToServe extends AsyncTask<Void, Integer, String> {

        Context context;

        public CameraSyncToServe(Context context) {
            isCameraSync = false;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_CAMERA_IMAGE);
            try {
                ArrayList<CameraModule> imageList = dataBaseHelper.getCameraImage();

                for(int i=0; i<imageList.size(); i++){
                    String id = imageList.get(i).getId();
                    String user_id = imageList.get(i).getUserId();
                    String latitude = imageList.get(i).getImageLat();
                    String longitude = imageList.get(i).getImageLon();
                    String version = imageList.get(i).getVersion();
                    String datetime = imageList.get(i).getImageDateTime();
                    String imagePath = imageList.get(i).getImagePath();
                    String desc  = imageList.get(i).getImageDesc();
                    // Normal data Upload!
                    File sourceFile = new File(imagePath);
                    String data = "";
                    JSONObject params = new JSONObject();
                    try {
                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, user_id);
                        params.put(URL_Utility.ACTION_APP_VERSION, version);
                        params.put(URL_Utility.PARAM_LATITUDE, latitude);
                        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
                        params.put(URL_Utility.PARAM_DATETIME, datetime);
                        params.put(URL_Utility.PARAM_IMAGE_DESC, desc);
                        // Login Token
                        params.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(SyncService.this,Utility.LOGGED_TOKEN));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Encrypt Data!
                    data = AESCrypt.encrypt(params.toString());

                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                    entity.addPart("data", new StringBody(data));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("Response",AESCrypt.decryptResponse(responseString));
                        String res = AESCrypt.decryptResponse(responseString);
                        if(!res.equals("")) {
                            try {
                                JSONObject mLoginObj = new JSONObject(res);
                                String status = mLoginObj.optString("status");
                                Log.e(TAG,"Sync Service Camera Data Status: "+ status );
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete Duplicate Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Log.e(TAG, "Sync Service Camera Api status Fail / not working");
                                    isCameraSync = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "Sync Service Camera Api not working");
                                isCameraSync = true;
                                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                                stopService();

                            }
                        }
                        else{
                            Log.e(TAG,"Sync Service Camera Api null");
                            isCameraSync = true;
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();

                        }
                    }
                    else {
                        isCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                        Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                        stopService();
                    }
                }
            } catch (IOException e) {
                isCameraSync = true;
                responseString = e.toString();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();

            } catch (Exception exception) {
                exception.printStackTrace();
                isCameraSync = true;
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        isCameraSync = true;
                        Log.e(TAG, "Sync Service Camera Off");
                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Log.e(TAG, "Sync Data Successfully");
                            stopSyncService();
                        }
                    }

                    else{
                        Log.e(TAG, "Sync Service Camera Api status Fail / not working");
                        isCameraSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    isCameraSync = true;
                    Log.e(TAG, "Sync Service Camera Api not working");
                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                Log.e(TAG, response);
                Log.e(TAG, "Sync Service Camera Api Data null");
                isCameraSync = true;
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- TimeLine Sync To Server -----------------------------------------------------------------------------------------------------------------------------------------

    private class TimeLineSyncToServe extends AsyncTask<Void, Integer, String> {

        Context context;

        public TimeLineSyncToServe(Context context)
        {
            isTimeLineSync = false;
            this.context  =context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_TIMELINE_IMAGE);
            try {
                ArrayList<CameraModule> imageList = dataBaseHelper.getTimeLineImage();

                for(int i=0; i<imageList.size(); i++){
                    String id = imageList.get(i).getId();
                    String user_id = imageList.get(i).getUserId();
                    String latitude = imageList.get(i).getImageLat();
                    String longitude = imageList.get(i).getImageLon();
                    String version = imageList.get(i).getVersion();
                    String datetime = imageList.get(i).getImageDateTime();
                    String imagePath = imageList.get(i).getImagePath();
                    String desc  = imageList.get(i).getImageDesc();
                    // Normal data Upload!
                    File sourceFile = new File(imagePath);
                    String data = "";
                    JSONObject params = new JSONObject();
                    try {
                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, user_id);
                        params.put(URL_Utility.ACTION_APP_VERSION, version);
                        params.put(URL_Utility.PARAM_LATITUDE, latitude);
                        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
                        params.put(URL_Utility.PARAM_DATETIME, datetime);
                        params.put(URL_Utility.PARAM_IMAGE_DESC, desc);
                        // Login Token
                        params.put(URL_Utility.PARAM_UNIQUE_NUMBER,Utility.getToken());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Encrypt Data!
                    data = AESCrypt.encrypt(params.toString());

                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                    entity.addPart("data", new StringBody(data));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("Response",AESCrypt.decryptResponse(responseString));
                        String res = AESCrypt.decryptResponse(responseString);
                        if(!res.equals("")) {
                            try {
                                JSONObject mLoginObj = new JSONObject(res);
                                String status = mLoginObj.optString("status");
                                Log.e(TAG,"Sync Service TimeLine Data Status: "+ status);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteTimeLineImage(id);
                                        Log.e(TAG, "Sync Service Delete TimeLine Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteTimeLineImage(id);
                                        Log.e(TAG, "Sync Service Delete Duplicate TimeLine Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Log.e(TAG, "Sync Service TimeLine Api status fail / not working");
                                    isTimeLineSync = true;
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, ""+ e.getMessage());
                                Log.e(TAG, "Sync Service TimeLine Api status Fail / not working");
                                isTimeLineSync = true;
                                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                                stopService();
                            }
                        }
                        else{
                            Log.e(TAG, "Sync Service TimeLine Api null");
                            isTimeLineSync = true;
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                    else {
                        isTimeLineSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                        Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                        stopService();
                    }
                }
            } catch (IOException e) {
                isTimeLineSync = true;
                responseString = e.toString();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            } catch (Exception exception) {
                isTimeLineSync = true;
                exception.printStackTrace();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        isTimeLineSync = true;
                        Log.e(TAG, "Sync Service TimeLine Off");
                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Log.e(TAG, "Sync Data Successfully");
                            stopSyncService();
                        }
                    }
                    else{
                        isTimeLineSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    isTimeLineSync = true;
                    Log.e(TAG, "Sync Service TimeLine Api not Work");
                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                Log.e(TAG, response);
                isTimeLineSync = true;
                Log.e(TAG, "Sync Service TimeLine Api null");
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();

            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- Map Camera Sync To Server -----------------------------------------------------------------------------------------------------------------------------------------

    private class MapCameraSyncToServe extends AsyncTask<Void, Integer, String> {

        Context context;
        public MapCameraSyncToServe(Context context) {
            isMapCameraSync = false;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_MAP_CAMERA_IMAGE);
            try {
                ArrayList<CameraModule> imageList = dataBaseHelper.getMapCameraImage();

                for(int i=0; i<imageList.size(); i++){
                    String id        = imageList.get(i).getId();
                    String user_id   = imageList.get(i).getUserId();
                    String project_id= imageList.get(i).getProjectId();
                    String work_id   = imageList.get(i).getWorkId();
                    String latitude  = imageList.get(i).getImageLat();
                    String longitude = imageList.get(i).getImageLon();
                    String version   = imageList.get(i).getVersion();
                    String datetime  = imageList.get(i).getImageDateTime();
                    String imagePath = imageList.get(i).getImagePath();
                    String desc      = imageList.get(i).getImageDesc();
                    // Normal data Upload!
                    File sourceFile = new File(imagePath);
                    String data = "";
                    JSONObject params = new JSONObject();
                    try {
                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, user_id);
                        params.put(URL_Utility.PARAM_PROJECT_ID, project_id);
                        params.put(URL_Utility.PARAM_WORK_ID, work_id);
                        params.put(URL_Utility.ACTION_APP_VERSION, version);
                        params.put(URL_Utility.PARAM_LATITUDE, latitude);
                        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
                        params.put(URL_Utility.PARAM_DATETIME, datetime);
                        params.put(URL_Utility.PARAM_IMAGE_DESC, desc);
                        // Login Token
                        params.put(URL_Utility.PARAM_UNIQUE_NUMBER,Utility.getToken());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Encrypt Data!
                    data = AESCrypt.encrypt(params.toString());

                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                    entity.addPart("data", new StringBody(data));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("Response",AESCrypt.decryptResponse(responseString));
                        String res = AESCrypt.decryptResponse(responseString);
                        if(!res.equals("")) {
                            try {
                                JSONObject mLoginObj = new JSONObject(res);
                                String status = mLoginObj.optString("status");
                                Log.e(TAG,"Sync Service Map Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete Duplicate Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Log.e(TAG,"Sync Service Map Camera Api status fail / not working");
                                    isMapCameraSync = true;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, "Sync Service Map Camera  Api status fail / not working");
                                isMapCameraSync = true;
                                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                                stopService();
                            }
                        }
                        else{
                            Log.e(TAG,"Sync Service Map Camera Api null");
                            isMapCameraSync = true;
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                    else {
                        isMapCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                        Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                        stopService();
                    }
                }
            } catch (IOException e) {
                isMapCameraSync = true;
                responseString = e.toString();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            } catch (Exception exception) {
                isMapCameraSync = true;
                exception.printStackTrace();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        isMapCameraSync = true;
                        Log.e(TAG, "Sync Service Map Camera Off");

                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            //dismissProgressBar();
                            Log.e(TAG, "Sync Data Successfully");
                            stopSyncService();
                        }
                    }
                    else{
                       Log.e(TAG, "Sync Service Map Camera Api status fail / not working");
                       isMapCameraSync = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    isMapCameraSync = true;
                    Log.e(TAG, "Sync Service Map Camera Api not work");
                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                Log.e(TAG, response);
                isMapCameraSync = true;
                Log.e(TAG,"Sync Service Map Camera Api null");
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();

            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- GIS Camera Sync To Server -----------------------------------------------------------------------------------------------------------------------------------------

    private class GISCameraSyncToServe extends AsyncTask<Void, Integer, String> {

        Context context;
        public GISCameraSyncToServe(Context context)
        {
            this.context = context;
            isGISCameraSync = false;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile(){
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_GIS_CAMERA_IMAGE);
            try {
                ArrayList<CameraModule> imageList = dataBaseHelper.getGISMapCameraImage();

                for(int i=0; i<imageList.size(); i++){
                    String id        = imageList.get(i).getId();
                    String user_id   = imageList.get(i).getUserId();
                    String survey_id = imageList.get(i).getSurveyId();
                    String work_id   = imageList.get(i).getWorkId();
                    String latitude  = imageList.get(i).getImageLat();
                    String longitude = imageList.get(i).getImageLon();
                    String version   = imageList.get(i).getVersion();
                    String datetime  = imageList.get(i).getImageDateTime();
                    String imagePath = imageList.get(i).getImagePath();
                    String desc      = imageList.get(i).getImageDesc();
                    // Normal data Upload!
                    File sourceFile = new File(imagePath);
                    String data = "";
                    JSONObject params = new JSONObject();
                    try {
                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, user_id);
                        params.put(URL_Utility.PARAM_SURVEY_ID, survey_id);
                        params.put(URL_Utility.PARAM_WORK_ID, work_id);
                        params.put(URL_Utility.ACTION_APP_VERSION, version);
                        params.put(URL_Utility.PARAM_LATITUDE, latitude);
                        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
                        params.put(URL_Utility.PARAM_DATETIME, datetime);
                        params.put(URL_Utility.PARAM_IMAGE_DESC, desc);
                        // Login Token
                        params.put(URL_Utility.PARAM_UNIQUE_NUMBER,Utility.getToken());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Encrypt Data!
                    data = AESCrypt.encrypt(params.toString());

                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
                    entity.addPart("data", new StringBody(data));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    int statusCode = response.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("Response",AESCrypt.decryptResponse(responseString));
                        String res = AESCrypt.decryptResponse(responseString);
                        if(!res.equals("")) {
                            try {
                                JSONObject mLoginObj = new JSONObject(res);
                                String status = mLoginObj.optString("status");
                                Log.e(TAG,"Sync Service GIS Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteGISMapCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete GIS Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteGISMapCameraImage(id);
                                        Log.e(TAG, "Sync Service Delete Duplicate GIS Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Log.e(TAG, "Sync Service GIS Camera Api status fail / not working");
                                    isGISCameraSync = true;
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Sync Service GIS Camera Api not working");
                                isGISCameraSync = true;
                                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                                stopService();
                            }
                        }
                        else{
                            Log.e(TAG,"Sync Service GIS Camera Api null");
                            isGISCameraSync = true;
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                    else {
                        isGISCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                        Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                        stopService();
                    }
                }
            } catch (IOException e) {
                isGISCameraSync = true;
                responseString = e.toString();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            } catch (Exception exception) {
                isGISCameraSync = true;
                exception.printStackTrace();
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        isGISCameraSync = true;
                        Log.e(TAG, "Logout Sync GIS Camera Off");

                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Log.e(TAG, "Sync Data Successfully");
                            stopSyncService();
                        }
                    }
                    else{
                        Log.e(TAG, "Sync Service GIS  Camera Api status fail / not working");
                        isGISCameraSync  = true;
                        if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                            stopService();
                        }
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "Sync Service GIS Camera Api not working");
                    isGISCameraSync = true;
                    Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
            else{
                Log.e(TAG,"Sync Service GIS Camera Api null");
                isGISCameraSync = true;
                Toast.makeText(context, "Sync Fail", Toast.LENGTH_SHORT).show();
                stopService();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- Stop Sync Service -----------------------------------------------------------------------------------------------------------------------------------------

    private void stopSyncService(){
        Toast.makeText(this, "Sync Data Successfully", Toast.LENGTH_SHORT).show();
        stopForeground(true);
        stopSelf();
        // Set Sync Successfully Notification
        getSyncSuccessfullyNotification();
    }

    private void stopService(){
        getSyncFailNotification();
        stopForeground(true);
        stopSelf();
    }

//-----------------------------------------------------------------------------------Network Register-----------------------------------------------------------------------------------------------

    // Register Network
    protected void registerNetworkBroadcast(){
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    // UnRegister Network
    protected void unregisterNetwork(){
        try{
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

}
