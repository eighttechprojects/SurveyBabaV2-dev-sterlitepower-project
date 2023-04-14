package com.surveybaba.Modules;

import static com.surveybaba.Database.DataBaseHelper.DELETE_TABLE_WALKING_TRACKING;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
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
import com.surveybaba.model.TrackingStatusData;
import com.surveybaba.model.WalkingModule;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GPSTrackingActivity extends AppCompatActivity implements WSResponseInterface,OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener,View.OnClickListener, LocationAssistant.Listener{
    // TAG
    private static final String TAG = "GPSTrackingActivity";
    // Google Map
    private GoogleMap googleMap;
    // Zoom Option Map!
    private GoogleMap zoomGoogleMap;
    private Activity mActivity;
    private BaseApplication baseApplication;
    private DataBaseHelper dataBaseHelper;
    // Float Button
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabTrackingByWalking, fabTrackingByPoint,fabTrackingUndo,fabTrackingRedo,trackingSaveButton,trackingCancelButton;
    private Button trackingStopButton;
    private LinearLayout llButton,llUndoRedolayout;
    private FrameLayout zoomMapLayout,zoomMapMarkerLayout;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    private boolean isLocation = false;
    private static double MIN_DISTANCE_UPDATE = 5d;
    private static final float DEFAULT_GOOGLE_MAP_ZOOM = 20f;
    // Progress Dialog Bar
    private ProgressDialog progressDialog;
    private TextView txtAccuracy;
    // Sync
    private boolean isSyncOn = false;
    private ArrayList<GpsTrackingModule> syncList = new ArrayList<>();
    private GpsTrackingModule gpsSyncTrackingModule;
    //----------------------------------------------------------------------------------- Tracking By Walking Option Variable -----------------------------------------------------------------------------------------------
    private boolean isWalkingTrackingOn = false;
    private boolean isWalkingStop = false;
    private Polyline polylineWalking;
    private Marker markerWalking;
    private ArrayList<LatLng> polylineWalkingLatLngList = new ArrayList<>();

    //----------------------------------------------------------------------------------- Tracking By Tap Option Variable-----------------------------------------------------------------------------------------------
    private boolean isTapTrackingOn =  false;
    private int tapMarkerCount = 0;
    private Polyline polylineTap = null;
    ArrayList<LatLng> polylineTapRedoLatLngList = new ArrayList<>();
    private Marker markerTap;
    private ArrayList<LatLng> tapLatLngList = new ArrayList<>();
    private ArrayList<Marker> tapMarkerList = new ArrayList<>();
    private RelativeLayout rlCurrentLocation;

//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracking);

        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Activity
        mActivity = this;
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // init
        init();
        // init Database
        initDatabase();
        // Set on Click Listener
        setOnClickListener();
        // Zoom Support Map Layout!
        SupportMapFragment Zoom_map_Fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.zoom_map);
        assert Zoom_map_Fragment != null;
        Zoom_map_Fragment.getMapAsync(onZoomMapReady());
        // Support Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gpsTrackingMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        Utility.saveData(this,Utility.WALKING,false);

        baseApplication.startMyService();

        if(isDataNotSync()){
            Utility.SyncYourDataAlert(this);
        }
        
    }

//------------------------------------------------------- init ---------------------------------------------------------------------------------------------------------------------------

    private void init(){
        // Fab Button
        fabMenu              = findViewById(R.id.fabMenu);
        fabTrackingByWalking = findViewById(R.id.fabTrackingByWalking);
        fabTrackingByPoint   = findViewById(R.id.fabTrackingByTap);
        fabTrackingUndo      = findViewById(R.id.fabTrackingUndo);
        fabTrackingRedo      = findViewById(R.id.fabTrackingRedo);
        // Text View
        txtAccuracy = findViewById(R.id.txtAccuracy);
        // Button
        trackingSaveButton = findViewById(R.id.trackingSaveButton);
        trackingStopButton = findViewById(R.id.trackingStopButton);
        trackingCancelButton = findViewById(R.id.trackingCancelButton);
        // Linear Layout
        llButton = findViewById(R.id.llButton);
        llUndoRedolayout = findViewById(R.id.llUndoRedolayout);
        // Zoom Layout
        zoomMapLayout = findViewById(R.id.zoom_map_layout);
        zoomMapMarkerLayout = findViewById(R.id.zoom_map_marker_layout);

        rlCurrentLocation = findViewById(R.id.rlCurrentLocation);

    }

