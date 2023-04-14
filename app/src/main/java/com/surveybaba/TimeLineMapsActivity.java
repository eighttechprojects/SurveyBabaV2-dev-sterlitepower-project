package com.surveybaba;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.surveybaba.DTO.Geom;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Utilities.CustomInfoWindowGoogleMap;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.backup.BackupManagerActivity;
import com.surveybaba.model.BinTimeline;
import com.surveybaba.setting.SettingActivity;
import com.volly.BaseApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeLineMapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationAssistant.Listener {
    private static final String TAG = "MapsActivity";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvView;

    private LocationAssistant assistant;
    private Location mCurrentLocation;

    private GoogleMap mMap;
    private ImageView imgMyLocation;
    public static String key_intent_timeline = "timeline";
    private float ZOOM_LEVEL_DEFAULT = 18f;
    private boolean isMapZoomed;
    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity_maps);
        baseApplication = (BaseApplication) getApplication();
        assistant = new LocationAssistant(TimeLineMapsActivity.this, TimeLineMapsActivity.this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
        initDatabase();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Timeline");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nvView = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        initSliebar();
        init();
        initFab();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        setBaseMap();
        bindMapData();
    }

    private DataBaseHelper dataBaseHelper;

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(TimeLineMapsActivity.this);
    }

    private LinearLayout llBTmanager, llBackupManager, llSetting, llLogout, llBackupManagerView, llSettingView, llTimeline;
    private TextView tvUsername;

    private void initSliebar() {
        View hView = nvView.getHeaderView(0);
        llBackupManager = hView.findViewById(R.id.llBackupManager);
        llBTmanager = hView.findViewById(R.id.llBTmanager);
        llSetting = hView.findViewById(R.id.llSetting);
        llLogout = hView.findViewById(R.id.llLogout);
        llBackupManagerView = hView.findViewById(R.id.llBackupManagerView);
        llSettingView = hView.findViewById(R.id.llSettingView);
        tvUsername = hView.findViewById(R.id.tvUsername);
        llTimeline = hView.findViewById(R.id.llTimeline);
        tvUsername.setText(Utility.getSavedData(TimeLineMapsActivity.this, Utility.LOGGED_FIRSTNAME));

        llTimeline.setOnClickListener(clickSlidebar);
        llSettingView.setOnClickListener(clickSlidebar);
        llBackupManagerView.setOnClickListener(clickSlidebar);
        llSetting.setOnClickListener(clickSlidebar);
        llBackupManager.setOnClickListener(clickSlidebar);
        llLogout.setOnClickListener(clickSlidebar);
        llBTmanager.setOnClickListener(clickSlidebar);
    }


    private View.OnClickListener clickSlidebar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.llBackupManager:
                    if (llBackupManagerView.getVisibility() == View.GONE) {
                        llBackupManagerView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (llBackupManagerView.getVisibility() == View.VISIBLE) {
                        llBackupManagerView.setVisibility(View.GONE);
                        return;
                    }
                    break;
                case R.id.llBackupManagerView:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectBackupManger();
                    break;
                case R.id.llSetting:
                    if (llSettingView.getVisibility() == View.GONE) {
                        llSettingView.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (llSettingView.getVisibility() == View.VISIBLE) {
                        llSettingView.setVisibility(View.GONE);
                        return;
                    }
                    break;
                case R.id.llSettingView:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectSetting();
                    break;
                case R.id.llLogout:
                    mDrawer.closeDrawer(GravityCompat.START);
                    onClickLogout();
                    break;
            }
        }
    };


    private void onClickLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TimeLineMapsActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.lblAre_you_sure));
        alertDialog.setPositiveButton(getResources().getString(R.string.lblLogout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                processLogout();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.lblCancel), null);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void processLogout() {
        Utility.clearData(TimeLineMapsActivity.this);
        dataBaseHelper.open();
        dataBaseHelper.logout();
        dataBaseHelper.close();
        baseApplication.stopMyService();
        TimeLineMapsActivity.this.finish();
        finishAffinity();
        startActivity(new Intent(this, SplashActivity.class));
    }


    private void init() {
        imgMyLocation = findViewById(R.id.imgMyLocation);
        imgMyLocation.setOnClickListener(mapClick);
    }

    private View.OnClickListener mapClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {
                case R.id.imgMyLocation:
                    if (mCurrentLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reDirectBackupManger() {
        Intent intent = new Intent(TimeLineMapsActivity.this, BackupManagerActivity.class);
        startActivity(intent);
    }

    private void reDirectSetting() {
        Intent intent = new Intent(TimeLineMapsActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void setBaseMap() {
        String baseMap = Utility.getSavedData(TimeLineMapsActivity.this, Utility.BASE_MAP);
        if (Utility.isEmptyString(baseMap)) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else {
            if (baseMap.equalsIgnoreCase(Utility.BASE_MAP_HYBRID)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            } else if (baseMap.equalsIgnoreCase(Utility.BASE_MAP_SATELLITE)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (baseMap.equalsIgnoreCase(Utility.BASE_MAP_TERRAIN)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            } else if (baseMap.equalsIgnoreCase(Utility.BASE_MAP_NORMAL)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else if (baseMap.equalsIgnoreCase(Utility.BASE_MAP_NONE)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        }
    }

    ArrayList<LatLng> arrayRoute = new ArrayList<LatLng>();

    private void bindMapData() {
        try {
            mMap.clear();
            mMap.setInfoWindowAdapter(new CustomInfoWindowGoogleMap(TimeLineMapsActivity.this));
            dataBaseHelper.open();
            Cursor curData = dataBaseHelper.executeCursor(dataBaseHelper.getGeomDetails());
            if (curData != null && curData.getCount() > 0) {
                curData.moveToFirst();
                for (int i = 0; i < curData.getCount(); i++) {
                    String gid = curData.getString(curData.getColumnIndex("gid"));
                    String user_id = curData.getString(curData.getColumnIndex("user_id"));
                    String file_path = curData.getString(curData.getColumnIndex("file_path"));
                    String geom_type = curData.getString(curData.getColumnIndex("geom_type"));
                    String geom_array = curData.getString(curData.getColumnIndex("geom_array"));
                    String latitude = curData.getString(curData.getColumnIndex("latitude"));
                    String longitude = curData.getString(curData.getColumnIndex("longitude"));
                    String accuracy = curData.getString(curData.getColumnIndex("accuracy"));
                    String record_date = curData.getString(curData.getColumnIndex("record_date"));
                    String viewData = curData.getString(curData.getColumnIndex("viewData"));
                    String syncData = curData.getString(curData.getColumnIndex("syncData"));
                    String IS_SYNC = curData.getString(curData.getColumnIndex("IS_SYNC"));
                    Geom vo = new Geom(gid, "", "", geom_type, geom_array, latitude, longitude, accuracy, record_date, viewData, syncData, IS_SYNC);
                    arrayRoute.clear();
                    JSONArray jsonArray = new JSONArray(geom_array);
                    for (int jsonCount = 0; jsonCount < jsonArray.length(); jsonCount++) {
                        JSONObject c = jsonArray.getJSONObject(jsonCount);
                        String _lat = c.optString("latitude");
                        String _lng = c.optString("longitude");
                        LatLng mLatLng = new LatLng(Double.parseDouble(_lat), Double.parseDouble(_lng));
                        arrayRoute.add(mLatLng);
                    }
                    BinTimeline binTimeline = new BinTimeline();
                    binTimeline.setUserId(user_id);
                    binTimeline.setRecordDate(record_date);
                    binTimeline.setImageFilePath(file_path);
                    binTimeline.setDescription(viewData);
                    binTimeline.setLat(Double.parseDouble(latitude));
                    binTimeline.setLongi(Double.parseDouble(longitude));
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(arrayRoute.get(0))
                            .title(arrayRoute.get(0).latitude+", "+arrayRoute.get(0).longitude);
                    Marker m1 = mMap.addMarker(markerOptions);
                    m1.setTag(binTimeline);
                    curData.moveToNext();
                }
            }
            dataBaseHelper.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (arrayRoute.size() > 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayRoute.get(0)));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (assistant.onPermissionsUpdated(requestCode, grantResults)) {
        }
    }

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TimeLineMapsActivity.this);
        dialogBuilder.setTitle(R.string.app_name);
        dialogBuilder.setMessage("Need\nPermission");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assistant.requestLocationPermission();
            }
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

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        if (location == null) return;
        mCurrentLocation = location;
        if(mMap!=null && !isMapZoomed)
        {
            isMapZoomed = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), ZOOM_LEVEL_DEFAULT));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemUtility.setFullscreenToggle(this, Utility.getBooleanSavedData(this, Utility.IS_FULL_SCREEN));
        SystemUtility.setScreenOrientation(this, Utility.getBooleanSavedData(this, Utility.IS_SCREEN_ORIENTATION_PORTRAIT));
        SystemUtility.setKeepScreenAwakeAlwaysToggle(this, Utility.getBooleanSavedData(this, Utility.IS_ALWAYS_KEEP_SCREEN_AWAKE));
        if(mMap!=null)
            setBaseMap();
        assistant.start();
    }

    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

    private FloatingActionButton fabTimeLine;
    private FloatingActionsMenu fabMenu;

    private void initFab() {
        fabMenu = findViewById(R.id.fabMenu);
        fabTimeLine = findViewById(R.id.fabTimeLine);
        fabTimeLine.setOnClickListener(floatClick);
    }

    private View.OnClickListener floatClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fabMenu.collapse();
            int getid = v.getId();
            switch (getid) {
                case R.id.fabTimeLine:
                    reDirectTimeLine();
                    break;
            }
        }
    };

    private void reDirectTimeLine() {
        Intent intent = new Intent(TimeLineMapsActivity.this, TimeLineActivity.class);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null && data.getExtras().containsKey(key_intent_timeline)) {
                bindMapData();
            }
        }
    }
}