package com.surveybaba.Modules;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fileupload.AndroidMultiPartEntity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.surveybaba.ADAPTER.AdapterSurveyMarker;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.SurveyFormModel;
import com.surveybaba.model.TimeLineModel;
import com.surveybaba.model.TimeLineSModel;
import com.surveybaba.model.TrackingStatusData;
import com.volly.BaseApplication;
import com.volly.URL_Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TimeLineMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener,View.OnClickListener ,LocationAssistant.Listener{

    private static final String TAG = "TimeLineMapActivity";
    // Google Map
    private GoogleMap googleMap;
    // Activity
    private Activity mActivity;
    // Float Button
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabTimeLineCamera;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    private boolean isLocation = false;
    private static final float DEFAULT_GOOGLE_MAP_ZOOM = 20f;
    private TextView txtAccuracy;
    // Database
    private DataBaseHelper  dataBaseHelper;
    private BaseApplication baseApplication;
    // Progress Dialog Bar
    private ProgressDialog progressDialog;
    private RelativeLayout rlCurrentLocation;
    boolean isSync = false;
    long totalSize = 0;

    private static final int TimeLine_REQUESTCODE = 1001;
//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line_map);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Activity
        mActivity = this;
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Database
        dataBaseHelper = new DataBaseHelper(this);
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // init
        init();
        // init Database
        initDatabase();
        // Set on Click Listener
        setOnClickListener();
        // Support Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.timeLineMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

//------------------------------------------------------- init ---------------------------------------------------------------------------------------------------------------------------

    private void init(){
        // Fab Button
        fabMenu           = findViewById(R.id.fabMenu);
        fabTimeLineCamera = findViewById(R.id.fabTimeLineCamera);
        // Text View
        txtAccuracy = findViewById(R.id.txtAccuracy);
        // Relative Layout
        rlCurrentLocation = findViewById(R.id.rlCurrentLocation);
    }

//------------------------------------------------------- InitDatabase ------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(mActivity);
    }

//------------------------------------------------------- set On Click Listener ---------------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){
        // Fab Button
        fabTimeLineCamera.setOnClickListener(this);
        rlCurrentLocation.setOnClickListener(this);
    }

//------------------------------------------------------- on Map Ready ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(@NonNull GoogleMap Map) {
        // Google Map
        googleMap = Map;
        // Type of Map is Hybrid!
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // My Location Enable
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        // On Map Click Listener
        googleMap.setOnMapClickListener(this);
        // On Marker Click Listener
        googleMap.setOnMarkerClickListener(this);
        // On Marker Drag Listener
        googleMap.setOnMarkerDragListener(this);

        SystemPermission systemPermission = new SystemPermission(this);
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

        if(isDataNotSync()){
            Utility.SyncYourDataAlert(this);
        }

        readTimeLineData();
        readTimeLineSData();
    }

//------------------------------------------------------- on Map Click ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

//------------------------------------------------------- on Marker Click ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        try{
            if(marker.getTag() instanceof  String){
                Log.e(TAG, "Instance of String");
            }
            else{
                if(marker.getTag() instanceof TimeLineModel){
                    Log.e(TAG, "Instance of TimeLineModel");
                    onTimeLineMarkerClick(marker);
                }
                if(marker.getTag() instanceof TimeLineSModel){
                    Log.e(TAG, "Instance of TimeLineSModel");
                    onTimeLineSMarkerClick(marker);

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return false;
    }

//------------------------------------------------------- on Marker Drag Start ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

//------------------------------------------------------- on Marker Drag ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

//------------------------------------------------------- on Marker Drag End ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

    }


//------------------------------------------------------- Camera Menu ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Back Button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if(item.getItemId() == R.id.timeLineMapSync){
            if(SystemUtility.isInternetConnected(this)){
                SyncAllData();
            }
            else{
                Utility.getOKDialogBox(this, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
            }
        }

        return super.onOptionsItemSelected(item);
    }