//------------------------------------------------------- InitDatabase ------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(GPSTrackingActivity.this);
    }

//------------------------------------------------------- set On Click Listener ---------------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){
        // Fab Button
        fabTrackingByWalking.setOnClickListener(this);
        fabTrackingByPoint.setOnClickListener(this);
        fabTrackingUndo.setOnClickListener(this);
        fabTrackingRedo.setOnClickListener(this);
        // Button
        trackingSaveButton.setOnClickListener(this);
        trackingStopButton.setOnClickListener(this);
        trackingCancelButton.setOnClickListener(this);
        rlCurrentLocation.setOnClickListener(this);

    }

//------------------------------------------ On Zoom Map Ready  -----------------------------------------------------------------------------------------------------------------

    public OnMapReadyCallback onZoomMapReady(){
        return googleMap -> {
            zoomGoogleMap = googleMap;
            zoomGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        };
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

        readLocalData();

    }

//------------------------------------------------------- on Map Click ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        if(isTapTrackingOn){
            trackingByTap(latLng);
        }

    }

//------------------------------------------------------- on Marker Click ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

//------------------------------------------------------- on Marker Drag Start ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        //------------------------------------------------------- Tap option On ---------------------------------------------------------------------------------------------------------------------------
        if(isTapTrackingOn){
            onMarkerPolyLineTapDrag(marker);
        }
    }

//------------------------------------------------------- on Marker Drag ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        //------------------------------------------------------- Tap option On ---------------------------------------------------------------------------------------------------------------------------
        if(isTapTrackingOn){
            onMarkerPolyLineTapDrag(marker);
        }
    }

//------------------------------------------------------- on Marker Drag End ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        //------------------------------------------------------- Tap option On ---------------------------------------------------------------------------------------------------------------------------
        if(isTapTrackingOn){
            zoomCameraOff();
        }
    }


//------------------------------------------------------- On Create Options Menu ------------------------------------------------------------------------------------------------------------------

    // Menu Option in google map
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gpstracking_menu, menu);
        return true;
    }

//------------------------------------------------------- On OptionsItem Selected ------------------------------------------------------------------------------------------------------

    // Edit button on menu!
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        // Sync button
        if (item.getItemId() == R.id.GPSTrackingSync) {
            if(SystemUtility.isInternetConnected(this)){
                SyncAllData();
            }
            else{
                Utility.getOKDialogBox(this, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
            }
        }
        // Show All Record Button
        if(item.getItemId() == R.id.GPSTrackingShowAllData){
            if(SystemUtility.isInternetConnected(this)){
                ShowAllRecord();
            }
            else{
                Utility.getOKDialogBox(this, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
            }
        }

        return false;
    }

//------------------------------------------------------- on Click ---------------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rlCurrentLocation:
                moveCameraToCurrentLocation();
                break;

            case R.id.fabTrackingByWalking:
                closeFabMenu();
                if(isTapTrackingOn){
                    Utility.getYesNoDialogBox(this, "Tap Alert", "Do you want to cancel it?", dialog -> trackingByTapClear());
                }
                else{
                    // DialogBox
                    Utility.getYesNoDialogBox(this, "Tracking", "Turn on Tracking by Walking Option?", dialog -> {
                        // Set Title
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Walking Tracking");
                        // Set boolean
                        isWalkingTrackingOn = true;
                        isTapTrackingOn = false;
                        isWalkingStop = false;
                        // Show button Layout
                        showButtonLayout(true);
                        // Show Save Button
                        showSaveButton(true);
                        // Show Stop Button
                        showStopButton(true);
                        // Show Cancel Button
                        showCancelButton(true);
                        // Show Undo Redo Button
                        showUndoRedoButton(false);
                        Utility.saveData(this,Utility.WALKING,false);
                        Log.e(TAG,"Tracking By Walking On");
                    }, dialog -> {
                        // Clear Walking Data
                        trackingByWalkingClear();
                        Log.e(TAG,"Tracking By Walking OFF");
                    });
                }
                break;

            case R.id.fabTrackingByTap:
                closeFabMenu();
                if(isWalkingTrackingOn){
                    Utility.getYesNoDialogBox(this, "Walking Alert", "Do you want to cancel it?", dialog -> trackingByWalkingClear());
                }
                else{
                    // DialogBox
                    Utility.getYesNoDialogBox(this, "Tracking", "Turn on Tracking by Tap Option?", dialog -> {
                        // Set Title
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Tap Tracking");
                        // Set boolean
                        isWalkingTrackingOn = false;
                        isTapTrackingOn = true;
                        // Show button Layout
                        showButtonLayout(true);
                        // Show Save button
                        showSaveButton(true);
                        // Show Stop button
                        showStopButton(false);
                        // Show Cancel button
                        showCancelButton(true);
                        // Show Undo Redo Button
                        showUndoRedoButton(true);
                        Utility.saveData(this,Utility.WALKING,false);
                    }, dialog -> {
                        // Clear Tap Data
                        trackingByTapClear();
                    });
                }

                break;

            case R.id.trackingSaveButton:
                if(isWalkingTrackingOn){
                    trackingByWalkingSave();
                }
                if(isTapTrackingOn){
                    trackingByTapSave();
                }
                break;

            case R.id.trackingStopButton:
                trackingStopButton();
                break;

            case R.id.trackingCancelButton:
                TapCancelButton();
                break;

            case R.id.fabTrackingUndo:
                if(isTapTrackingOn){
                    polyLineTapUndoOption();
                }
                break;

            case R.id.fabTrackingRedo:
                if(isTapTrackingOn){
                    polyLineTapRedoOption();
                }
                break;
        }
    }

