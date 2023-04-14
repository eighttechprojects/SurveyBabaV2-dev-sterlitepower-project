package com.surveybaba.Modules.GPSTrackingModule;

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
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Modules.GPSTrackingActivity;
import com.surveybaba.R;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
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

public class GPSTrackingMapActivity extends AppCompatActivity implements OnMapReadyCallback,LocationAssistant.Listener, View.OnClickListener, WSResponseInterface {

    // TAG
    private static final String TAG = "GPSTrackingMapActivity";
    // Google Map
    private GoogleMap mMap;
    // Activity
    private Activity mActivity;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    private boolean isLocation = false;
    // Progress Dialog Bar
    private ProgressDialog progressDialog;
    // Text View
    private TextView txtAccuracy;
    // Relative Layout
    private RelativeLayout rlCurrentLocation;

//------------------------------------------------------- On Create ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracking_map);
        // Add Back Button on ToolBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        // Activity
        mActivity = this;
        // Location
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Base Application
        BaseApplication baseApplication = (BaseApplication) getApplication();
        baseApplication.startMyService();

        // Support Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gpsTrackingMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // init
        init();
        // setOnClickListener
        setOnClickListener();



    }

//------------------------------------------------------- init ---------------------------------------------------------------------------------------------------------------------------

    private void init(){

        // Text View
        txtAccuracy = findViewById(R.id.txtAccuracy);
        // Current Location
        rlCurrentLocation = findViewById(R.id.rlCurrentLocation);

    }

//------------------------------------------------------- set On Click Listener ---------------------------------------------------------------------------------------------------------------------------

    private void setOnClickListener(){

        rlCurrentLocation.setOnClickListener(this);
    }

//------------------------------------------------------- onMapReady ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Google Map
        mMap = googleMap;
        // Type of Map is Hybrid!
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // My Location Enable
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }

//------------------------------------------------------- onClick ---------------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rlCurrentLocation:
                moveCameraToCurrentLocation();
                break;


        }
    }

//------------------------------------------------------- On Create Options Menu ------------------------------------------------------------------------------------------------------------------

    // Menu Option in google map
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gpstrackingmap_menu, menu);
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

        return false;
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
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(this,Utility.LOGGED_TOKEN));
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
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_SHOW;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_GPS_TRACKING_SHOW, params, false, false);
    }

//------------------------------------------------------- onSuccessResponse ------------------------------------------------------------------------------------------------------------------

    @Override
    public void onSuccessResponse(URL_Utility.ResponseCode responseCode, String response) {

        // GPS TRACKING  SHOW ALL RECORD
        if(responseCode == URL_Utility.ResponseCode.WS_GPS_TRACKING_SHOW){
            String res = AESCrypt.decryptResponse(response);
            Log.e(TAG,res);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG,"Status: "+" "+ status);
                    // Success Response
                    if (status.equalsIgnoreCase("Success"))
                    {

                    }
                    // Fail Response
                    else{
                        dismissProgressbar();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    dismissProgressbar();
                    Toast.makeText(mActivity, "something is Wrong", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(mActivity, "something is Wrong", Toast.LENGTH_SHORT).show();
                dismissProgressbar();
            }
        }

    }

//------------------------------------------------------- onErrorResponse ------------------------------------------------------------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        Log.e(TAG,error.getMessage());
        dismissProgressbar();
        Toast.makeText(mActivity, "something wrong", Toast.LENGTH_SHORT).show();
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
            isLocation = false;
            txtAccuracy.setVisibility(View.GONE);
        }
        else{
            // CurrentLocation
            mCurrentLocation = location;
            // Accuracy
            txtAccuracy.setText("Accuracy: " + mCurrentLocation.getAccuracy() + " mtr");
            txtAccuracy.setVisibility(View.VISIBLE);
            if(!isLocation){
                isLocation = true;
                // Current LatLon
                LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
            }
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

//------------------------------------------------------- on Destroy ---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        Utility.saveData(this,Utility.WALKING,false);
        super.onDestroy();
    }

//------------------------------------------------------- on back Pressed ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
    }

//------------------------------------------------------- moveCameraToCurrentLocation ---------------------------------------------------------------------------------------------------------------------------

    private void moveCameraToCurrentLocation(){
        if (mCurrentLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),20f));
        }
    }


}