//------------------------------------------------------- On Click ----------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rlCurrentLocation:
                if(mCurrentLocation != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),20f));
                }
                break;

            case R.id.fabTimeLineCamera:
                closeFabMenu();
                Intent intent = new Intent(mActivity,TimeLineCameraActivity.class);
                startActivityForResult(intent, TimeLine_REQUESTCODE);
                break;

        }
    }

//-------------------------------------------------------  Sync ----------------------------------------------------------------------------------------------------------------------

    private boolean isDataNotSync(){
        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
      //  ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

        // here when there is not data present in local database then logout directly!
     //   if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
            Log.e(TAG, "Sync Service No Data Found in Local DataBase");
            return false;
        }
        else{
            return true;
        }
    }

    private void SyncAllData(){
        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
        ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

        // here when there is not data present in local database then logout directly!
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
            Log.e(TAG, "Sync Service No Data Found in Local DataBase");
            Utility.getOKDialogBox(this, "Sync", "Data Already Sync", DialogInterface::dismiss);
        }
        else{
            if(SystemPermission.isInternetConnected(mActivity)){
                baseApplication.startSyncService();
            }
        }
    }

//    private void dbCameraSync(){
//
//        ArrayList<CameraModule> imageList1 = dataBaseHelper.getTimeLineImage();
//        if(imageList1.size() == 0){
//            Utility.getOKDialogBox(this, "Sync", "No Data Found", DialogInterface::dismiss);
//            Log.e(TAG, "Time Line No Data Found in Local DataBase");
//            return;
//        }
//        if(SystemPermission.isInternetConnected(this)) {
//            // Dialog Box
//            Utility.getOKCancelDialogBox(this, "Sync", "you want to sync all your Data?", dialog -> {
//                // Yes then
//                ArrayList<CameraModule> imageList = dataBaseHelper.getTimeLineImage();
//                if (imageList.size() == 0) {
//                    Utility.getOKDialogBox(this, "Sync", "No Data Found", DialogInterface::dismiss);
//                    Log.e(TAG, "TimeLine No Data Found in Local DataBase");
//                } else {
//                    // Data Found then!
//                    // Check for Internet Connection
//                    if (SystemPermission.isInternetConnected(this)) {
//                        isSync = true;
//                        progressDialog = new ProgressDialog(this);
//                        progressDialog.setCancelable(false);
//                        progressDialog.setMessage("TimeLine Sync...");
//                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        progressDialog.show();
//                        new TimeLineMapActivity.FileSyncToServe().execute();
//                    } else {
//                        isSync = false;
//                    }
//                }
//            });
//        }
//        else{
//            Utility.getOKDialogBox(this, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
//        }
//    }

//------------------------------------------------------- progressBar ----------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------- Progress Bar ---------------------------------------------------------------------------------------------------------------------------

//-------------------------------------------------------  Progress Bar ----------------------------------------------------------------------------------------------------------------------

    private void showProgressBar(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }
    private void dismissProgressbar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

//------------------------------------------------------- close Fab Menu ---------------------------------------------------------------------------------------------------------------------------

    private void closeFabMenu(){
        fabMenu.close(true);
    }