//------------------------------------------------------- Tracking By Walking ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByWalking(LatLng currentLatLng){
        boolean hasEnoughDistance;
        if(isWalkingTrackingOn && !isWalkingStop) {
            if (polylineWalkingLatLngList.size() > 0) {
                // distance in meter!
                double distance = SphericalUtil.computeDistanceBetween(currentLatLng, polylineWalkingLatLngList.get(polylineWalkingLatLngList.size() - 1));
                hasEnoughDistance = (distance > MIN_DISTANCE_UPDATE); // -> min distance update set at -> 10 meter
            }
            else{
                hasEnoughDistance = true;
            }
            if (hasEnoughDistance) {
                    //-------------------------------
                    // Add LatLon to List
                    polylineWalkingLatLngList.add(currentLatLng);
                    // Add marker
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("Current Location");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markerOptions.position(currentLatLng);
                    if(markerWalking != null){
                        markerWalking.remove();
                    }
                    markerWalking = googleMap.addMarker(markerOptions);
                    assert markerWalking != null;
                    markerWalking.setPosition(currentLatLng);
                    // Add polyline
                    if (polylineWalking != null) {
                        polylineWalking.setPoints(polylineWalkingLatLngList);
                    }
                    else {
                        String default_color = Utility.COLOR_CODE.PINK;
                        PolylineOptions rectOptions = new PolylineOptions()
                                .clickable(false)
                                .color(Color.parseColor(default_color))
                                .width(SystemUtility.getLineSize(mActivity))
                                .startCap(new RoundCap())
                                .endCap(new RoundCap())
                                .jointType(JointType.ROUND)
                                .addAll(polylineWalkingLatLngList)
                                .geodesic(true);
                        polylineWalking = googleMap.addPolyline(rectOptions);
                    }
            }
        }
    }

//------------------------------------------------------- Tracking Walking Save ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByWalkingSave(){

        if(polylineWalkingLatLngList.size() == 0){
            Utility.getOKDialogBox(this, "Alert", "No Walking Data Found", DialogInterface::dismiss);
            return;
        }
        if(isWalkingStop){
            Utility.getYesNoDialogBox(this, "Save", "Are you sure u want to save data?", dialog -> {
                // Internet Connected
                if(SystemUtility.isInternetConnected(GPSTrackingActivity.this)){
                    showProgressBar("Uploading..");
                    SaveToServe(Utility.TYPE_WALK,polylineWalkingLatLngList);
                }
                // Internet Not Connected
                else{
                    SaveToLocalDatabase(Utility.TYPE_WALK,polylineWalkingLatLngList);
                    trackingByWalkingClear();
                }
            });
        }
        else{
            Utility.getOKDialogBox(GPSTrackingActivity.this, "Alert", "You have to first stop tracking Option?. Press Stop Button", DialogInterface::dismiss);
        }
    }

//------------------------------------------------------- Tracking Walking Clear ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByWalkingClear(){
        if(markerWalking != null){
            markerWalking.remove();
        }
        if(polylineWalking != null){
            polylineWalking.remove();
        }
        polylineWalkingLatLngList.clear();
        isWalkingTrackingOn = false;
        isTapTrackingOn = false;
        isWalkingStop = false;
        showUndoRedoButton(false);
        showButtonLayout(false);
        showSaveButton(false);
        showStopButton(false);
        showCancelButton(false);
        // Set Title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tracking");
        Utility.saveData(this,Utility.WALKING,false);
    }

    private void readTrackingByWalkingData(){
            ArrayList<WalkingModule> list = dataBaseHelper.getWalkingTracking();
            if(list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String latitude = list.get(i).getLat();
                    String longitude = list.get(i).getLon();
                    Log.e(TAG, "LocalDatabase: " + " Lat: " + latitude + " Lon: " + longitude);
                    LatLng currentLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                    trackingByWalking(currentLatLng);
                }
                // Delete / Clear Walking Table
                dataBaseHelper.open();
                dataBaseHelper.executeQuery(DELETE_TABLE_WALKING_TRACKING);
                dataBaseHelper.close();
            }
    }

//------------------------------------------------------- Tracking By Tap ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByTap(LatLng currentLatLng){
        // number of marker
        tapMarkerCount++;
        // Add marker
        Marker markerTap = googleMap.addMarker(new MarkerOptions()
                .icon(SystemUtility.getRoundedMarkerIconGreen(mActivity))
                .position(currentLatLng)
                .draggable(true)
                .anchor(0.5f,0.5f));
        tapMarkerList.add(markerTap);
        tapLatLngList.add(currentLatLng);
        // Add PolyLine
        if(polylineTap != null){
            polylineTap.remove();
        }
        polylineTap =  googleMap.addPolyline( new PolylineOptions()
                .color(Color.RED)
                .width(7f)
                .clickable(false)
                .addAll(tapLatLngList));
    }

    private void onMarkerPolyLineTapDrag(Marker marker){
        // Camera On
        zoomCameraOn(marker);
        tapLatLngList.iterator();

        // Check Which Marker is Click
        for(int index=0; index<tapMarkerList.size(); index++){
            if(tapMarkerList.get(index).equals(marker)){
                if(polylineTap != null){
                    polylineTap.remove();
                    tapLatLngList.set(index,marker.getPosition());
                }
            }
        }

        if(polylineTap != null){
            polylineTap.remove();
        }
        polylineTap =  googleMap.addPolyline(new PolylineOptions()
                .color(Color.RED)
                .width(7f)
                .clickable(false)
                .addAll(tapLatLngList));
    }

//------------------------------------------ Tap Undo -----------------------------------------------------------------------------------------------------------------

    private void polyLineTapUndoOption(){
        int i = tapMarkerCount-1;
        tapMarkerCount = i;
        if(i>0) {
            polylineTapRedoLatLngList.add(tapLatLngList.get(i));
            tapMarkerList.get(i).remove();   // remove marker.
            tapMarkerList.remove(i);        // remove marker.
            // LatLon Remove
            tapLatLngList.remove(i);
            // Add Polyline on google map!

            if(polylineTap != null){
                polylineTap.remove();
            }
            polylineTap =  googleMap.addPolyline(new PolylineOptions()
                    .color(Color.RED)
                    .width(7f)
                    .clickable(false)
                    .addAll(tapLatLngList));
        }
        else{
            tapMarkerCount = 1;
        }
    }

//------------------------------------------ Tap Redo -----------------------------------------------------------------------------------------------------------------

    private void polyLineTapRedoOption(){
        if(polylineTapRedoLatLngList.size() > 0){
            int count  = polylineTapRedoLatLngList.size() - 1;
            // Add latLon
            tapLatLngList.add(polylineTapRedoLatLngList.get(count));
            // Add marker
            Marker markerTap = googleMap.addMarker(new MarkerOptions()
                .icon(SystemUtility.getRoundedMarkerIconGreen(mActivity))
                .position(polylineTapRedoLatLngList.get(count))
                .draggable(true)
                .anchor(0.5f,0.5f));
            // Add Marker
            tapMarkerList.add(markerTap);
            if(polylineTap != null){
                polylineTap.remove();
            }
            polylineTap =  googleMap.addPolyline(new PolylineOptions()
                    .color(Color.RED)
                    .width(7f)
                    .clickable(false)
                    .addAll(tapLatLngList));
            // Increase Counter
            tapMarkerCount++;
            polylineTapRedoLatLngList.remove(count);
        }
    }