//------------------------------------------------------- Location ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            assistant.requestLocationPermission();
        });
        dialogBuilder.setCancelable(true);
        dialogBuilder.show();
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        assistant.requestLocationPermission();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this).setMessage(R.string.permissionPermanentlyDeclined).setPositiveButton(R.string.lbl_Ok, fromDialog).show();

    }

    @Override
    public void onNeedLocationSettingsChange() {
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.changeLocationSettings();
            }
        }).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onNewLocationAvailable(Location location) {

        if (location == null ){
            showProgressBar("Fetch Location...");
            isLocation = false;
            txtAccuracy.setVisibility(View.GONE);
        }
        else{
            mCurrentLocation = location;
            // Accuracy
            txtAccuracy.setText("Accuracy: " + mCurrentLocation.getAccuracy() + " mtr");
            txtAccuracy.setVisibility(View.VISIBLE);
            if(!isLocation){
                isLocation = true;
                baseApplication.startMyService();
                dismissProgressbar();
                // Current LatLon
                LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
            }
        }

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

//------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        assistant.start();
    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

//------------------------------------------------------- File Sync To Server ---------------------------------------------------------------------------------------------------------------------------
//
//    private class FileSyncToServe extends AsyncTask<Void, Integer, String> {
//
//        public FileSyncToServe() {}
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            return uploadFile();
//        }
//        @SuppressWarnings("deprecation")
//        private String uploadFile(){
//            String responseString = null;
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(URL_Utility.WS_UPLOAD_TIMELINE_IMAGE);
//            try {
//                ArrayList<CameraModule> imageList = dataBaseHelper.getTimeLineImage();
//
//                for(int i=0; i<imageList.size(); i++){
//                    String id = imageList.get(i).getId();
//                    String user_id   = imageList.get(i).getUserId();
//                    String latitude  = imageList.get(i).getImageLat();
//                    String longitude = imageList.get(i).getImageLon();
//                    String version   = imageList.get(i).getVersion();
//                    String datetime  = imageList.get(i).getImageDateTime();
//                    String imagePath = imageList.get(i).getImagePath();
//                    String desc      = imageList.get(i).getImageDesc();
//                    // Normal data Upload!
//                    File sourceFile = new File(imagePath);
//                    String data = "";
//                    JSONObject params = new JSONObject();
//                    try {
//                        params.put(URL_Utility.PARAM_LOGIN_USER_ID, user_id);
//                        params.put(URL_Utility.ACTION_APP_VERSION, version);
//                        params.put(URL_Utility.PARAM_LATITUDE, latitude);
//                        params.put(URL_Utility.PARAM_LONGITUDE, longitude);
//                        params.put(URL_Utility.PARAM_DATETIME, datetime);
//                        params.put(URL_Utility.PARAM_IMAGE_DESC, desc);
//                        // Login Token
//                        params.put(URL_Utility.PARAM_UNIQUE_NUMBER,Utility.getToken());
//                    }
//                    catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    // Encrypt Data!
//                    data = AESCrypt.encrypt(params.toString());
//
//                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(num -> publishProgress((int) ((num / (float) totalSize) * 100)));
//                    entity.addPart(URL_Utility.PARAM_IMAGE_DATA, new FileBody(sourceFile));
//                    entity.addPart("data", new StringBody(data));
//
//                    totalSize = entity.getContentLength();
//                    httppost.setEntity(entity);
//                    HttpResponse response = httpclient.execute(httppost);
//                    HttpEntity r_entity = response.getEntity();
//                    int statusCode = response.getStatusLine().getStatusCode();
//
//                    if (statusCode == 200) {
//                        responseString = EntityUtils.toString(r_entity);
//                        Log.e("Response",AESCrypt.decryptResponse(responseString));
//                        String res = AESCrypt.decryptResponse(responseString);
//                        if(!res.equals("")) {
//                            try {
//                                JSONObject mLoginObj = new JSONObject(res);
//                                String status = mLoginObj.optString("status");
//                                if (status.equalsIgnoreCase("Success")){
//                                    // Delete Data one by one!
//                                    if(imageList.size() > 0){
//                                        imageList.remove(i);
//                                        dataBaseHelper.deleteTimeLineImage(id);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    else {
//                        responseString = "Error occurred! Http Status Code: " + statusCode;
//                    }
//                }
//            } catch (IOException e) {
//                responseString = e.toString();
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return responseString;
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
//            String response = AESCrypt.decryptResponse(result);
//            if(!response.equals("")) {
//                try {
//                    JSONObject mLoginObj = new JSONObject(response);
//                    String status = mLoginObj.optString("status");
//                    if (status.equalsIgnoreCase("Success")) {
//                        syncSuccessfullyMessage();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            super.onPostExecute(result);
//        }
//    }
//
//    private  boolean isCameraDataNotSync(){
//        // Camera  Data!
//        ArrayList<CameraModule> cameraImageList = dataBaseHelper.getTimeLineImage();
//        return cameraImageList.size() > 0;
//    }

//    private void syncSuccessfullyMessage() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
//        alertDialog.setMessage("TimeLine Sync Successfully");
//        alertDialog.setPositiveButton(getResources().getString(R.string.lbl_Ok), (dialog, which) -> {
//            googleMap.clear();
//            readTimeLineData();
//            readTimeLineSData();
//            dialog.dismiss();
//        });
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }

//------------------------------------------------------- onActivityResult ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Form Successfully Submit or not!
        if(requestCode == TimeLine_REQUESTCODE && resultCode == RESULT_OK) {
            Log.e(TAG, "Save TimeLine Successfully");
            googleMap.clear();
            readTimeLineData();
            readTimeLineSData();
        }
    }

//------------------------------------------------------- readTimeLineData ---------------------------------------------------------------------------------------------------------------------------

    private void readTimeLineData(){
        ArrayList<CameraModule> timeLineDataLocalList = dataBaseHelper.getTimeLineImageLocal();
        if(timeLineDataLocalList.size() > 0){

            showProgressBar("TimeLine Loading...");
            for(int i=0; i<timeLineDataLocalList.size(); i++){

                CameraModule timeline = timeLineDataLocalList.get(i);
                if(!Utility.isEmptyString(timeline.getImageLat()) && !Utility.isEmptyString(timeline.getImageLon())){
                    TimeLineModel timeLineModel = new TimeLineModel();
                    timeLineModel.setImagePath(timeline.getImagePath());
                    timeLineModel.setDescription(timeline.getImageDesc());
                    timeLineModel.setDatetime(timeline.getImageDateTime());
                    LatLng timeLineLatLng = new LatLng(Double.parseDouble(timeline.getImageLat()),Double.parseDouble(timeline.getImageLon()));
                    MarkerOptions timeLineMarkerOptions = new MarkerOptions().position(timeLineLatLng).draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker timeLineMarker = googleMap.addMarker(timeLineMarkerOptions);
                    assert timeLineMarker != null;
                    timeLineMarker.setTag(timeLineModel);
                }
                else{
                    Log.e(TAG, "Time Line Lat Lon Empty");
                }
            }
            dismissProgressbar();
        }
    }
    // Read Form Server!
    private void readTimeLineSData(){
        ArrayList<CameraModule> timeLineDataSLocalList = dataBaseHelper.getTimeLineSImageLocal();
        if(timeLineDataSLocalList.size() > 0){

            showProgressBar("TimeLine Loading...");
            for(int i=0; i<timeLineDataSLocalList.size(); i++){

                CameraModule timeline = timeLineDataSLocalList.get(i);
                if(!Utility.isEmptyString(timeline.getImageLat()) && !Utility.isEmptyString(timeline.getImageLon())){
                    TimeLineSModel timeLineSModel = new TimeLineSModel();
                    timeLineSModel.setImagePath(timeline.getImagePath());
                    timeLineSModel.setDescription(timeline.getImageDesc());
                    timeLineSModel.setDatetime(timeline.getImageDateTime());
                    LatLng timeLineLatLng = new LatLng(Double.parseDouble(timeline.getImageLat()),Double.parseDouble(timeline.getImageLon()));
                    MarkerOptions timeLineMarkerOptions = new MarkerOptions().position(timeLineLatLng).draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker timeLineMarker = googleMap.addMarker(timeLineMarkerOptions);
                    assert timeLineMarker != null;
                    timeLineMarker.setTag(timeLineSModel);
                }
                else{
                    Log.e(TAG, "Time Line Lat Lon Empty");
                }
            }
            dismissProgressbar();

        }
    }