//------------------------------------------ Tap Cancel Button -----------------------------------------------------------------------------------------------------------------

    private void TapCancelButton(){

        if(isTapTrackingOn){
            Utility.getYesNoDialogBox(this, "Clear", "Do you want to clear Tap Data?", dialog -> {
                trackingByTapClear();
                dialog.dismiss();
            });
        }

        if(isWalkingTrackingOn){
            Utility.getYesNoDialogBox(this, "Clear", "Do you want to clear Walking Data?", dialog -> {
                trackingByWalkingClear();
                dialog.dismiss();
            });
        }

    }

//------------------------------------------------------- Tracking Tap Save ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByTapSave(){

        if(tapLatLngList.size() == 0){
            Utility.getOKDialogBox(this, "Alert", "No Tap Data Found", DialogInterface::dismiss);
            return;
        }
        Utility.getYesNoDialogBox(this, "Save", "Are you sure u want to save this Data?", dialog -> {
            // Internet Connected
            if(SystemUtility.isInternetConnected(GPSTrackingActivity.this)){
                showProgressBar("Uploading..");
                SaveToServe(Utility.TYPE_TAP,tapLatLngList);
            }
            // Internet Not Connected
            else{
               SaveToLocalDatabase(Utility.TYPE_TAP, tapLatLngList);
               trackingByTapClear();
            }
        });
    }

//------------------------------------------------------- Tracking Tap Clear ---------------------------------------------------------------------------------------------------------------------------

    private void trackingByTapClear(){
        if(markerTap != null){
            markerTap.remove();
        }
        for(Marker marker : tapMarkerList){
            marker.remove();
        }
        if(polylineTap != null){
            polylineTap.remove();
        }
        tapMarkerCount = 0;
        tapMarkerList.clear();
        tapLatLngList.clear();
        polylineTapRedoLatLngList.clear();
        isTapTrackingOn =false;
        isWalkingTrackingOn = false;
        showUndoRedoButton(false);
        showButtonLayout(false);
        showCancelButton(false);
        showStopButton(false);
        showSaveButton(false);
        // Set Title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tracking");
        Utility.saveData(this,Utility.WALKING,false);
    }

//------------------------------------------------------- Tracking Stop ---------------------------------------------------------------------------------------------------------------------------

    private void trackingStopButton(){
        // Walking
        if(isWalkingTrackingOn){
            Utility.getYesNoDialogBox(this, "Stop", "Are you sure u want to Stop Tracking?", dialog -> {
                // when user stop tracking then
                isWalkingStop = true;
                Utility.saveData(GPSTrackingActivity.this,Utility.WALKING,false);
                Toast.makeText(GPSTrackingActivity.this, "Tracking Stop", Toast.LENGTH_SHORT).show();
            });
        }

    }

//------------------------------------------------------- Save Data To Local DataBase ---------------------------------------------------------------------------------------------------------------------------

    private void SaveToLocalDatabase(String types, ArrayList<LatLng> latLongs){
        String user_id  = Utility.getSavedData(GPSTrackingActivity.this, Utility.LOGGED_USERID);
        String latLong  = new Gson().toJson(latLongs);
        String datetime = Utility.getRecordDate();
        String version  = URL_Utility.APP_VERSION;
        String token    = String.valueOf(Utility.getToken());
        dataBaseHelper.insertGPSTracking(user_id, types,latLong,datetime,version,token);
        trackingByTapClear();
        Utility.getOKDialogBox(GPSTrackingActivity.this, "Save", "Data Save Successfully", DialogInterface::dismiss);
        readLocalData();
        Log.e(TAG, "GPS Tracking By "+types+" Save to Local Database");
    }

//------------------------------------------------------- Save Data To Server ---------------------------------------------------------------------------------------------------------------------------

    private void SaveToServe(String types, ArrayList<LatLng> list){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID,  Utility.getSavedData(mActivity, Utility.LOGGED_USERID));
            jsonObject.put(URL_Utility.PARAM_DATETIME, Utility.getDateTime());
            jsonObject.put(URL_Utility.PARAM_VERSION,  URL_Utility.APP_VERSION);
            jsonObject.put(URL_Utility.PARAM_WT_TOKEN,    String.valueOf(Utility.getToken()));
            jsonObject.put(URL_Utility.PARAM_TYPE,     types);
            jsonObject.put(URL_Utility.PARAM_UNIQUE_NUMBER, 	String.valueOf(Utility.getToken()));
            // Login Token
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(GPSTrackingActivity.this,Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.PARAM_LATLON,   new Gson().toJson(list));
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(GPSTrackingActivity.this, responseCode, URL_Utility.WS_GPS_TRACKING_UPLOAD, params, false, false);
    }

//------------------------------------------------------- Read Local Database ---------------------------------------------------------------------------------------------------------------------------

    private void readLocalData(){
        ArrayList<GpsTrackingModule> list = dataBaseHelper.getGpsTracking();
        if(list.size() != 0){
            for(int i=0; i<list.size(); i++){
                GpsTrackingModule gpsTrackingModule = list.get(i);
                ArrayList<LatLng> latLongList = Utility.convertStringToListOfPoints(gpsTrackingModule.getLatLong());
                if(latLongList.size() != 0) {
                    // Type is Walking then
                    if (gpsTrackingModule.getType().equals(Utility.TYPE_WALK)) {
                        LatLng latLng = new LatLng(latLongList.get(latLongList.size()-1).latitude, latLongList.get(latLongList.size()-1).longitude);
                        Marker marker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(latLng).draggable(false).anchor(0.5f, 0.5f));
                        assert marker != null;
                        marker.setTitle("Tap Point");
                        marker.setTag(gpsTrackingModule);
                    }
                    else if(gpsTrackingModule.getType().equals(Utility.TYPE_TAP)){
                        for (int j = 0; j < latLongList.size(); j++) {
                            LatLng latLng = new LatLng(latLongList.get(j).latitude, latLongList.get(j).longitude);
                            Marker marker = googleMap.addMarker(new MarkerOptions().icon(SystemUtility.getRoundedMarkerIconGreen(mActivity)).position(latLng)
                                    .draggable(false).anchor(0.5f, 0.5f));
                            assert marker != null;
                            marker.setTitle("Tap Point");
                            marker.setTag(gpsTrackingModule);
                        }
                    }
                    Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                            .color(Color.BLUE)
                            .width(7f)
                            .clickable(false)
                            .addAll(latLongList));
                }
            }
        }
    }

//------------------------------------------------------- Sync Data To Server ---------------------------------------------------------------------------------------------------------------------------

    private void SyncToServe(GpsTrackingModule gpsTrackingModule){
        Map<String, String> params = new HashMap<>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID,  gpsTrackingModule.getUser_id());
            jsonObject.put(URL_Utility.PARAM_DATETIME, gpsTrackingModule.getDatetime());
            jsonObject.put(URL_Utility.PARAM_VERSION,  gpsTrackingModule.getVersion());
            jsonObject.put(URL_Utility.PARAM_WT_TOKEN,    gpsTrackingModule.getToken());
            jsonObject.put(URL_Utility.PARAM_TYPE,     gpsTrackingModule.getType());
            // Login Token
            jsonObject.put(URL_Utility.PARAM_UNIQUE_NUMBER, 	gpsTrackingModule.getUnique_number());
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(GPSTrackingActivity.this,Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.PARAM_LATLON,   gpsTrackingModule.getLatLong());
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(GPSTrackingActivity.this, responseCode, URL_Utility.WS_GPS_TRACKING_UPLOAD, params, false, false);
    }

//------------------------------------------------------- Tracking Sync ---------------------------------------------------------------------------------------------------------------------------

    private boolean isDataNotSync(){
        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
     //   ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

        // here when there is not data present in local database then logout directly!
            if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0  && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
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
            Utility.getOKDialogBox(this, "Sync", "Data Already Sync", dialog -> {
                dialog.dismiss();
            });
        }
        else{
            if(SystemPermission.isInternetConnected(mActivity)){
                baseApplication.startSyncService();
            }
        }
    }