//------------------------------------------------------- onTimeLineMarkerClick ---------------------------------------------------------------------------------------------------------------------------

    private void onTimeLineMarkerClick(Marker marker){

        try{
            TimeLineModel timeLineModel = (TimeLineModel) marker.getTag();
            assert timeLineModel != null;
            // Dialog Box
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.timeline_custom_marker_view);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Close Button
            Button timeLineCloseButton = dialog.findViewById(R.id.timeLineCloseButton);
            timeLineCloseButton.setOnClickListener(v -> dialog.dismiss());
            // ImageView
            ImageView timelineMarkerImage      = dialog.findViewById(R.id.timelineMarkerImage);
            Glide.with(mActivity).load(timeLineModel.getImagePath()).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            //Glide.with(this).load(timeLineModel.getImagePath()).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            // Description
            TextView timeLineMarkerDescription = dialog.findViewById(R.id.timeLineMarkerDescription);
            timeLineMarkerDescription.setText(timeLineModel.getDescription());
            // Date Time
            TextView timeLineMarkerDateTime = dialog.findViewById(R.id.timeLineMarkerDateTime);
            timeLineMarkerDateTime.setText(timeLineModel.getDatetime());

            timelineMarkerImage.setOnClickListener(view -> {
                Dialog dialog1 = new Dialog(mActivity);
                dialog1.setContentView(R.layout.image_zoom_view_layout);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView imageView = dialog1.findViewById(R.id.dialogbox_image);
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
                // Image Load!
                Glide.with(mActivity).load(timeLineModel.getImagePath()).error(R.drawable.icon_no_image).into(imageView);
               // Glide.with(mActivity).load(timeLineModel.getImagePath()).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(imageView);
                photoViewAttacher.update();
                dialog1.show();
            });
            // Dialog Box Show
            dialog.show();
        }
        catch (Exception e){
            Log.e("SurveyActivity", e.getMessage());
        }
    }
    // Server Data
    private void onTimeLineSMarkerClick(Marker marker){

        try{
            TimeLineSModel timeLineSModel = (TimeLineSModel) marker.getTag();
            assert timeLineSModel != null;
            // Dialog Box
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.timeline_custom_marker_view);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // Close Button
            Button timeLineCloseButton = dialog.findViewById(R.id.timeLineCloseButton);
            timeLineCloseButton.setOnClickListener(v -> dialog.dismiss());
            // ImageView
            ImageView timelineMarkerImage      = dialog.findViewById(R.id.timelineMarkerImage);
            Glide.with(mActivity).load(Utility.decodeBase64Image(timeLineSModel.getImagePath())).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            //Glide.with(this).load(timeLineSModel.getImagePath()).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            // Description
            TextView timeLineMarkerDescription = dialog.findViewById(R.id.timeLineMarkerDescription);
            timeLineMarkerDescription.setText(timeLineSModel.getDescription());
            // Date Time
            TextView timeLineMarkerDateTime = dialog.findViewById(R.id.timeLineMarkerDateTime);
            timeLineMarkerDateTime.setText(timeLineSModel.getDatetime());

            timelineMarkerImage.setOnClickListener(view -> {
                Dialog dialog1 = new Dialog(mActivity);
                dialog1.setContentView(R.layout.image_zoom_view_layout);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView imageView = dialog1.findViewById(R.id.dialogbox_image);
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
                // Image Load!
                //Glide.with(mActivity).load(Utility.decodeBase64Image(timeLineSModel.getImagePath())).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(imageView);
                Glide.with(mActivity).load(Utility.decodeBase64Image(timeLineSModel.getImagePath())).error(R.drawable.icon_no_image).into(imageView);
                photoViewAttacher.update();
                dialog1.show();
            });

            // Dialog Box Show
            dialog.show();
        }
        catch (Exception e){
            Log.e("SurveyActivity", e.getMessage());
        }

    }

}