//------------------------------------------------------- Tracking Show All Record ---------------------------------------------------------------------------------------------------------------------------

    private void ShowAllRecord(){
        googleMap.clear();
        readLocalData();
        showProgressBar("Loading Record...");
        Map<String, String> params = new HashMap<String, String>();
        String data = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(URL_Utility.PARAM_USED_ID,  Utility.getSavedData(mActivity, Utility.LOGGED_USERID));
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_SHOW_RECORDS;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_GPS_TRACKING_SHOW_RECORDS, params, false, false);
    }

//------------------------------------------------------- read Show All Record ---------------------------------------------------------------------------------------------------------------------------

    private void readShowAllRecord(String type, ArrayList<LatLng> latLongList){
        for(int i=0; i<latLongList.size(); i++){
            // Type is Walking then
            if (type.equals(Utility.TYPE_WALK)) {
                LatLng latLng = new LatLng(latLongList.get(latLongList.size()-1).latitude, latLongList.get(latLongList.size() - 1).longitude);
                Marker marker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(latLng).draggable(false).anchor(0.5f, 0.5f));
                assert marker != null;
                marker.setTitle("Walking");
            }
            else if(type.equals(Utility.TYPE_TAP)){
                for (int j = 0; j < latLongList.size(); j++) {
                    LatLng latLng = new LatLng(latLongList.get(j).latitude, latLongList.get(j).longitude);
                    Marker marker = googleMap.addMarker(new MarkerOptions().icon(SystemUtility.getRoundedMarkerIconGreen(mActivity)).position(latLng)
                            .draggable(false).anchor(0.5f, 0.5f));
                    assert marker != null;
                    marker.setTitle("Tap Point");
                }
            }
            Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                    .color(Color.BLUE)
                    .width(7f)
                    .clickable(false)
                    .addAll(latLongList));
        }
    }

//------------------------------------------------------- on Success Response ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {
        Log.e("http", "responseCode: " + responseCode);
        Log.e("http", "response: " + response);

        // GPS TRACKING  SHOW ALL RECORD
        if(responseCode == URL_Utility.ResponseCode.WS_GPS_TRACKING_SHOW_RECORDS){
            String res = AESCrypt.decryptResponse(response);
            //Log.e(TAG,res);
            // DATA Not Null
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG,"Status: "+" "+ status);
                    // Success Response
                    if (status.equalsIgnoreCase("Success"))
                    {
                        JSONArray trackDataArray = new JSONArray(mObj.optString("trackData"));
                        if (trackDataArray.length() > 0) {
                            for (int i = 0; i < trackDataArray.length(); i++) {
                                JSONObject mOArray = trackDataArray.getJSONObject(i);
                                String type = mOArray.optString("type");
                                //String datetime = mOArray.optString("datetime");
                                JSONArray latLongArray = new JSONArray(mOArray.optString("latlong"));
                                ArrayList<LatLng> latLongList = new ArrayList<>();
                                for(int j=0; j<latLongArray.length(); j++){
                                    JSONObject mlatLonArray = latLongArray.getJSONObject(j);
                                    double latitude  = Double.parseDouble(mlatLonArray.optString("latitude"));
                                    double longitude = Double.parseDouble(mlatLonArray.optString("longitude"));
                                    latLongList.add(new LatLng(latitude,longitude));
                                }
                                if(latLongList.size() != 0){
                                    readShowAllRecord(type,latLongList);
                                }
                            }
                            dismissProgressbar();
                        }
                        else{
                            Utility.getOKDialogBox(GPSTrackingActivity.this, "Record Alert", "No Data Available", DialogInterface::dismiss);
                            dismissProgressbar();
                        }
                    }
                    // Fail Response
                    else{

                        //JSONArray trackDataArray = new JSONArray(mObj.optString("trackData"));
                        //if(trackDataArray.length() > 0) {
                            Utility.getOKDialogBox(GPSTrackingActivity.this, "Record Alert", "No Data Available", DialogInterface::dismiss);
                        //}
                      // Utility.getOKDialogBox(GPSTrackingActivity.this, "Record Alert", "Unable to Fetch All Record due to Internet Connection Issue Try Again Later.", DialogInterface::dismiss);
                         dismissProgressbar();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }
            else{
                dismissProgressbar();
            }
        }

    }

//------------------------------------------------------- on Error Response ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        String res = AESCrypt.decryptResponse(error.getMessage());
        Log.e(TAG,res);
        if(isSyncOn){
            googleMap.clear();
            readLocalData();
        }
        isSyncOn = false;
        syncList.clear();
        dismissProgressbar();
        Utility.getOKDialogBox(GPSTrackingActivity.this, "Alert", "Internet Connection Issue Occur Try Again Later", DialogInterface::dismiss);
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
        new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, (dialog, which) -> {
            dialog.dismiss();
            assistant.changeLocationSettings();
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
            // Current LatLon
            LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            // Accuracy
            txtAccuracy.setText("Accuracy: " + mCurrentLocation.getAccuracy() + " mtr");
            txtAccuracy.setVisibility(View.VISIBLE);
            if(!isLocation){
                isLocation = true;
                baseApplication.startMyService();
                dismissProgressbar();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                Log.e("WALKING","Location On");
            }
            // Is Walking On
            trackingByWalking(currentLatLng);
        }
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }

//------------------------------------------------------- progressBar ----------------------------------------------------------------------------------------------------------------------

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

//------------------------------------------------------- Visibility layout ---------------------------------------------------------------------------------------------------------------------------

    private void showButtonLayout(boolean isOn){
        if(isOn){
            llButton.setVisibility(View.VISIBLE);
        }
        if(!isOn){
            llButton.setVisibility(View.GONE);
        }
    }

    private void showUndoRedoButton(boolean isOn ){
        if(isOn){
            llUndoRedolayout.setVisibility(View.VISIBLE);
        }
        if(!isOn){
            llUndoRedolayout.setVisibility(View.GONE);
        }
    }

    private void showSaveButton(boolean isOn ){
        if(isOn){
            trackingSaveButton.setVisibility(View.VISIBLE);
        }
        if(!isOn){
            trackingSaveButton.setVisibility(View.GONE);
        }
    }

    private void showStopButton(boolean isOn ){
        if(isOn){
            trackingStopButton.setVisibility(View.VISIBLE);
        }
        if(!isOn){
            trackingStopButton.setVisibility(View.GONE);
        }
    }

    private void showCancelButton(boolean isOn ){
        if(isOn){
            trackingCancelButton.setVisibility(View.VISIBLE);
        }
        if(!isOn){
            trackingCancelButton.setVisibility(View.GONE);
        }
    }
    // Set Zoom Camera on map
    private void zoomCameraOn(Marker marker){
        zoomMapLayout.setVisibility(View.VISIBLE);
        zoomMapMarkerLayout.setVisibility(View.VISIBLE);
        zoomGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_GOOGLE_MAP_ZOOM));
    }
    // Zoom Camera off
    private void zoomCameraOff(){
        zoomMapLayout.setVisibility(View.GONE);
        zoomMapMarkerLayout.setVisibility(View.GONE);
    }

//------------------------------------------------------- close Fab Menu ---------------------------------------------------------------------------------------------------------------------------

    private void closeFabMenu(){
        fabMenu.close(true);
    }


//------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        assistant.start();
        Log.e("WALKING","Location On");
        // is Walking True
        if(isWalkingTrackingOn){
            if(Utility.getBooleanSavedData(this,Utility.WALKING)){
                Utility.saveData(this,Utility.WALKING,false);
                readTrackingByWalkingData();
            }
        }
    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        assistant.stop();
        Log.e("WALKING","Location OFF");
        if(isWalkingTrackingOn){
            Utility.saveData(this,Utility.WALKING,true);
        }
        super.onPause();
    }

//------------------------------------------------------- on Destroy ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        Utility.saveData(this,Utility.WALKING,false);
        super.onDestroy();
    }


//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {

        if(isWalkingTrackingOn){
            Utility.getYesNoDialogBox(this, "Walking Alert", "Do you want to cancel it?", dialog -> trackingByWalkingClear());
        }
        else if(isTapTrackingOn) {
            Utility.getYesNoDialogBox(this, "Tap Alert", "Do you want to cancel it?", dialog -> trackingByTapClear());
        }
        else{
            finish();
        }
    }

    private void moveCameraToCurrentLocation(){
        if (mCurrentLocation != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),20f));
        }
    }

}