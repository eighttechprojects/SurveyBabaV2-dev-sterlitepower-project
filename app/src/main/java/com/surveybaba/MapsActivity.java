package com.surveybaba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.duy.compass.MainCompassActivity;
import com.fileupload.AndroidMultiPartEntity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.collections.PolygonManager;
import com.surveybaba.ADAPTER.AdapterSurveyMarker;
import com.surveybaba.ADAPTER.LayersAdapter;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.DTO.ProjectDTO;
import com.surveybaba.DashBoard.DashBoardBNActivity;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.Fragment.HomeFragment;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Modules.MapCameraActivity;
import com.surveybaba.Modules.WMSMap.WMSProvider;
import com.surveybaba.Modules.WMSMap.WMSTileProviderFactory;
import com.surveybaba.PolygonInsideOutSide.PolygonInsideOutside;
import com.surveybaba.Utilities.CustomInfoWindowGoogleMap;
import com.surveybaba.Utilities.ImageFileUtils;
import com.surveybaba.Utilities.MyBroadcastReceiver;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.backup.BackupManagerActivity;
import com.surveybaba.model.BinLayerProject;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.FormLatLon;
import com.surveybaba.model.GeoFenceModel.GeoFenceLatLon;
import com.surveybaba.model.GeoFenceModel.GeoFenceModel;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.ProjectLayerModel;
import com.surveybaba.model.SurveyFormLocalModel;
import com.surveybaba.model.SurveyFormModel;
import com.surveybaba.model.TimeLineModel;
import com.surveybaba.model.TimeLineSModel;
import com.surveybaba.model.TrackingStatusData;
import com.surveybaba.setting.SettingActivity;
import com.volly.BaseApplication;
import com.volly.URL_Utility;
import com.volly.WSResponseInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.surveybaba.Database.DataBaseHelper.keyParam_data;
import static com.surveybaba.Utilities.Utility.GIS_SURVEY_WORK_ID;
import static com.surveybaba.Utilities.Utility.PASS_LAT;
import static com.surveybaba.Utilities.Utility.PASS_LONG;
import static com.surveybaba.Utilities.Utility.PASS_PROJECT_ID;
import static com.surveybaba.Utilities.Utility.PASS_WORK_ID;
import static com.surveybaba.Utilities.Utility.PROJECT_ID_DEFAULT;
import static com.surveybaba.Utilities.Utility.PROJECT_PROJECT_ID;
import static com.surveybaba.Utilities.Utility.PROJECT_WORK_ID;
import static com.surveybaba.Utilities.Utility.convertAreaFromSqMeter;
import static com.surveybaba.Utilities.Utility.convertDistanceFromMeter;
import static com.surveybaba.Utilities.Utility.getRecordDate;
import static com.surveybaba.Utilities.Utility.showConverterMeasuremetDialog;
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
import uk.co.senab.photoview.PhotoViewAttacher;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationAssistant.Listener,  GoogleMap.OnMarkerClickListener {

    // TAG
    private static final String TAG = "MapsActivity";
    // Change by Rahul Suthar
    private static final String TAG_TRACKING = "Tracking";
    // Google Map
    private GoogleMap mMap;
    // Activity
    Activity mActivity;
    // Drawer
    private DrawerLayout mDrawer;
    private NavigationView nvView;
    // Toolbar
    private Toolbar toolbar;
    // DataBase
    private DataBaseHelper dataBaseHelper;
    // Base Application
    BaseApplication baseApplication;
    private ActionBarDrawerToggle drawerToggle;
    // Location
    private LocationAssistant assistant;
    private Location mCurrentLocation;
    // Broadcast
    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    // Permission
    SystemPermission systemPermission;
    // Camera Image
    private static final int MAP_CAMERA_REQUESTCODE = 1002;

    //----------------------------------
    // Camera Point
    private Marker cameraPointMarker;
    private int cameraPointCounter = 0;
    private boolean isMapCameraOn = false;


    // Point/Circle -------------------------------------------------------------------
    private Marker pointMarker;
    private int pointCounter = 0;
    private ArrayList<Marker> listpoint = new ArrayList<>();
    private ArrayList<Marker> listPointMarker = new ArrayList<>();
    private Circle circleTemp;
    private Marker circleMarkerTemp;
    private ArrayList<Marker> listCirclesMarker = new ArrayList<>();
    private ArrayList<Circle> listCircles = new ArrayList<>();
    // Redo and Undo
    private ArrayList<ArrayList<LatLng>> undoListPointLatLongs = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> redoListPointLatLongs = new ArrayList<>();

    // PolyLine --------------------------------------------------------------------------
    private Polyline mPolyline, mPolylineTemp;
    private ArrayList<LatLng> listLinesLatLngs = new ArrayList<>();
    private ArrayList<Marker> listLinesMarkers = new ArrayList<>();
    private ArrayList<LatLng> listLinesLatLngsTemp = new ArrayList<>();
    private ArrayList<Marker> listLinesMarkersTemp = new ArrayList<>();
    // Redo and Undo
    private ArrayList<ArrayList<LatLng>> undoListLineLatLongs = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> redoListLineLatLongs = new ArrayList<>();

    // Polygon -----------------------------------------------------------------------
    private Polygon mPolygon;
    private ArrayList<LatLng> listPolygonLatLongs = new ArrayList<>();
    private ArrayList<Marker> listPolygonMarkers = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> undoListPolygonLatLongs = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> redoListPolygonLatLongs = new ArrayList<>();

    // Polygon Walking -----------------------------------------------------------------------
    private Polygon mWalkingPolygon;
    private Marker mWalkingPolygonLastPointMarker;
    private int mWalkingPolygonMarkerCount = 0;
    private ArrayList<LatLng> listWalkingPolygonLatLongs = new ArrayList<>();
    private ArrayList<Marker> listWalkingPolygonMarkers = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> undoListWalkingPolygonLatLongs = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> redoListWalkingPolygonLatLongs = new ArrayList<>();


    // Layer Render -----------------------------------------------------------------------

    private final int smallDashLength = 10;
    private final int smallDashGap    = 5;
    private final int largeDashLength = 20;
    private final int largeDashGap    = 15;

    private int colorFillTransparent = Color.parseColor("#4DFFEA00");
    private int colorGreenTransparent = Color.parseColor("#4Db9feb9");
    private int colorRedTransparent = Color.parseColor("#4Dffcccc");
    int colorPolyLine = Color.BLUE;
    int colorPolyStroke = Color.YELLOW, colorPolyFill = colorFillTransparent;
    private int countLayerRendering = 0;
    private ArrayList<LatLng> listLatlongLayerClicked = new ArrayList<>();
    private ArrayList<ProjectLayerModel> listLayersReadOnly = new ArrayList<>();
    ArrayList<String> colorListPolygonLayers = new ArrayList<>();
    ArrayList<String> colorListPolyLineLayers = new ArrayList<>();
    ArrayList<String> colorListPointLayers = new ArrayList<>();
    ArrayList<String> iconListPointLayers = new ArrayList<>();
    ArrayList<String> lineTypePolyLineLayer = new ArrayList<>();
    ArrayList<String> layer_viewonly = new ArrayList<>();

    // Polyline boundry
    private ArrayList<Polyline> listPolyLineLayerReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<Polyline>> listOfMultiLayerPolyLine = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> listLatLngPolyLineLayerReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<LatLng>>> listOfMultiLayerPolyLineReadOnly = new ArrayList<>();
    // Polygon boundry
    private ArrayList<Polygon> listOfPolygonsBoundryLayerReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<Polygon>> listOfMultiLayersPolygons = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> listOfPolygonLatlngsBoundryLayerReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<LatLng>>> listOfMultiLayerPolygonLatlngsReadOnly = new ArrayList<>();
    // Point/Markers
    private ArrayList<Marker> listOfMarkersLayersReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<Marker>> listOfMultiPointsLayers = new ArrayList<>();
    private ArrayList<ArrayList<LatLng>> listOfMarkersLatLngLayersReadOnly = new ArrayList<>();
    // private ArrayList<ArrayList<LatLng>> listOfMultiLayerPointsReadOnly = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<LatLng>>> listOfMultiLayerPointsReadOnly = new ArrayList<>();

    // WMS  Layer
    private ArrayList<String> listOfWMSLayer = new ArrayList<>();

    // Magnifying Glass --------------------------------------------------------------------
    private CardView cardMagnifyingGlass;
    private RelativeLayout rlMagnifyingGlass;
    private ImageView imgMagnifyingGlass;

    // Text View
    private TextView tvUsername,tvUserEmailID ,txtAccuracy1;
    private TextView txtDistance;
    // Float Button
    private FloatingActionButton fabMesur, fabPoly, fabLine, fabPoint, fabLayers;
    private FloatingActionsMenu fabMenu;
    // Relative Layout
    private RelativeLayout rlCentre, rlUndo, rlRedo, rlMapStyle, rlMyProjectLocation, rlBottomView, rlLine, rlPolygon, rlWalking,rlSaveWalkingLocation,rlWalkingStop,rlCamera;
    private RelativeLayout rlZoomIn, rlZoomOut, rlLayers, rlSaveProjectLayer,rlConverterMeasurement,rlWalkingReset,rlWalkingMinDistance;
    // Linear Layout
    private LinearLayout llTimeline, llMeasuring,llGpsSatellite, llDashBoard ,llLayers, llBTmanager, llGpsCompass, llCompass, llMySubscription, llProjects, llBackupManager, llSetting, llLogout,llSync, llBackupManagerView, llSettingView, llMenu;
    private LinearLayout llFeature, llMeasuringTools;
    // Recycler View
    private RecyclerView rcvLayers;
    // Adapter
    private LayersAdapter layersAdapter;
    // Handler
    private Handler mHandlerTimer = new Handler();
    private Handler mHandlerTimerUiInActive = new Handler();
    // Timer
    private Timer myTimer, myTimerUiInActive;
    TimerTask timerTask;
    // Boolean
    private boolean isPolygonWalkingOn = false;
    private boolean isTimerStarted;
    private boolean isTimerStartedUiInActive;
    private boolean isMeasurementModeON;
    boolean isNormalUserMode, isCurrentLocInvoked;
    boolean isSatelliteMap;
    private boolean isAutomatedMapMoved;
    boolean isDrawing;
    private boolean isServiceStarted, isWalkingTool, isDraggingMarker;
    // Static
    private final long TIMER_DELAY = 5000;
    private long TIMER_DELAY_UI_INACTIVE = 10000;
    int MAX_WIDTH_MAG_BITMAP = 200;
    int MAX_HEIGHT_MAG_BITMAP = 200;
    int DEFAULT_WIDTH_BITMAP = 100;
    int DEFAULT_HEIGHT_BITMAP = 100;
    private float ZOOM_LEVEL_BOUNDRY =  20f;//10.5f;
    private float ZOOM_LEVEL_DEFAULT = 18f;
    private double RADIUS_CIRCLE_MTR = 10d;// in mtrs
    private double MIN_DISTANCE_UPDATE = 5d;// in mtrs
    private double MIN_POLYGON_DISTANCE_UPDATE = 5d; // in mtrs

    // Marker
    private Marker tempMiddleMarker1, tempMiddleMarker2, tempMiddleMarker3;
    private Marker projectMarker;
    // LatLng
    private LatLng projectLatLng;
    private LatLng centreLatLng;
    // String
    private String selectedFeature = "";
    public static String key_intent_timeline = "timeline";
    private String totalDistanceLineVertices;
    private String project_id = PROJECT_ID_DEFAULT, form_id = "", form_name = "", layersArr = "";
    String work_id ="0";
    // Bitmap
    Bitmap decodedBitmap = null;
    Projection projection = null;
    // Alert Dialog
    AlertDialog alertDialog;
    // Progress Bar
    private ProgressDialog progressDialog;
    // ImageView
    private ImageView imgUserProfileImage;
    private ImageView imgSavePlus;
    private ImageView imgMyLocation, imgCentreLocation;
    // Sync
    private boolean isSyncOn = false;
    private boolean isFormSync = false;
    private boolean isMapCameraSync = false;
    private ArrayList<FormDataModel> syncFormDataList =  new ArrayList<>();
    private FormDataModel formDataModel;
    public static final String TYPE_FILE   = "file";
    public static final String TYPE_CAMERA = "cameraUploader";
    public static final String TYPE_VIDEO  = "videoUploader";
    public static final String TYPE_AUDIO  = "audioUploader";
    public static boolean isFileUpload   = true;
    public static boolean isCameraUpload = true;
    public static boolean isVideoUpload  = true;
    public static boolean isAudioUpload  = true;
    long totalSize = 0;

    // Geo Fence
    ArrayList<LatLng> geoFence = new ArrayList<>();

    BinLayerProject binLayerProjectSelected = null;
    ProjectLayerModel projectLayerModel = null;
    ProjectLayerModel projectLayerModelSelected = null;

    // GeoFence
    Polygon polygonGeoFence = null;
    Marker markerGeoFence = null;

//------------------------------------------------------- On Create --------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_maps);
        // Tool Bar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        // Drawer Layout
        nvView = findViewById(R.id.nvView);
        mDrawer = findViewById(R.id.drawer_layout);
        // Activity
        mActivity = this;
        // base Application
        baseApplication = (BaseApplication) getApplication();
        // Device MIN-Distance
        Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_5);

        // ToolBar
        // Navigation Toggle Button
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawer, toolbar, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Location
        assistant = new LocationAssistant(mActivity, MapsActivity.this, LocationAssistant.Accuracy.HIGH, 0, true);
        assistant.setVerbose(true);
        // init !
        getWorkID();
        initDatabase();
        initSliebar();
        // init
        init();
      //  initFab();
        // map Fragment!
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        // System Permission
        systemPermission = new SystemPermission(this);

        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

    }

//------------------------------------------------------- InitDatabase --------------------------------------------------------------------------------------------------------------------------

    private void initDatabase() {
        dataBaseHelper = new DataBaseHelper(MapsActivity.this);
    }

//------------------------------------------------------- init Slide bar--------------------------------------------------------------------------------------------------------------------------

    private void initSliebar() {
        View hView = nvView.getHeaderView(0);

        tvUsername = hView.findViewById(R.id.tvUsername);
        tvUserEmailID = hView.findViewById(R.id.tvUserEmailID);
        imgUserProfileImage = hView.findViewById(R.id.UserProfileImage);
        // Set User Name
        tvUsername.setText(Utility.getSavedData(mActivity, Utility.PROFILE_FIRSTNAME));
        // Set User Email ID
        tvUserEmailID.setText(Utility.getSavedData(mActivity, Utility.PROFILE_EMAILID));
        // Set profile Image
        try{
            // Set profile Image
            Glide.with(mActivity).load(decodeBase64Image(Utility.getSavedData(mActivity, Utility.PROFILE_IMAGE))).error(R.drawable.ic_login_user_icon).circleCrop().into(imgUserProfileImage);
        }
        catch (Exception e){
            // Set profile Image
            Glide.with(mActivity).load(R.drawable.ic_login_user_icon).error(R.drawable.ic_login_user_icon).circleCrop().into(imgUserProfileImage);
            Log.e(TAG, e.getMessage());
        }

        llCompass = hView.findViewById(R.id.llCompass);
        llGpsSatellite = hView.findViewById(R.id.llGpsSatellite);
        llSetting = hView.findViewById(R.id.llSetting);
        llLogout = hView.findViewById(R.id.llLogout);
        llDashBoard = hView.findViewById(R.id.llDashBoard);
        llSync = hView.findViewById(R.id.llSync);

        llCompass.setOnClickListener(clickSlidebar);
        llGpsSatellite.setOnClickListener(clickSlidebar);
        llSetting.setOnClickListener(clickSlidebar);
        llLogout.setOnClickListener(clickSlidebar);
        llDashBoard.setOnClickListener(clickSlidebar);
        llSync.setOnClickListener(clickSlidebar);
        llDashBoard.setVisibility(View.VISIBLE);
        llLogout.setVisibility(View.VISIBLE);
        llSync.setVisibility(View.VISIBLE);
    }

    private void getWorkID(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("work_id");
            Utility.saveData(this,Utility.PROJECT_WORK_ID,value);
        }
    }

//------------------------------------------------------- init --------------------------------------------------------------------------------------------------------------------------

    private void init() {
        llFeature = findViewById(R.id.llFeature);
        rlCentre = findViewById(R.id.rlCentre);
        rlUndo = findViewById(R.id.rlUndo);
        rlRedo = findViewById(R.id.rlRedo);
        rlMapStyle = findViewById(R.id.rlMapStyle);
        rlBottomView = findViewById(R.id.rlBottomView);
        rlMyProjectLocation = findViewById(R.id.rlMyProjectLocation);
        txtDistance = findViewById(R.id.txtDistance);
        imgMyLocation = findViewById(R.id.imgMyLocation);
        imgCentreLocation = findViewById(R.id.imgCentreLocation);
        rcvLayers = findViewById(R.id.recyclerViewLayers);
        cardMagnifyingGlass = findViewById(R.id.cardMagnifyingGlass);
        rlMagnifyingGlass = findViewById(R.id.rlMagnifyingGlass);
        imgMagnifyingGlass = findViewById(R.id.imgMagnifyingGlass);
        llMenu = findViewById(R.id.llMenu);
        rlZoomIn = findViewById(R.id.rlZoomIn);
        rlZoomOut = findViewById(R.id.rlZoomOut);
        rlLayers = findViewById(R.id.rlLayers);
        rlSaveProjectLayer = findViewById(R.id.rlSaveProjectLayer);
        imgSavePlus = findViewById(R.id.imgSavePlus);
        llMeasuringTools = findViewById(R.id.llMeasuringTools);
        rlLine = findViewById(R.id.rlLine);
        rlPolygon = findViewById(R.id.rlPolygon);
        rlWalking = findViewById(R.id.rlWalking);
        txtAccuracy1 = findViewById(R.id.txtAccuracy1);
        // Change Made By Rahul Suthar
        rlSaveWalkingLocation = findViewById(R.id.rlSaveWalkingLocation);
        rlWalkingStop = findViewById(R.id.rlWalkingStop);
        rlCamera = findViewById(R.id.rlCamera);
        rlConverterMeasurement = findViewById(R.id.rlConverterMeasurement);
        rlWalkingReset = findViewById(R.id.rlWalkingReset);
        rlWalkingMinDistance = findViewById(R.id.rlWalkingMinDistance);
        // SetOnClick Listener
        llMenu.setOnClickListener(mapClick);
        rlCentre.setOnClickListener(mapClick);
        rlUndo.setOnClickListener(mapClick);
        rlRedo.setOnClickListener(mapClick);
        rlMapStyle.setOnClickListener(mapClick);
        imgMyLocation.setOnClickListener(mapClick);
        rlMyProjectLocation.setOnClickListener(mapClick);
        rlZoomIn.setOnClickListener(mapClick);
        rlZoomOut.setOnClickListener(mapClick);
        rlLayers.setOnClickListener(mapClick);
        rlSaveProjectLayer.setOnClickListener(mapClick);
        rlLine.setOnClickListener(mapClick);
        rlPolygon.setOnClickListener(mapClick);
        rlWalking.setOnClickListener(mapClick);
        rlSaveWalkingLocation.setOnClickListener(mapClick);
        rlWalkingStop.setOnClickListener(mapClick);
        rlCamera.setOnClickListener(mapClick);
        rlConverterMeasurement.setOnClickListener(mapClick);
        rlWalkingReset.setOnClickListener(mapClick);
        rlWalkingMinDistance.setOnClickListener(mapClick);


        layersAdapter = new LayersAdapter(mActivity, listLayersReadOnly, new LayersAdapter.onLayerSelected() {
            @Override
            public void getLayer(ProjectLayerModel projectLayer, boolean isRecentlyEnabled) {

                switch (projectLayer.getLayerType()) {
                    // Point
                    case MapsActivity.LAYER_TYPE.Point:
                        projectLayerModel = projectLayer;
                        dataBaseHelper.updateOneProjectLayerData(projectLayer); //SetActivate and Enabled
                        llFeature.setVisibility(View.VISIBLE);
                        // Layer
                        updateVisibilityOfLayersSettings(false);
                        // Camera OFF
                        updateVisibilityOfrlCamera(false);
                        // Convertmeasurment Off
                        showConverterMeasurmentButton(false);
                        // Walking Reset
                        showrlWalkingResetButton(false);
                        // Walking Min Distance Button
                        showrlWalkingMinDistanceButton(false);
                        // Walking Stop
                        updateLayerWalkingStop(false);

                        updateCheckMarkFloatingButton(true, projectLayer.getLayerName());
                        selectedFeature = getResources().getString(R.string.Point);
                        isWalkingTool = false;
                        isPolygonWalkingOn = false;
                        break;

                    // PolyLine
                    case MapsActivity.LAYER_TYPE.Line:

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
                        builder.setTitle("Line Option");
                        //builder.setCancelable(false);
                        String[] line_items = {"Line By Walk", "Line By Tap"};
                        builder.setItems(line_items, (dialog, which) -> {
                            if (line_items[which].equals("Line By Walk")) {
                                Toast.makeText(mActivity, "Walking Started", Toast.LENGTH_SHORT).show();
                                projectLayerModel = projectLayer;
                                dataBaseHelper.updateOneProjectLayerData(projectLayer); //SetActivate and Enabled
                                llFeature.setVisibility(View.VISIBLE);
                                // Layer
                                updateVisibilityOfLayersSettings(false);
                                // Camera OFF
                                updateVisibilityOfrlCamera(false);
                                // Convertmeasurment true
                                showConverterMeasurmentButton(false);
                                // Walking Reset
                                showrlWalkingResetButton(true);
                                // Walking Min Distance
                                showrlWalkingMinDistanceButton(true);
                                // Walking Stop
                                updateLayerWalkingStop(true);

                                updateCheckMarkFloatingButton(true, projectLayer.getLayerName());
                                selectedFeature = getResources().getString(R.string.Line);
                                isPolygonWalkingOn = false;
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_COLOR, projectLayerModel.getLayerLineColor());
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_TYPE, projectLayerModel.getLayerLineType());
                                isWalkingTool = true;
                                dialog.dismiss();
                            }
                            else if (line_items[which].equals("Line By Tap")) {
                                Toast.makeText(mActivity, "PolyLine Tap Started", Toast.LENGTH_SHORT).show();
                                projectLayerModel = projectLayer;
                                dataBaseHelper.updateOneProjectLayerData(projectLayer); //SetActivate and Enabled
                                llFeature.setVisibility(View.VISIBLE);
                                // Layer
                                updateVisibilityOfLayersSettings(false);
                                // Camera OFF
                                updateVisibilityOfrlCamera(false);
                                // Converter Measurment
                                showConverterMeasurmentButton(false);
                                // Walking Reset
                                showrlWalkingResetButton(false);
                                // Walking Min Disntace
                                showrlWalkingMinDistanceButton(false);
                                // Walking Stop
                                updateLayerWalkingStop(false);
                                updateCheckMarkFloatingButton(true, projectLayer.getLayerName());
                                selectedFeature = getResources().getString(R.string.Line);
                                isPolygonWalkingOn = false;
                                isWalkingTool = false;
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_COLOR, projectLayerModel.getLayerLineColor());
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_TYPE, projectLayerModel.getLayerLineType());
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                        break;

                    // Polygon
                    case MapsActivity.LAYER_TYPE.Polygon:
                        android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(mActivity);
                        build.setTitle("Polygon Option");
                        //build.setCancelable(false);
                        String[] line_item = {"Polygon By Walk", "Polygon By Tap"};
                        build.setItems(line_item, (dialog, which) -> {
                            if (line_item[which].equals("Polygon By Walk")) {
                                Toast.makeText(mActivity, "Walking Started", Toast.LENGTH_SHORT).show();
                                projectLayerModel = projectLayer;
                                dataBaseHelper.updateOneProjectLayerData(projectLayer); //SetActivate and Enabled
                                llFeature.setVisibility(View.VISIBLE);
                                updateVisibilityOfLayersSettings(false);
                                // Camera OFF
                                updateVisibilityOfrlCamera(false);
                                // Convert Measurment
                                showConverterMeasurmentButton(true);
                                // Walking Reset
                                showrlWalkingResetButton(true);
                                // Walking Min Distance
                                showrlWalkingMinDistanceButton(true);
                                // Walking Stop
                                updateLayerWalkingStop(true);
                                updateCheckMarkFloatingButton(true, projectLayer.getLayerName());
                                isPolygonWalkingOn = true;
                                selectedFeature = getResources().getString(R.string.WalkingPolygone);
                                isWalkingTool = true;
                                Utility.saveData(MapsActivity.this,Utility.LAYER_LINE_COLOR,projectLayerModel.getLayerLineColor());
                                Utility.saveData(MapsActivity.this,Utility.LAYER_LINE_TYPE,projectLayerModel.getLayerLineType());
                                dialog.dismiss();
                            }
                            else if (line_item[which].equals("Polygon By Tap")) {
                                Toast.makeText(mActivity, "Polygon Tap Started", Toast.LENGTH_SHORT).show();
                                projectLayerModel = projectLayer;
                                dataBaseHelper.updateOneProjectLayerData(projectLayer); //SetActivate and Enabled
                                llFeature.setVisibility(View.VISIBLE);
                                updateVisibilityOfLayersSettings(false);
                                // Camera OFF
                                updateVisibilityOfrlCamera(false);
                                // Converter Measurment
                                showConverterMeasurmentButton(true);
                                // Walking Reset
                                showrlWalkingResetButton(false);
                                // Walking Min Distance
                                showrlWalkingMinDistanceButton(false);
                                // Walking Stop
                                updateLayerWalkingStop(false);
                                updateCheckMarkFloatingButton(true, projectLayer.getLayerName());
                                isPolygonWalkingOn = false;
                                selectedFeature = getResources().getString(R.string.Polygone);
                                isWalkingTool = false;
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_COLOR, projectLayerModel.getLayerLineColor());
                                Utility.saveData(MapsActivity.this, Utility.LAYER_LINE_TYPE, projectLayerModel.getLayerLineType());
                                dialog.dismiss();
                            }
                        });
                        build.show();
                        break;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvLayers.setLayoutManager(linearLayoutManager);
        rcvLayers.setAdapter(layersAdapter);
    }

//------------------------------------------------------- InitExtra --------------------------------------------------------------------------------------------------------------------------

    private void initExtra() {
        project_id = Utility.getSavedData(mActivity, Utility.KEY_PROJECT_ID);
        isNormalUserMode = project_id.equalsIgnoreCase(PROJECT_ID_DEFAULT);
        // Tool bar
        toolbar.setTitle(Utility.getSavedData(mActivity, Utility.PROJECT_PROJECT_NAME));
        toolbar.setSubtitle(Utility.getSavedData(mActivity, Utility.PROJECT_WORK_NAME));
        // Layer
        listLayersReadOnly.clear();
        listLayersReadOnly.addAll(dataBaseHelper.getProjectLayersList(Utility.getSavedData(this,Utility.PROJECT_WORK_ID), false));
        layersAdapter.notifyDataSetChanged();

        if (listLayersReadOnly.size() > 0){
            // top recycleview/ layer view
            renderLayersOffline(listLayersReadOnly, true, true, true);
        }
        else{
            startMyDialog("Loading Data...");
            ExecutorService service = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            service.execute(() -> {
                        // Post ----------------------------------------
                        handler.post(() -> {
                              loadLocalData();
                            dismissMyDialog();
                        });
            });

//            //  Log.e(TAG, "Draw GeoFence");
//            drawGeoFence();
//            // Log.e(TAG, "Get All Survey Data");
//            GetAllSurveyData();
//            //  Log.e(TAG, "Get Survey Form Data");
//            GetSurveyFormData();
//            //   Log.e(TAG, "Get Map Camera Local  Data");
//            readMapCameraImageLocal();
//            //   Log.e(TAG, "Get Map Camera S Data");
//            readMapCameraImageS();
        }


    }

//------------------------------------------------------- Set base map --------------------------------------------------------------------------------------------------------------------------

    private void setBaseMap() {
        String baseMap = Utility.getSavedData(MapsActivity.this, Utility.BASE_MAP);
        if(Utility.isEmptyString(baseMap)) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else {
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

        /*Calendar calendarNow = Calendar.getInstance(Locale.US);
        if (calendarNow.before(getNoonStartTime())) {
            Utility.setStyleMap(this, mMap, R.string.style_label_retro);
        } else if (calendarNow.before(getEveningStartTime())) {
            Utility.setStyleMap(this, mMap, R.string.style_label_default);
        } else {
            Utility.setStyleMap(this, mMap, R.string.style_label_night);
        }*/
    }

//------------------------------------------------------- On Map Ready ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        setBaseMap();
        updateSaveUndoUi();

        if(isFormDataNotSync()){
            Utility.SyncYourDataAlert(this);
        }


//------------------------------------------------------- On Map Click Listener ---------------------------------------------------------------------------------------------------------------------------------

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

               // drawLayersOnMap(latLng);

//                if (isMeasurementModeON && Utility.isEmptyString(selectedFeature)) {
//                    Utility.showToast(mActivity, "Select measuring tool", txtDistance);
//                    return;
//                }
//
//                // Walking mode!
//                if (isMeasurementModeON && isWalkingTool) {
//                    Utility.showToast(mActivity, "Walking measuring tool is recording..", txtDistance);
//                    return;
//                }
//

//---------------------------------------------------------- On Map Click Camera Point Option------------------------------------------------------------
                // Camera Mode!
                if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))){

//                    if(!isUserInsideBoundary(latLng)) {
//                        Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
//                            @Override
//                            public void OkClick(DialogInterface dialog) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                    else{

                        if (cameraPointCounter < 1) {
                            cameraPointCounter++;
                            MarkerOptions cameraPointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.BitmapFromVector(mActivity,R.drawable.icon_camera_marker));
                            cameraPointMarker = mMap.addMarker(cameraPointMarkerOptions);
                            cameraPointMarker.setTag(getResources().getString(R.string.cameraPoint));
                        }
                        updateSaveUndoUi();
                    //}

                }

//---------------------------------------------------------- On Map Click Point Option------------------------------------------------------------
                // Point mode!
                if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))){

                    if(!isUserInsideBoundary(latLng)) {
                        Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                            @Override
                            public void OkClick(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else {
                        //----------------------------
                        // When layer On then!
                        // Point
                        if (pointCounter < 1) {
                            pointCounter++;
                            if (projectLayerModel != null) {
                                String icon = projectLayerModel.getLayerIcon(); //Utility.getSavedData(this,Utility.LAYER_POINT_ICON);
                                if (!icon.equals("")) {
                                    String w = projectLayerModel.getLayerIconWidth(); //(Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_WIDTH));
                                    String h = projectLayerModel.getLayerIconHeight(); //(Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_HEIGHT));
                                    int width = w.equals("") ? 50 : Integer.parseInt(w);
                                    int height = h.equals("") ? 50 : Integer.parseInt(h);

                                    try {
                                        Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(icon), width, height, true);
                                        MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(bmp));
                                        pointMarker = mMap.addMarker(pointMarkerOptions);
                                        assert pointMarker != null;
                                        pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                                    }
                                    catch (Exception e) {
                                        MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                        pointMarker = mMap.addMarker(pointMarkerOptions);
                                        assert pointMarker != null;
                                        pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                                    }
                                }
                                else {
                                    MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                    pointMarker = mMap.addMarker(pointMarkerOptions);
                                    assert pointMarker != null;
                                    pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                                }
                            } else {
                                MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                pointMarker = mMap.addMarker(pointMarkerOptions);
                                assert pointMarker != null;
                                //   pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                            }

                        }
                        updateSaveUndoUi();
                    }
                }

//---------------------------------------------------------- On Map Click Line Option------------------------------------------------------------
                // Line Mode!
                else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {

                    if(!isUserInsideBoundary(latLng)) {
                        Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                            @Override
                            public void OkClick(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else {

                        // Logic of adding centre marker on map will start when there's already one marker in polyline
                        // When a new latlong is trying to add to polyline
                        // calculate middle latlong
                        // Add middle latlong in List of that Polyline
                        // After that add New Latlong in List of that Polyline
                        // Now when going to calculate distance in between 2 latlong then update centre marker's Icon with updated distance text
                        // On Start Drag > Copy middle marker as temp. And Remove existing middle markers from list.
                        // Now on Dragging > fetch new middle latlong and set it to temp middle marker.
                        // On Drag end > Copy latlong from temp middle markers
                        // remove temp markers
                        // and add new middle marker with previously copied temp middle marlkers' latlong
                        // and set them as part of list.
                        //------ FIND CENTRE LAT-LONG ————————————
                        if (listLinesLatLngs.size() > 0) {
                            LatLng pos1 = listLinesLatLngs.get(listLinesLatLngs.size() - 1);
                            LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, latLng);
                            Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                            markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                            listLinesLatLngs.add(centrePosition);
                            listLinesMarkers.add(markerMiddle);
                        }
                        //-------------------------------
                        listLinesLatLngs.add(latLng);
                        Marker marker = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                        marker.setTag(latLng.latitude + ", " + latLng.longitude);
                        listLinesMarkers.add(marker);

                        if (listLinesMarkers.size() > 0) {
                            listLinesMarkers.get(0).setDraggable(listLinesMarkers.size() > 1);
                        }

                        if (mPolyline != null) {
                            mPolyline.setPoints(listLinesLatLngs);
                        } else {

//                if(projectLayerModel != null){
//                    PolylineOptions rectOptions = new PolylineOptions()
//                            .clickable(true)
//                            .color(Color.parseColor(projectLayerModel.getLayerLineColor()))
//                            .width(SystemUtility.getLineSize(mActivity))
//                            .startCap(new RoundCap())
//                            .endCap(new RoundCap())
//                            .jointType(JointType.ROUND)
//                            .addAll(listLinesLatLngs)
//                            .geodesic(true);
//                    mPolyline = mMap.addPolyline(rectOptions);
//                }
//                else{

//                PolylineOptions rectOptions = new PolylineOptions()
//                        .clickable(true)
//                        .color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)))
//                        .width(SystemUtility.getLineSize(mActivity))
//                        .startCap(new RoundCap())
//                        .endCap(new RoundCap())
//                        .jointType(JointType.ROUND)
//                        .addAll(listLinesLatLngs)
//                        .geodesic(true);

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.clickable(false);
                            polylineOptions.color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)));
                            polylineOptions.width(SystemUtility.getLineSize(mActivity));
                            String line_type = Utility.getSavedData(mActivity, Utility.LAYER_LINE_TYPE);
                            if (!Utility.isEmptyString(line_type)) {

                                switch (line_type) {

                                    case LAYER_LINE_TYPE.Normal:
                                        polylineOptions.startCap(new RoundCap());
                                        polylineOptions.endCap(new RoundCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                        break;

                                    case LAYER_LINE_TYPE.SmallDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;

                                    case LAYER_LINE_TYPE.LargeDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;
                                }
                            }
                            // Line Type is Empty then
                            else {
                                polylineOptions.startCap(new RoundCap());
                                polylineOptions.endCap(new RoundCap());
                                polylineOptions.jointType(JointType.ROUND);
                            }
                            polylineOptions.addAll(listLinesLatLngs);
                            polylineOptions.geodesic(true);

                            mPolyline = mMap.addPolyline(polylineOptions);

                        }

//            Log.e(TAG, "Add markers, size: " + listLinesLatLngs.size());
//            for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                Log.e(TAG, "" + listLinesLatLngs.get(i));
//            }
                        undoListLineLatLongs.add(SystemUtility.getClonedListLatLng(listLinesLatLngs));
                        redoListLineLatLongs = SystemUtility.getClonedList(undoListLineLatLongs);
                        updateDistanceBetweenLines(listLinesLatLngs.size() - 1, listLinesLatLngs);
                        updateSaveUndoUi();
                    }
                }

//---------------------------------------------------------- On Map Click Polygon Option------------------------------------------------------------
                // Polygon mode!
                else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {

                    if(!isUserInsideBoundary(latLng)) {
                        Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                            @Override
                            public void OkClick(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else
                    {
                        //  Log.e(TAG,"drawLayersOnMap Polygon");
                        //------ FIND CENTRE LAT-LONG ————————————
                        if (listPolygonLatLongs.size() > 0) {

                            if (listPolygonLatLongs.size() == 1) {
                                // 2nd and 3rd marker in the list
                                LatLng pos1 = listPolygonLatLongs.get(listPolygonLatLongs.size() - 1);
                                LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, latLng);

                                Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                                markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                                listPolygonLatLongs.add(centrePosition);
                                listPolygonMarkers.add(markerMiddle);

                                listPolygonLatLongs.add(latLng);
                                Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                                marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                                listPolygonMarkers.add(marker2);
                            }
                            else {
                                // When >= 3 points
                                if (listPolygonLatLongs.size() >= 6) {// Remove Last Middle Marker & Position
                                    listPolygonMarkers.get(listPolygonLatLongs.size() - 1).remove();
                                    listPolygonMarkers.remove(listPolygonLatLongs.size() - 1);
                                    listPolygonLatLongs.remove(listPolygonLatLongs.size() - 1);
                                }
                                LatLng lastPos = listPolygonLatLongs.get(listPolygonLatLongs.size() - 1);
                                LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(lastPos, latLng);

                                LatLng firstPos = listPolygonLatLongs.get(0);
                                LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(firstPos, latLng);

                                Marker markerMiddle1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition1, "", mActivity);
                                markerMiddle1.setTag(centrePosition1.latitude + ", " + centrePosition1.longitude);
                                listPolygonLatLongs.add(centrePosition1);
                                listPolygonMarkers.add(markerMiddle1);

                                listPolygonLatLongs.add(latLng);
                                Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                                marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                                listPolygonMarkers.add(marker2);

                                Marker markerMiddle2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, "", mActivity);
                                markerMiddle2.setTag(centrePosition2.latitude + ", " + centrePosition2.longitude);
                                listPolygonLatLongs.add(centrePosition2);
                                listPolygonMarkers.add(markerMiddle2);
                            }

                        }
                        else {   // First Marker in the list
                            listPolygonLatLongs.add(latLng);
                            Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                            marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                            listPolygonMarkers.add(marker2);
                        }
                        //-------------------------------
                        if (mPolygon != null) {
                            mPolygon.setPoints(listPolygonLatLongs);
                        }
                        else {

                            PolygonOptions rectOptions = new PolygonOptions()
                                    .clickable(false)
                                    .addAll(listPolygonLatLongs)
                                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                            mPolygon = mMap.addPolygon(rectOptions);
                        }
                        undoListPolygonLatLongs.add(SystemUtility.getClonedListLatLng(listPolygonLatLongs));
                        redoListPolygonLatLongs = SystemUtility.getClonedList(undoListPolygonLatLongs);
                        updateDistanceBetweenPolygonPoints(listPolygonLatLongs.size() - 2, listPolygonLatLongs);
                        updateSaveUndoUi();
                    }

                }
            }
        });

//------------------------------------------------------- On Marker Drag Listener ---------------------------------------------------------------------------------------------------------------------------------

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

//------------------------------------------------------- On Marker Drag Start ------------------------------------------------------------------------------------------------------------------------
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
//                >> Add new marker at same position
//                >> Add new Polyline in between current new marker and prev marker position
                isDraggingMarker = true;

//------------------------------------------------------- On Line Marker Drag------------------------------------------------------------------------------------------------------------------------

                if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                    if (listLinesMarkers.size() >= 3 && listLinesMarkers.contains(marker)) {
                        int indexCurrentMarker = listLinesMarkers.indexOf(marker);
                        LatLng currentLatLng = listLinesLatLngs.get(indexCurrentMarker);

                        if (marker.getTag() != null && marker.getTag() instanceof String && !((String) marker.getTag()).contains(",")) {
                            // Middle(Distance) Marker is dragging
                            marker = SystemUtility.updateToBigMarkerOnMap(marker, mActivity);

                            Marker prevMarker = listLinesMarkers.get(indexCurrentMarker - 1);
                            Marker nextMarker = listLinesMarkers.get(indexCurrentMarker + 1);

                            LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                            LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());

                            String display_distance1 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                            String display_distance2 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());

                            tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition1, display_distance1, mActivity);
                            tempMiddleMarker1.setTag(display_distance1);

                            tempMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, display_distance2, mActivity);
                            tempMiddleMarker2.setTag(display_distance2);

                        } else {
                            // Regular Marker is dragging
                            Marker newMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, currentLatLng);
                            newMarkerTemp.setTag(newMarkerTemp.getPosition().latitude + ", " + newMarkerTemp.getPosition().longitude);

                            if (indexCurrentMarker > 0 && indexCurrentMarker == listLinesMarkers.size() - 1) {
                                // LAST POINT
                                //======== COPYING OLD MIDDLE MARKER AS TEMP AND REMOVING PREV MIDDLE MARKER FROM LINE ==================
                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, listLinesLatLngs.get(indexCurrentMarker - 1), "", mActivity);
                                tempMiddleMarker1.setTag(tempMiddleMarker1.getPosition().latitude + ", " + tempMiddleMarker1.getPosition().longitude);
                                // -------------------------
                                listLinesMarkers.get(indexCurrentMarker - 1).remove();
                                listLinesMarkers.remove(indexCurrentMarker - 1);
                                listLinesLatLngs.remove(indexCurrentMarker - 1);
                                mPolyline.setPoints(listLinesLatLngs);
                                indexCurrentMarker -= 1;

                               // Log.e(TAG, "On DragStart marker, size: " + listLinesLatLngs.size());
                                for (int i = 0; i < listLinesLatLngs.size(); i++) {
                                    Log.e(TAG, "" + listLinesLatLngs.get(i));
                                }
                                //==========================
                                LatLng prevMarkerLatLng = listLinesLatLngs.get(indexCurrentMarker - 1);

                                Marker PrevMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, prevMarkerLatLng);
                                PrevMarkerTemp.setTag(prevMarkerLatLng.latitude + ", " + prevMarkerLatLng.longitude);

                                listLinesMarkersTemp.add(PrevMarkerTemp);
                                listLinesMarkersTemp.add(newMarkerTemp);
                                listLinesLatLngsTemp.add(prevMarkerLatLng);
                                listLinesLatLngsTemp.add(newMarkerTemp.getPosition());

                            } else if (indexCurrentMarker == 0) {
                                // FIRST POINT
                                //======== REMOVING NEXT MIDDLE MARKER FROM LINE ==================
                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, listLinesLatLngs.get(indexCurrentMarker + 1), "", mActivity);
                                tempMiddleMarker1.setTag(tempMiddleMarker1.getPosition().latitude + ", " + tempMiddleMarker1.getPosition().longitude);
                                // -------------------------
                                listLinesMarkers.get(indexCurrentMarker + 1).remove();
                                listLinesMarkers.remove(indexCurrentMarker + 1);
                                listLinesLatLngs.remove(indexCurrentMarker + 1);
                                mPolyline.setPoints(listLinesLatLngs);
                                //==========================
                                LatLng nextMarkerLatLng = listLinesLatLngs.get(indexCurrentMarker + 1);

                                Marker NextMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, nextMarkerLatLng);
                                NextMarkerTemp.setTag(nextMarkerLatLng.latitude + ", " + nextMarkerLatLng.longitude);

                                listLinesMarkersTemp.add(newMarkerTemp);
                                listLinesMarkersTemp.add(NextMarkerTemp);
                                listLinesLatLngsTemp.add(newMarkerTemp.getPosition());
                                listLinesLatLngsTemp.add(nextMarkerLatLng);
                            } else {
                                // MIDDLE POINT
                                //======== REMOVING PREV & NEXT MIDDLE MARKERS ==================
                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, listLinesLatLngs.get(indexCurrentMarker - 1), "", mActivity);
                                tempMiddleMarker1.setTag(tempMiddleMarker1.getPosition().latitude + ", " + tempMiddleMarker1.getPosition().longitude);
                                // -------------------------
                                listLinesMarkers.get(indexCurrentMarker - 1).remove();
                                listLinesMarkers.remove(indexCurrentMarker - 1);
                                listLinesLatLngs.remove(indexCurrentMarker - 1);
                                indexCurrentMarker -= 1;
                                // -------------------------
                                tempMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, listLinesLatLngs.get(indexCurrentMarker + 1), "", mActivity);
                                tempMiddleMarker2.setTag(tempMiddleMarker2.getPosition().latitude + ", " + tempMiddleMarker2.getPosition().longitude);
                                // -------------------------
                                listLinesMarkers.get(indexCurrentMarker + 1).remove();
                                listLinesMarkers.remove(indexCurrentMarker + 1);
                                listLinesLatLngs.remove(indexCurrentMarker + 1);
                                mPolyline.setPoints(listLinesLatLngs);
                                //==========================
                                LatLng prevMarkerLatLng = listLinesLatLngs.get(indexCurrentMarker - 1);
                                LatLng nextMarkerLatLng = listLinesLatLngs.get(indexCurrentMarker + 1);

                                Marker PrevMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, prevMarkerLatLng);
                                PrevMarkerTemp.setTag(prevMarkerLatLng.latitude + ", " + prevMarkerLatLng.longitude);

                                Marker NextMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, nextMarkerLatLng);
                                NextMarkerTemp.setTag(nextMarkerLatLng.latitude + ", " + nextMarkerLatLng.longitude);

                                listLinesMarkersTemp.add(PrevMarkerTemp);
                                listLinesMarkersTemp.add(newMarkerTemp);
                                listLinesMarkersTemp.add(NextMarkerTemp);
                                listLinesLatLngsTemp.add(prevMarkerLatLng);
                                listLinesLatLngsTemp.add(newMarkerTemp.getPosition());
                                listLinesLatLngsTemp.add(nextMarkerLatLng);
                            }
//                            PolylineOptions rectOptions = new PolylineOptions()
//                                    .clickable(true)
//                                    .color(Color.RED)
//                                    .width(SystemUtility.getLineSize(mActivity))
//                                    .startCap(new RoundCap())
//                                    .endCap(new RoundCap())
//                                    .pattern(Arrays.asList(new Dash(20), new Gap(10)))
//                                    .jointType(JointType.BEVEL)
//                                    .addAll(listLinesLatLngsTemp)
//                                    .geodesic(true);
//                            mPolylineTemp = mMap.addPolyline(rectOptions);

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.clickable(false);
                            //polylineOptions.color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)));
                            polylineOptions.color(Color.RED);
                            polylineOptions.width(SystemUtility.getLineSize(mActivity));
                            String line_type = Utility.getSavedData(mActivity,Utility.LAYER_LINE_TYPE);
                            if(!Utility.isEmptyString(line_type)){

                                switch (line_type) {

                                    case LAYER_LINE_TYPE.Normal:
                                        polylineOptions.startCap(new RoundCap());
                                        polylineOptions.endCap(new RoundCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                        break;

                                    case LAYER_LINE_TYPE.SmallDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;

                                    case LAYER_LINE_TYPE.LargeDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;
                                }
                            }
                            // Line Type is Empty then
                            else{
                                polylineOptions.startCap(new RoundCap());
                                polylineOptions.endCap(new RoundCap());
                                polylineOptions.jointType(JointType.ROUND);
                            }
                            polylineOptions.addAll(listLinesLatLngsTemp);
                            polylineOptions.geodesic(true);

                            mPolylineTemp = mMap.addPolyline(polylineOptions);





                        }
                    }
                }

//------------------------------------------------------- On Polygon Marker Drag------------------------------------------------------------------------------------------------------------------------

                else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
                   // Log.e(TAG,"On Polygon Marker");
                    if (listPolygonMarkers.contains(marker)) {
                        int indexCurrentMarker = listPolygonMarkers.indexOf(marker);
                        LatLng currentLatLng = listPolygonLatLongs.get(indexCurrentMarker);
                       // Log.e(TAG, "currentLatLng indexCurrentMarker" + currentLatLng + ", " + indexCurrentMarker);
                        //===================================================
                        if (listPolygonLatLongs.size() == 3)
                        {
                            if (marker.getTag() != null && marker.getTag() instanceof String && !((String) marker.getTag()).contains(",")) {

                                //Log.e(TAG, "On DragStart marker, size: " + listLinesLatLngs.size());
                                for (int i = 0; i < listPolygonLatLongs.size(); i++) {
                                    Log.e(TAG, "" + listPolygonLatLongs.get(i));
                                }

                                String currentMtag = (String) marker.getTag();
                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, currentLatLng, currentMtag, mActivity);
                                tempMiddleMarker1.setTag(currentMtag);

                                Collections.swap(listPolygonLatLongs, 1, 2);
                                Collections.swap(listPolygonMarkers, 1, 2);
                                mPolygon.setPoints(listPolygonLatLongs);

                                //Log.e(TAG, "On DragStart marker, size: " + listLinesLatLngs.size());
                                for (int i = 0; i < listPolygonLatLongs.size(); i++) {
                                    Log.e(TAG, "" + listPolygonLatLongs.get(i));
                                }
                                indexCurrentMarker = listPolygonMarkers.indexOf(marker);
                                currentLatLng = listPolygonLatLongs.get(indexCurrentMarker);
                                //Log.e(TAG, "currentLatLng indexCurrentMarker" + currentLatLng + ", " + indexCurrentMarker);

                                marker = SystemUtility.updateToBigMarkerOnMap(marker, mActivity);
                                Marker prevMarker = listPolygonMarkers.get(1);
                                Marker nextMarker = listPolygonMarkers.get(0);

                                LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                                LatLng centrePosition3 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());

                                String display_distance2 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                                String display_distance3 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());

                                tempMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, display_distance2, mActivity);
                                tempMiddleMarker2.setTag(display_distance2);

                                tempMiddleMarker3 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition3, display_distance3, mActivity);
                                tempMiddleMarker3.setTag(display_distance3);
                            } else {
                                if (indexCurrentMarker == 0) {
                                    //  FIRST INDEX
                                    listPolygonMarkers.get(indexCurrentMarker + 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker + 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker + 1);

                                    Marker nextMarker = listPolygonMarkers.get(listPolygonMarkers.size() - 1);
                                    LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());
                                    String display_distance1 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());
                                    tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition1, display_distance1, mActivity);
                                    tempMiddleMarker1.setTag(display_distance1);
                                } else if (indexCurrentMarker > 0 && indexCurrentMarker == listPolygonMarkers.size() - 1) {
                                    // LAST INDEX
                                    listPolygonMarkers.get(indexCurrentMarker - 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker - 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker - 1);

                                    Marker prevMarker = listPolygonMarkers.get(0);
                                    LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                                    String display_distance1 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                                    tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition1, display_distance1, mActivity);
                                    tempMiddleMarker1.setTag(display_distance1);
                                }
                            }
                        }
                        else if (listPolygonLatLongs.size() >= 6) {
                            if (marker.getTag() != null &&
                                    marker.getTag() instanceof String &&
                                    !((String) marker.getTag()).contains(",")) {
                                // MIDDLE POINT DRAGGING
                                // Make the draggable Big point
                                // Create 2 temp middle markers in between current to immediate prev and current to immediate next
                                // On drag >> Update both temp middle marker position
                                // On drag end >> add both temp middle markers as polygon list

                                marker = SystemUtility.updateToBigMarkerOnMap(marker, mActivity);
                                indexCurrentMarker = listPolygonMarkers.indexOf(marker);

                                Marker prevMarker, nextMarker;
                                if (indexCurrentMarker == listPolygonMarkers.size() - 1)
                                    nextMarker = listPolygonMarkers.get(0);
                                else
                                    nextMarker = listPolygonMarkers.get(indexCurrentMarker + 1);

                                prevMarker = listPolygonMarkers.get(indexCurrentMarker - 1);

                                LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                                LatLng centrePosition3 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());

                                String display_distance2 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                                String display_distance3 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());

                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, display_distance2, mActivity);
                                tempMiddleMarker1.setTag(display_distance2);

                                tempMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition3, display_distance3, mActivity);
                                tempMiddleMarker2.setTag(display_distance3);
                            } else {
                                // NORMAL POINT DRAGGING
                                Marker prevMarker, nextMarker;
                                if (indexCurrentMarker == 0) {
                                    // FIRST INDEX
                                    listPolygonMarkers.get(indexCurrentMarker + 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker + 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker + 1);

                                    int lastIndex = listPolygonMarkers.size() - 1;
                                    listPolygonMarkers.get(lastIndex).remove();
                                    listPolygonMarkers.remove(lastIndex);
                                    listPolygonLatLongs.remove(lastIndex);

                                    mPolygon.setPoints(listPolygonLatLongs);

                                    indexCurrentMarker = listPolygonMarkers.indexOf(marker);

                                    prevMarker = listPolygonMarkers.get(listPolygonMarkers.size() - 1);
                                    nextMarker = listPolygonMarkers.get(indexCurrentMarker + 1);
                                } else if (indexCurrentMarker > 0 && indexCurrentMarker == listPolygonMarkers.size() - 2) {
                                    // SECOND LAST INDEX
                                    listPolygonMarkers.get(indexCurrentMarker - 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker - 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker - 1);
                                    indexCurrentMarker -= 1;

                                    listPolygonMarkers.get(indexCurrentMarker + 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker + 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker + 1);

                                    mPolygon.setPoints(listPolygonLatLongs);

                                    indexCurrentMarker = listPolygonMarkers.indexOf(marker);

                                    prevMarker = listPolygonMarkers.get(indexCurrentMarker - 1);
                                    nextMarker = listPolygonMarkers.get(0);
                                } else {
                                    // MIDDLE INDEX
                                    listPolygonMarkers.get(indexCurrentMarker - 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker - 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker - 1);
                                    indexCurrentMarker -= 1;

                                    listPolygonMarkers.get(indexCurrentMarker + 1).remove();
                                    listPolygonMarkers.remove(indexCurrentMarker + 1);
                                    listPolygonLatLongs.remove(indexCurrentMarker + 1);

                                    mPolygon.setPoints(listPolygonLatLongs);

                                    indexCurrentMarker = listPolygonMarkers.indexOf(marker);

                                    prevMarker = listPolygonMarkers.get(indexCurrentMarker - 1);
                                    nextMarker = listPolygonMarkers.get(indexCurrentMarker + 1);
                                }
                                LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                                LatLng centrePosition3 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());

                                String display_distance2 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                                String display_distance3 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());

                                tempMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, display_distance2, mActivity);
                                tempMiddleMarker1.setTag(display_distance2);

                                tempMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition3, display_distance3, mActivity);
                                tempMiddleMarker2.setTag(display_distance3);
                            }
                        }
                    }
                }

//------------------------------------------------------- On Point Marker Drag------------------------------------------------------------------------------------------------------------------------

                else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))) {
                    pointMarker.setPosition(marker.getPosition());
                    LatLng latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                    pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
//                    if(listPointMarker.size() >= 0 && listPointMarker.contains(marker)) {
//                        int indexCurrentMarker = listPointMarker.indexOf(marker);
//                        LatLng currentLatLng = listpoint.get(indexCurrentMarker).getPosition();
//                        circleMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, currentLatLng);
//                        circleMarkerTemp.setTag(circleMarkerTemp.getPosition().latitude + ", " + circleMarkerTemp.getPosition().longitude);
//                        Marker marker1 = listpoint.get(indexCurrentMarker);
//                        marker1.remove();
//                        listpoint.remove(indexCurrentMarker);
//                    }
//                    if(listCirclesMarker.size() >= 0 && listCirclesMarker.contains(marker)) {
//                        int indexCurrentMarker = listCirclesMarker.indexOf(marker);
//                        LatLng currentLatLng = listCircles.get(indexCurrentMarker).getCenter();
//
//                        circleMarkerTemp = SystemUtility.addTempOrangeMarkerToMap(mMap, currentLatLng);
//                        circleMarkerTemp.setTag(circleMarkerTemp.getPosition().latitude + ", " + circleMarkerTemp.getPosition().longitude);
//
//                        circleTemp = mMap.addCircle(new CircleOptions()
//                                .center(currentLatLng)
//                                .radius(RADIUS_CIRCLE_MTR)
//                                .strokeWidth(SystemUtility.getLineSize(mActivity))
//                                .strokeColor(Color.RED)
//                                .fillColor(colorRedTransparent));
//                        Circle vCircle = listCircles.get(indexCurrentMarker);
//                        vCircle.remove();
//                        listCircles.remove(indexCurrentMarker);
//                    }
                }

//------------------------------------------------------- On Camera Point Marker Drag------------------------------------------------------------------------------------------------------------------------

                else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))){
                    cameraPointMarker.setPosition(marker.getPosition());
                    cameraPointMarker.setTag(getResources().getString(R.string.cameraPoint));
                }

//------------------------------------------------------- On My Marker Drag Walking Polygon---------------------------------------------------

                else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))){

                    if(listWalkingPolygonLatLongs.size() > 0){
                        listWalkingPolygonLatLongs.iterator();
                        for(int i=0; i< listWalkingPolygonMarkers.size(); i++){
                            // find the marker point!
                            if(listWalkingPolygonMarkers.get(i).equals(marker)){
                                if(mWalkingPolygon != null){
                                    mWalkingPolygon.remove();
                                    listWalkingPolygonLatLongs.set(i,marker.getPosition());
                                }
                            }
                        }
                        if (mWalkingPolygon != null) {
                            //mWalkingPolygon.setPoints(listWalkingPolygonLatLongs);
                            mWalkingPolygon.remove();
                        }
                            PolygonOptions rectOptions = new PolygonOptions()
                                    .clickable(false)
                                    .addAll(listWalkingPolygonLatLongs)
                                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                            mWalkingPolygon = mMap.addPolygon(rectOptions);

                        calculateTotalDistance(listWalkingPolygonLatLongs);
                    }

                }
//
////------------------------------------------------------- On GeoFence Marker Drag Polygon---------------------------------------------------
//
//                if(marker.getTag() instanceof GeoFenceModel){
//                    moveGeoFence(marker);
//                }


//------------------------------------------------------- On Snap shot------------------------------------------------------------------------------------------------------------------------

                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(@Nullable Bitmap bitmap) {
                        Bitmap bitmapStart = bitmap;
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmapStart.compress(Bitmap.CompressFormat.JPEG, 25, out);
                        decodedBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        projection = mMap.getProjection();
                        cardMagnifyingGlass.setVisibility(View.VISIBLE);
                    }
                });


            }

//------------------------------------------------------- On Marker Drag ------------------------------------------------------------------------------------------------------------------------

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
               // Log.e(TAG,"on Marker Drag");
                isDraggingMarker = true;
                onMyMarkerDrag(marker);
            }

//------------------------------------------------------- On Marker Drag End------------------------------------------------------------------------------------------------------------------------
            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {

                if (!selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))) {
                    if(!isUserInsideBoundary(marker.getPosition())) {
                        Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                            @Override
                            public void OkClick(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                }

                //Log.e(TAG,"on Marker Drag End");
                onMyMarkerDrag(marker);
                cardMagnifyingGlass.setVisibility(View.GONE);

//------------------------------------------------------- On Line Marker Drag End ---------------------------------------------------------------------------------------------------

                    if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                        removeTempPolyLine();
                        //================================
//                    Log.e(TAG, "Before end drag size: " + listLinesLatLngs.size());
//                    for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                        Log.e(TAG, "" + listLinesLatLngs.get(i));
//                    }
                        int indexCurrentMarker = listLinesMarkers.indexOf(marker);
                        //Log.e(TAG, "indexCurrentMarker " + indexCurrentMarker);
                        if (indexCurrentMarker > 0 &&
                                indexCurrentMarker == listLinesMarkers.size() - 1) {
                            // LAST POINT
                            if (tempMiddleMarker1 != null && tempMiddleMarker1.isVisible()) {
                                LatLng latLngNewMiddlePoint = tempMiddleMarker1.getPosition();
                                String displayDistance = (String) tempMiddleMarker1.getTag();
                                tempMiddleMarker1.remove();
                                tempMiddleMarker1 = null;
                                //Log.e(TAG, "Removed temp Middle Marker");
                                Marker updatedMiddleMarker = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint, displayDistance, mActivity);
                                updatedMiddleMarker.setTag(displayDistance);
                                listLinesMarkers.add(indexCurrentMarker, updatedMiddleMarker);
                                listLinesLatLngs.add(indexCurrentMarker, updatedMiddleMarker.getPosition());
                                mPolyline.setPoints(listLinesLatLngs);
//                            Log.e(TAG, "After end drag size: " + listLinesLatLngs.size());
//                            for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                                Log.e(TAG, "" + listLinesLatLngs.get(i));
//                            }
                            }
                        } else if (indexCurrentMarker == 0) {
                            // FIRST POINT
                            if (tempMiddleMarker1 != null && tempMiddleMarker1.isVisible()) {
                                LatLng latLngNewMiddlePoint = tempMiddleMarker1.getPosition();
                                String displayDistance = (String) tempMiddleMarker1.getTag();
                                tempMiddleMarker1.remove();
                                tempMiddleMarker1 = null;
                                Log.e(TAG, "Removed temp Middle Marker");
                                Marker updatedMiddleMarker = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint, displayDistance, mActivity);
                                updatedMiddleMarker.setTag(displayDistance);
                                listLinesMarkers.add(indexCurrentMarker + 1, updatedMiddleMarker);
                                listLinesLatLngs.add(indexCurrentMarker + 1, updatedMiddleMarker.getPosition());
                                mPolyline.setPoints(listLinesLatLngs);
//                            Log.e(TAG, "After end drag size: " + listLinesLatLngs.size());
//                            for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                                Log.e(TAG, "" + listLinesLatLngs.get(i));
//                            }
                            }
                        } else {
                            // MIDDLE POINT
                            if (tempMiddleMarker1 != null && tempMiddleMarker1.isVisible() &&
                                    tempMiddleMarker2 != null && tempMiddleMarker2.isVisible()) {
                                LatLng latLngNewMiddlePoint1 = tempMiddleMarker1.getPosition();
                                String displayDistance1 = (String) tempMiddleMarker1.getTag();
                                tempMiddleMarker1.remove();
                                tempMiddleMarker1 = null;
                                LatLng latLngNewMiddlePoint2 = tempMiddleMarker2.getPosition();
                                String displayDistance2 = (String) tempMiddleMarker2.getTag();
                                tempMiddleMarker2.remove();
                                tempMiddleMarker2 = null;
                                // Log.e(TAG, "Removed temp Middle Markers");
                                Marker updatedMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint1, displayDistance1, mActivity);
                                updatedMiddleMarker1.setTag(displayDistance1);

                                Marker updatedMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint2, displayDistance2, mActivity);
                                updatedMiddleMarker2.setTag(displayDistance2);

                                listLinesMarkers.add(indexCurrentMarker, updatedMiddleMarker1);
                                listLinesMarkers.add(indexCurrentMarker + 2, updatedMiddleMarker2);
                                listLinesLatLngs.add(indexCurrentMarker, updatedMiddleMarker1.getPosition());
                                listLinesLatLngs.add(indexCurrentMarker + 2, updatedMiddleMarker2.getPosition());
                                mPolyline.setPoints(listLinesLatLngs);
//                            Log.e(TAG, "After end drag size: " + listLinesLatLngs.size());
//                            for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                                Log.e(TAG, "" + listLinesLatLngs.get(i));
//                            }
                            }
                        }
                        //================================
                        undoListLineLatLongs.add(SystemUtility.getClonedListLatLng(listLinesLatLngs));
                        redoListLineLatLongs = SystemUtility.getClonedList(undoListLineLatLongs);
                        calculateTotalDistance(listLinesLatLngs);
                    }

//------------------------------------------------------- On Polygon Marker Drag End------------------------------------------------------------------------------------------------------------------------

                    else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
                        removeTempPolyLine();
                        //---------------------------------
                        if (listPolygonLatLongs.size() == 2) {   // FIRST or LAST BIG MARKER IS DRAGGING
                            LatLng latLngNewMiddlePoint = tempMiddleMarker1.getPosition();
                            String displayDistance = (String) tempMiddleMarker1.getTag();
                            tempMiddleMarker1.remove();
                            tempMiddleMarker1 = null;
                            // Log.e(TAG, "Removed temp Middle Marker");
                            Marker updatedMiddleMarker = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint, displayDistance, mActivity);
                            updatedMiddleMarker.setTag(displayDistance);
                            listPolygonMarkers.add(1, updatedMiddleMarker);
                            listPolygonLatLongs.add(1, updatedMiddleMarker.getPosition());
                            mPolygon.setPoints(listPolygonLatLongs);
                        } else if (listPolygonLatLongs.size() == 3) {   // MIDDLE MARKER IS DRAGGING

                            LatLng latLngNewMiddlePoint1 = tempMiddleMarker1.getPosition();
                            String displayDistance1 = (String) tempMiddleMarker1.getTag();
                            tempMiddleMarker1.remove();
                            tempMiddleMarker1 = null;

                            LatLng latLngNewMiddlePoint2 = tempMiddleMarker2.getPosition();
                            String displayDistance2 = (String) tempMiddleMarker2.getTag();
                            tempMiddleMarker2.remove();
                            tempMiddleMarker2 = null;

                            LatLng latLngNewMiddlePoint3 = tempMiddleMarker3.getPosition();
                            String displayDistance3 = (String) tempMiddleMarker3.getTag();
                            tempMiddleMarker3.remove();
                            tempMiddleMarker3 = null;

                            //Log.e(TAG, "Removed temp Middle Marker");
                            Marker updatedMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint1, displayDistance1, mActivity);
                            updatedMiddleMarker1.setTag(displayDistance1);

                            Marker updatedMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint2, displayDistance2, mActivity);
                            updatedMiddleMarker2.setTag(displayDistance2);

                            Marker updatedMiddleMarker3 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint3, displayDistance3, mActivity);
                            updatedMiddleMarker3.setTag(displayDistance3);

                            listPolygonMarkers.add(1, updatedMiddleMarker1);
                            listPolygonMarkers.add(3, updatedMiddleMarker2);
                            listPolygonMarkers.add(5, updatedMiddleMarker3);
                            listPolygonLatLongs.add(1, updatedMiddleMarker1.getPosition());
                            listPolygonLatLongs.add(3, updatedMiddleMarker2.getPosition());
                            listPolygonLatLongs.add(5, updatedMiddleMarker3.getPosition());
                            mPolygon.setPoints(listPolygonLatLongs);
                        } else if (listPolygonLatLongs.size() > 3) {
                            int indexCurrentMarker = listPolygonMarkers.indexOf(marker);

                            LatLng latLngNewMiddlePoint1 = tempMiddleMarker1.getPosition();
                            String displayDistance1 = (String) tempMiddleMarker1.getTag();
                            tempMiddleMarker1.remove();
                            tempMiddleMarker1 = null;

                            LatLng latLngNewMiddlePoint2 = tempMiddleMarker2.getPosition();
                            String displayDistance2 = (String) tempMiddleMarker2.getTag();
                            tempMiddleMarker2.remove();
                            tempMiddleMarker2 = null;

                            // Log.e(TAG, "Removed temp Middle Marker");
                            Marker updatedMiddleMarker1 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint1, displayDistance1, mActivity);
                            updatedMiddleMarker1.setTag(displayDistance1);

                            Marker updatedMiddleMarker2 = SystemUtility.addSmallMarkerToMap(mMap, latLngNewMiddlePoint2, displayDistance2, mActivity);
                            updatedMiddleMarker2.setTag(displayDistance2);

                            if (indexCurrentMarker == 0) {
                                listPolygonMarkers.add(indexCurrentMarker + 1, updatedMiddleMarker1);
                                listPolygonMarkers.add(updatedMiddleMarker2);
                                listPolygonLatLongs.add(indexCurrentMarker + 1, updatedMiddleMarker1.getPosition());
                                listPolygonLatLongs.add(updatedMiddleMarker2.getPosition());
                            } else if (indexCurrentMarker == listPolygonMarkers.size() - 1) {
                                listPolygonMarkers.add(indexCurrentMarker, updatedMiddleMarker1);
                                listPolygonMarkers.add(indexCurrentMarker + 2, updatedMiddleMarker2);
                                listPolygonLatLongs.add(indexCurrentMarker, updatedMiddleMarker1.getPosition());
                                listPolygonLatLongs.add(indexCurrentMarker + 2, updatedMiddleMarker2.getPosition());
                            } else {
                                listPolygonMarkers.add(indexCurrentMarker, updatedMiddleMarker1);
                                listPolygonMarkers.add(indexCurrentMarker + 2, updatedMiddleMarker2);
                                listPolygonLatLongs.add(indexCurrentMarker, updatedMiddleMarker1.getPosition());
                                listPolygonLatLongs.add(indexCurrentMarker + 2, updatedMiddleMarker2.getPosition());
                            }
                            mPolygon.setPoints(listPolygonLatLongs);
                        }
                        //---------------------------------
                        undoListPolygonLatLongs.add(SystemUtility.getClonedListLatLng(listPolygonLatLongs));
                        redoListPolygonLatLongs = SystemUtility.getClonedList(undoListPolygonLatLongs);
                        mPolygon.setStrokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)));
                        mPolygon.setFillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                        calculateTotalDistance(listPolygonLatLongs);

                    }

//------------------------------------------------------ On Point Marker Drag End------------------------------------------------------------------------------------------------------------------------

                    else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))) {
                        pointMarker.setPosition(marker.getPosition());
                        LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                        pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
//                    removeCircle();
//                    if (listCirclesMarker.contains(marker)) {
//                        int index = -1;
//                        for (int i = 0; i < listCirclesMarker.size(); i++) {
//                            Marker marker2 = listCirclesMarker.get(i);
//                            if (marker2.getId().equalsIgnoreCase(marker.getId())) {
//                                index = i;
//                                Log.e(TAG, "INDEX " + index);
//                                break;
//                            }
//                        }
//                        if (index > -1) {
//                            marker.setTag(marker.getPosition().latitude + ", " + marker.getPosition().longitude);
//                            listCirclesMarker.set(index, marker);
//                            listCircles.add(index, mMap.addCircle(new CircleOptions()
//                                    .center(marker.getPosition())
//                                    .radius(RADIUS_CIRCLE_MTR)
//                                    .strokeWidth(SystemUtility.getLineSize(mActivity))
//                                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
//                                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)))));
//                        }
//                    }
////                    ----------------------------------
//                    ArrayList<LatLng> listPointsLatLngsCircle = new ArrayList<>();
//                    for (Circle circle : listCircles) {
//                        listPointsLatLngsCircle.add(circle.getCenter());
//                    }
//                    undoListPointLatLongs.add(SystemUtility.getClonedListLatLng(listPointsLatLngsCircle));
//                    redoListPointLatLongs = SystemUtility.getClonedList(undoListPointLatLongs);
                    }

//------------------------------------------------------- On Camera Point Marker Drag End---------------------------------------------------

                    else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))) {
                        cameraPointMarker.setPosition(marker.getPosition());
                        cameraPointMarker.setTag(getResources().getString(R.string.cameraPoint));
                    }


                updateSaveUndoUi();
                isDraggingMarker = false;
            }
        });

//------------------------------------------------------- On Camera Idle ------------------------------------------------------------

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (Utility.getBooleanSavedData(mActivity, Utility.IS_PANEL_HIDE)) {
                   // startTimerUiInActive();
                }
                if (!isAutomatedMapMoved) {
                   // Log.e(TAG, "onCameraIdle " + mMap.getCameraPosition().target.latitude);
                   // Log.e(TAG, "onCameraIdle " + mMap.getCameraPosition().target.longitude);
                    centreLatLng = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                    if (Utility.getBooleanSavedData(mActivity, Utility.IS_REALIGN_MAP_CURSOR)) {
                        //startTimer();
                    }
                } else
                    isAutomatedMapMoved = false;
            }
        });

//------------------------------------------------------- On Camera Move Started ------------------------------------------------------------

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
               // stopTimer();
              //  stopTimerUiInActive();
               // setActiveUI(true);
            }
        });

//      ============ Initiating drawing layers from Project server data ============
        initExtra();
//      ========================================================================
    }
//
//    private void moveGeoFence(Marker marker){
//        if(markerGeoFence != null){
//            if(geoFence.size() > 0) {
//                LatLng prevMarkerLatLng = markerGeoFence.getPosition();
//                LatLng currentMarkerLatLng = marker.getPosition();
//                String distance = getDistanceBetween2Points(prevMarkerLatLng, currentMarkerLatLng);
//                for(int i = 0; i < geoFence.size(); i++) {
//                    LatLng newLatLng = new LatLng(geoFence.get(i).latitude + Double.parseDouble(distance), geoFence.get(i).longitude + Double.parseDouble(distance));
//                    geoFence.set(i, newLatLng);
//                }
//                markerGeoFence.setPosition(marker.getPosition());
//                polygonGeoFence.setPoints(geoFence);
//            }
//        }
//    }


//------------------------------------------------------- On My Marker Drag------------------------------------------------------------------------------------------------------------------------

    private void onMyMarkerDrag(Marker marker) {

//------------------------------------------------------- On My Marker Drag Projection---------------------------------------------------

        if (projection != null) {
            Point screenPosition = projection.toScreenLocation(marker.getPosition());
            if (decodedBitmap != null) {
                // x + width must be <= bitmap.width()
                //Log.e("CALC", "Origin Bitmap: width: " + decodedBitmap.getWidth() + "height: " + decodedBitmap.getHeight());
                int pointX = (screenPosition.x - DEFAULT_WIDTH_BITMAP),
                        pointY = (screenPosition.y - DEFAULT_HEIGHT_BITMAP),
                        newBmpWidth = MAX_WIDTH_MAG_BITMAP,
                        newBmpHeight = MAX_HEIGHT_MAG_BITMAP;
                if (screenPosition.x - DEFAULT_WIDTH_BITMAP < 0) {
                    //Log.e("CALC", "Screenposition X is < 0 ");
                    for (int i = (DEFAULT_WIDTH_BITMAP - 1); i > 0; i--) {
                        if (screenPosition.x - i > 0) {
                            pointX = screenPosition.x - i;
                            //Log.e("CALC", " UPDATED Point X = " + pointX);
                            break;
                        }
                    }
                }
                //Log.e("CALC", "FINAL Point X = " + pointX);
                if (screenPosition.y - DEFAULT_HEIGHT_BITMAP < 0) {
                   // Log.e("CALC", "Screenposition Y is < 0 ");
                    for (int i = (DEFAULT_HEIGHT_BITMAP - 1); i > 0; i--) {
                        if (screenPosition.y - i > 0) {
                            pointY = screenPosition.y - i;
                           // Log.e("CALC", " UPDATED Point Y = " + pointY);
                            break;
                        }
                    }
                }
                //Log.e("CALC", "FINAL Point Y = " + pointY);
                int tempNewMaxBmpWidth = pointX + MAX_WIDTH_MAG_BITMAP;
                //Log.e("CALC", "Default tempNewMaxBmpWidth = " + tempNewMaxBmpWidth);
                if (decodedBitmap.getWidth() < tempNewMaxBmpWidth) {
                   // Log.e("CALC", "PointX + 100 < 0 ");
                    for (int i = MAX_WIDTH_MAG_BITMAP; i > 0; i--) {
                        tempNewMaxBmpWidth = pointX + i;
                        if (decodedBitmap.getWidth() > tempNewMaxBmpWidth) {
                            newBmpWidth = i;
                            break;
                        }
                    }
                }
              //  Log.e("CALC", "newBmpWidth = " + newBmpWidth);
                int tempNewMaxBmpHeight = pointY + MAX_HEIGHT_MAG_BITMAP;
                //Log.e("CALC", "Default tempNewMaxBmpHeight = " + tempNewMaxBmpHeight);
                if (decodedBitmap.getHeight() < tempNewMaxBmpHeight) {
                    //Log.e("CALC", "PointY + 100 < 0 ");
                    for (int i = MAX_HEIGHT_MAG_BITMAP; i > 0; i--) {
                        tempNewMaxBmpHeight = pointY + i;
                        if (decodedBitmap.getHeight() > tempNewMaxBmpHeight) {
                            newBmpHeight = i;
                            break;
                        }
                    }
                }
                //Log.e("CALC", "newBmpHeight = " + newBmpHeight);
                if (pointX > 0 && pointY > 0 && newBmpHeight > 0 && newBmpWidth > 0 && (pointX + newBmpWidth) <= decodedBitmap.getWidth() && (pointY + newBmpHeight) <= decodedBitmap.getHeight())
                    imgMagnifyingGlass.setImageBitmap(Bitmap.createBitmap(decodedBitmap, pointX, pointY, newBmpWidth, newBmpHeight));
            }
        }

//------------------------------------------------------- On My Marker Drag Line---------------------------------------------------

        if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
            if (listLinesMarkers.contains(marker)) {
                int index = -1;
                for (int i = 0; i < listLinesMarkers.size(); i++) {
                    Marker marker1 = listLinesMarkers.get(i);
                    if (marker1.getId().equalsIgnoreCase(marker.getId())) {
                        index = i;
                       // Log.e(TAG, "INDEX " + index);
                        break;
                    }
                }
                if (index > -1) {
                    //=====================================
                    // change centr latlong here
                    if (index > 0 && index == listLinesMarkers.size() - 1) {
                        // LAST POINT
                        Marker prevMarker = listLinesMarkers.get(index - 1);
                        LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                        String display_distance = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker1 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker1, centrePosition, display_distance, mActivity);
                    } else if (index == 0) {
                        // FIRST POINT
                        Marker nextMarker = listLinesMarkers.get(index + 1);
                        LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());
                        String display_distance = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker1 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker1, centrePosition, display_distance, mActivity);
                    } else {   // MIDDLE POINT
                        Marker prevMarker = listLinesMarkers.get(index - 1);
                        LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                        String display_distance1 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker1 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker1, centrePosition1, display_distance1, mActivity);

                        Marker nextMarker = listLinesMarkers.get(index + 1);
                        LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());
                        String display_distance2 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker2 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker2, centrePosition2, display_distance2, mActivity);
                    }
                    //=====================================
                    marker.setTag(marker.getPosition().latitude + ", " + marker.getPosition().longitude);
                    listLinesMarkers.set(index, marker);
                    listLinesLatLngs.set(index, marker.getPosition());
                    mPolyline.setPoints(listLinesLatLngs);
                }
            }
        }

//------------------------------------------------------- On My Marker Drag Polygon---------------------------------------------------

        else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
            if (listPolygonMarkers.contains(marker)) {
                int index = -1;
                for (int i = 0; i < listPolygonMarkers.size(); i++) {
                    Marker marker2 = listPolygonMarkers.get(i);
                    if (marker2.getId().equalsIgnoreCase(marker.getId())) {
                        index = i;
                       // Log.e(TAG, "INDEX " + index);
                        break;
                    }
                }
                if (index > -1) {
                    if (listPolygonMarkers.size() == 2) {// Single Line and Big vertex is dragging
                        Marker marker1;
                        if (index == 0) {
                            marker1 = listPolygonMarkers.get(listPolygonMarkers.size() - 1);
                        } else {
                            marker1 = listPolygonMarkers.get(0);
                        }
                        LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(marker1.getPosition(), marker.getPosition());
                        String display_distance = getDistanceBetween2Points(marker1.getPosition(), marker.getPosition());
                        tempMiddleMarker1 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker1, centrePosition, display_distance, mActivity);
                    }
                    else if (listPolygonMarkers.size() == 3) {// Single line and Middle point is dragging - 3 new middle points are being added and updated
                        // tempMiddleMarker1 will be as it is.
                        Marker prevMarker = listPolygonMarkers.get(1);
                        LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                        String display_distance2 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker2 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker2, centrePosition2, display_distance2, mActivity);

                        Marker nextMarker = listPolygonMarkers.get(0);
                        LatLng centrePosition3 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());
                        String display_distance3 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());
                        tempMiddleMarker3 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker3, centrePosition3, display_distance3, mActivity);
                    }
                    else if (listPolygonMarkers.size() > 3) {
                        Marker prevMarker, nextMarker;
                        if (index == 0) {
                            prevMarker = listPolygonMarkers.get(index + 1);
                            nextMarker = listPolygonMarkers.get(listPolygonMarkers.size() - 1);
                        } else if (index > 0 && index == listPolygonMarkers.size() - 1) {
                            prevMarker = listPolygonMarkers.get(index - 1);
                            nextMarker = listPolygonMarkers.get(0);
                        } else {
                            prevMarker = listPolygonMarkers.get(index - 1);
                            nextMarker = listPolygonMarkers.get(index + 1);
                        }

                        LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(prevMarker.getPosition(), marker.getPosition());
                        LatLng centrePosition3 = SystemUtility.getCentreLatLngFrom2LatLng(nextMarker.getPosition(), marker.getPosition());

                        String display_distance2 = getDistanceBetween2Points(prevMarker.getPosition(), marker.getPosition());
                        String display_distance3 = getDistanceBetween2Points(nextMarker.getPosition(), marker.getPosition());

                        tempMiddleMarker1 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker1, centrePosition2, display_distance2, mActivity);
                        tempMiddleMarker2 = SystemUtility.updateToSmallMarkerOnMap(tempMiddleMarker2, centrePosition3, display_distance3, mActivity);
                    }
                    listPolygonMarkers.set(index, marker);
                    listPolygonLatLongs.set(index, marker.getPosition());
                    mPolygon.setPoints(listPolygonLatLongs);
                    //mPolygon.setStrokeColor(Color.parseColor(Utility.COLOR_CODE.PINK));
                    //mPolygon.setFillColor(colorFillTransparent);
                    mPolygon.setStrokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)));
                    mPolygon.setFillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                }
            }
        }

//------------------------------------------------------- On My Marker Drag Point---------------------------------------------------

        else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))){
            pointMarker.setPosition(marker.getPosition());
            LatLng latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
            pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
            //pointMarker.setTitle("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
        }

//------------------------------------------------------- On My Marker Drag Camera Point---------------------------------------------------
        // Camera Point
        else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))){
            cameraPointMarker.setPosition(marker.getPosition());
            cameraPointMarker.setTag(getResources().getString(R.string.cameraPoint));
        }

//---------------------------------------------------------- On Map Drag Walking Polygon Option------------------------------------------------------------

        // Walking Polygon mode!
        else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))) {
            if(listWalkingPolygonLatLongs.size() > 0){
                listWalkingPolygonLatLongs.iterator();
                for(int i=0; i< listWalkingPolygonMarkers.size(); i++){
                    // find the marker point!
                    if(listWalkingPolygonMarkers.get(i).equals(marker)){
                        if(mWalkingPolygon != null){
                            mWalkingPolygon.remove();
                            listWalkingPolygonLatLongs.set(i,marker.getPosition());
                        }
                    }
                }
                if (mWalkingPolygon != null) {
                    mWalkingPolygon.remove();
                }
                    PolygonOptions rectOptions = new PolygonOptions()
                            .clickable(false)
                            .addAll(listWalkingPolygonLatLongs)
                            .strokeWidth(SystemUtility.getLineSize(mActivity))
                            .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                            .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                    mWalkingPolygon = mMap.addPolygon(rectOptions);

                calculateTotalDistance(listWalkingPolygonLatLongs);
            }
        }


////------------------------------------------------------- On GeoFence Marker Drag Polygon---------------------------------------------------
//
//        if(marker.getTag() instanceof GeoFenceModel){
//            moveGeoFence(marker);
//        }

    }

//------------------------------------------------------- On Click On Map Button ---------------------------------------------------------------------------------------------------------------------------------

    private View.OnClickListener mapClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {

                case R.id.rlWalkingMinDistance:
                    rlWalkingMinDistance();
                    break;

                case R.id.rlWalkingReset:
                    rlWalkingReset();
                    break;

                case R.id.rlConverterMeasurement:
                    rlConverterMeasurement();
                    break;

                case R.id.rlCamera:
                    // turn on camera point function
                    if(!Utility.getBooleanSavedData(mActivity,Utility.INSIDE_ZONE)){
                        // when user not inside zone!
                        isMapCameraOn = false;
                        Utility.getUserInsideAreaAlertDialogBox(mActivity, DialogInterface::dismiss);
                    }
                    else{
                        isMapCameraOn = true;
                        showConverterMeasurmentButton(false);
                        showrlWalkingResetButton(false);
                        showrlWalkingMinDistanceButton(false);
                        updateLayerWalkingStop(false);
                        updateVisibilityOfLayersSettings(false);
                        updateCheckMarkFloatingButton(true,"Camera Tap On Map");
                        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.siemensBlueColor));
                        toolbar.setTitle("Camera Tap On Map");
                        toolbar.setSubtitle(null);
                        isWalkingTool = false;
                        isPolygonWalkingOn = false;

                        selectedFeature = getResources().getString(R.string.cameraPoint);
                        Toast.makeText(baseApplication, "Tap on Map", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.rlWalkingStop:
                    onLayerWalkingStop();
                    break;

                case R.id.rlCentre:
                    break;
                case R.id.rlUndo:
                    undoAction();
                    updateSaveUndoUi();
                    break;
                case R.id.rlRedo:
                    redoAction();
                    updateSaveUndoUi();
                    break;
                case R.id.rlMapStyle:
                    showMapTypeOptionDialog();
                    break;
                case R.id.imgMyLocation:
                    if (mCurrentLocation != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),20f));
                    }
                    break;
                case R.id.rlMyProjectLocation:
                    if (projectLatLng != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(projectLatLng.latitude, projectLatLng.longitude)));
                    }
                    break;
                case R.id.llMenu:
                    mDrawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.rlZoomIn:
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    break;
                case R.id.rlZoomOut:
                    mMap.animateCamera(CameraUpdateFactory.zoomOut());
                    break;
                case R.id.rlLine:
                    // Save Option On -> Change By Rahul Suthar
                    updateSaveWalking(false);
                    clearDrawing();
                    selectedFeature = getResources().getString(R.string.Line);
                    //toolbar.setTitle("Measuring length");
                    updateMeasuringTool(false);
                    break;
                case R.id.rlPolygon:
                    // Save Option On -> Change By Rahul Suthar
                    updateSaveWalking(false);
                    clearDrawing();
                    selectedFeature = getResources().getString(R.string.Polygone);
                   // toolbar.setTitle("Measuring area");
                    updateMeasuringTool(false);
                    break;
                case R.id.rlWalking:
                    // Remove all Line, Point and polygon data
                    clearDrawing();
                    selectedFeature = getResources().getString(R.string.Line);
                    //toolbar.setTitle("Measuring walk");
                    // Visible Measuring Tool layout
                    updateMeasuringTool(false);
                    isWalkingTool = true;
                    // Save Option On -> Change By Rahul Suthar
                    updateSaveWalking(false);
                    break;

                case R.id.rlSaveWalkingLocation:
                    //Save();
                    //updateMeasuringTool(false);
                    Toast.makeText(MapsActivity.this, "Save", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.rlSaveProjectLayer:
                    if (isDrawing) {
                        // Saving Drawing
                        // If any drawing is exist >> Proceed for save
                        // Else do nothing

                        if(isWalkingTool && isPolygonWalkingOn){
                            Utility.getOKDialogBox(mActivity, "Alert", "Please Stop Polygon Walking Option.Press Stop Button", new Utility.DialogBoxOKClick() {
                                @Override
                                public void OkClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else if(isWalkingTool){
                            Utility.getOKDialogBox(mActivity, "Alert", "Please Stop Line Walking Option.Press Stop Button", new Utility.DialogBoxOKClick() {
                                @Override
                                public void OkClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else{
                            if (projectLayerModel != null) {
                                // Log.e(TAG,""+ projectLayerModel.getLayerType());
                                // Log.e(TAG,""+ new Gson().toJson(mPolyline.getPoints()) );
                                if (projectLayerModel.getLayerType().equalsIgnoreCase(LAYER_TYPE.Polygon) && mWalkingPolygon != null) {
                                    boolean isInside = true;
                                    for(int i=0; i<mWalkingPolygon.getPoints().size(); i++){
                                        if(!isUserInsideBoundary(mWalkingPolygon.getPoints().get(i))) {
                                            Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_FORM_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                                                @Override
                                                public void OkClick(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            isInside = false;
                                            break;
                                        }
                                    }
                                    if(isInside) {
                                        listLatlongLayerClicked.clear();
                                        listLatlongLayerClicked.addAll(mWalkingPolygon.getPoints());
                                        JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
                                        redirectToFormDetails(projectLayerModel, geomArray);
                                    }
                                }
                                else if (projectLayerModel.getLayerType().equalsIgnoreCase(LAYER_TYPE.Polygon) && mPolygon != null) {
                                    boolean isInside = true;
                                    for(int i=0; i<mPolygon.getPoints().size(); i++){
                                        if(!isUserInsideBoundary(mPolygon.getPoints().get(i))) {
                                            Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_FORM_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                                                @Override
                                                public void OkClick(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            isInside = false;
                                            break;
                                        }
                                    }
                                    if(isInside) {
                                        listLatlongLayerClicked.clear();
                                        listLatlongLayerClicked.addAll(mPolygon.getPoints());
                                        JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
                                        redirectToFormDetails(projectLayerModel, geomArray);
                                    }
                                }
                                else if (projectLayerModel.getLayerType().equalsIgnoreCase(LAYER_TYPE.Line) && mPolyline != null) {
                                    boolean isInside = true;
                                    for(int i=0; i<mPolyline.getPoints().size(); i++){
                                        if(!isUserInsideBoundary(mPolyline.getPoints().get(i))) {
                                            Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_FORM_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                                                @Override
                                                public void OkClick(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            isInside = false;
                                            break;
                                        }
                                    }
                                    if(isInside){
                                        listLatlongLayerClicked.clear();
                                        listLatlongLayerClicked.addAll(mPolyline.getPoints());
                                        JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
                                        redirectToFormDetails(projectLayerModel, geomArray);
                                    }
                                }
                                else if (projectLayerModel.getLayerType().equalsIgnoreCase(LAYER_TYPE.Point) && pointMarker != null) {

                                    boolean isInside = true;

                                        if(!isUserInsideBoundary(pointMarker.getPosition())) {
                                            Utility.getOKDialogBox(mActivity, Utility.BOUNDARY_FORM_ERROR_MESSAGE, new Utility.DialogBoxOKClick() {
                                                @Override
                                                public void OkClick(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            isInside = false;

                                        }

                                    if(isInside){
                                        listLatlongLayerClicked.clear();
                                        listLatlongLayerClicked.add(pointMarker.getPosition());
                                        JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
                                        redirectToFormDetails(projectLayerModel, geomArray);
                                    }

                                }
                            }
                        }

//                        if (binLayerProjectSelected != null) {
//                            if (binLayerProjectSelected.getType().equalsIgnoreCase(LAYER_TYPE.Polygon) && mPolygon != null) {
//                                listLatlongLayerClicked.clear();
//                                listLatlongLayerClicked.addAll(mPolygon.getPoints());
//                                JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
//                                redirectToFormDetails(binLayerProjectSelected, geomArray);
//                            } else if (binLayerProjectSelected.getType().equalsIgnoreCase(LAYER_TYPE.Line) && mPolyline != null) {
//                                listLatlongLayerClicked.clear();
//                                listLatlongLayerClicked.addAll(mPolyline.getPoints());
//                                JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
//                                redirectToFormDetails(binLayerProjectSelected, geomArray);
//                            } else if (binLayerProjectSelected.getType().equalsIgnoreCase(LAYER_TYPE.Point) && listCircles.size() > 0) {
//                                listLatlongLayerClicked.clear();
//                                for (Circle circle : listCircles) {
//                                    listLatlongLayerClicked.add(circle.getCenter());
//                                }
//                                JsonArray geomArray = Utility.convertCustomListToJsonArray(listLatlongLayerClicked);
//                                redirectToFormDetails(binLayerProjectSelected, geomArray);
//                            }
//                        }
                    } else if (isMeasurementModeON) {
                        reDirectSetting();
                    } else
                        updateVisibilityOfLayersSettings();
                    break;
            }
        }
    };

//------------------------------------------------------- On Click Slider Bar Button Action ---------------------------------------------------------------------------------------------------------------------------------

    private View.OnClickListener clickSlidebar = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {

                // Sync
                case R.id.llSync:
                    mDrawer.closeDrawer(GravityCompat.START);
                    if(SystemUtility.isInternetConnected(mActivity)) {
                        //startMyDialog("Sync...");
                        //SyncFormData();
                        SyncAllData();
                    }
                    else{
                        Utility.getOKDialogBox(mActivity, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
                    }
                    break;
                // DashBoard
                case R.id.llDashBoard:
                    redirectToDashBoardActivity();
                    break;

                // Compass
                case R.id.llCompass:
                    mDrawer.closeDrawer(GravityCompat.START);
                    redirectToCompassActivity();
                    break;

                // Gps Satellite
                case R.id.llGpsSatellite:
                  //  mDrawer.closeDrawer(GravityCompat.START);
                  //  redirectToGpsSatellite();
                    break;

                case R.id.llSetting:
                    mDrawer.closeDrawer(GravityCompat.START);
                    reDirectSetting();
                    break;

                case R.id.llLogout:
                    mDrawer.closeDrawer(GravityCompat.START);
                    if(SystemPermission.isInternetConnected(mActivity)){
                        onClickLogout();
                    }
                    else{
                        Utility.getOKDialogBox(mActivity, "Connection Error", "Need Internet Connection to Logout", DialogInterface::dismiss);
                    }
                    break;

//                case R.id.llTimeline:
//                    if (!isDrawing) {
//                        mDrawer.closeDrawer(GravityCompat.START);
//                        reDirectTimeLine();
//                    }
//                    break;
//                case R.id.llMySubscription:
//                    if (!isDrawing) {
//                        mDrawer.closeDrawer(GravityCompat.START);
//                        startActivity(new Intent(MapsActivity.this, MySubscription.class));
//                    }
//                    break;
//                case R.id.llProjects:
//                    if (!isDrawing)
//                        redirectToProjectList();
//                    break;
//                case R.id.llLayers:
//                    if (!isDrawing)
//                        redirectToProjectLayersList();
//                    break;
//                case R.id.llMeasuring:
//                    if (isDrawing) {
//                        Utility.showDoubleBtnDialog(mActivity, "Alert!!", "There are unsaved changes. Discard anyway ?", "Yes", "No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case DialogInterface.BUTTON_POSITIVE:
//                                        clearDrawing();
//                                        updateCheckMarkFloatingButton(false, "");
//                                        turnMeasurementMode(true);
//                                        dialog.dismiss();
//                                        break;
//                                    case DialogInterface.BUTTON_NEGATIVE:
//                                        dialog.dismiss();
//                                        break;
//                                }
//                            }
//                        });
//                    } else {
//                        clearDrawing();
//                        turnMeasurementMode(true);
//                    }
//                    break;
//                case R.id.llGpsCompass:
//                    mDrawer.closeDrawer(GravityCompat.START);
//                    redirectToCompassActivity();
//                    break;
//                case R.id.llBTmanager:
//                    break;
            }
        }
    };

//------------------------------------------------------- On Layer Walking Stop ---------------------------------------------------------------------------------------------------------------------------------

    private void onLayerWalkingStop(){

        Utility.getYesNoDialogBox(this, "Alert", "You Want to Stop Line Walking?", new Utility.DialogBoxOKClick() {
            @Override
            public void OkClick(DialogInterface dialog) {
                Toast.makeText(mActivity, "Walking Stop Click on Save Button", Toast.LENGTH_SHORT).show();
                isWalkingTool = false;

                if(isPolygonWalkingOn){
                    if(listWalkingPolygonLatLongs.size() > 1)
                    {
                        LatLng pos1 = listWalkingPolygonLatLongs.get(listWalkingPolygonLatLongs.size() - 1);
                        LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, listWalkingPolygonLatLongs.get(0));
                        Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                        markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                        listWalkingPolygonLatLongs.add(centrePosition);
                        listWalkingPolygonMarkers.add(markerMiddle);
                        if (mWalkingPolygon != null) {
                            mWalkingPolygon.remove();
                        }
                        PolygonOptions rectOptions = new PolygonOptions()
                                .clickable(false)
                                .addAll(listWalkingPolygonLatLongs)
                                .strokeWidth(SystemUtility.getLineSize(mActivity))
                                .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                                .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                        mWalkingPolygon = mMap.addPolygon(rectOptions);
                        calculateTotalDistance(listWalkingPolygonLatLongs);
                    }
                }

                dialog.dismiss();
            }
        });
    }

//------------------------------------------------------- rlConverterMeasurement ---------------------------------------------------------------------------------------------------------------------------------

    private void rlConverterMeasurement(){


        Utility.showConverterMeasuremetDialog(mActivity, new Utility.OnClickMap() {
            @Override
            public void onClickMap(String converterType) {

                Utility.saveData(mActivity, Utility.UNIT_AREA_DATA,converterType);
                calculateTotalDistance(listPolygonLatLongs);
                switch(converterType){
                    case "1":
                        Toast.makeText(mActivity, "SqMeter Mode", Toast.LENGTH_SHORT).show();
                        break;

                    case "2":
                        Toast.makeText(mActivity, "Hectare Mode", Toast.LENGTH_SHORT).show();
                        break;

                    case "5":
                        Toast.makeText(mActivity, "Yard Mode", Toast.LENGTH_SHORT).show();
                        break;

                    case "6":
                        Toast.makeText(mActivity, "Acre Mode", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

//------------------------------------------------------- On Logout ---------------------------------------------------------------------------------------------------------------------------------

    private void onClickLogout() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        // Data Present in Local DataBase
        if(isFormDataNotSync()){
            alertDialog.setMessage("Please Sync your Data for your Safety else your data may be loss.");
            alertDialog.setPositiveButton(getResources().getString(R.string.lblSync), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if(SystemPermission.isInternetConnected(mActivity)){
                        SyncAllData();
                    }
                    else{
                        Utility.getOKDialogBox(mActivity, "Connection Error", "Need Internet Connection to Logout", DialogInterface::dismiss);
                    }
                }
            });
        }
        // Data Not Present In Local DataBase
        else{
            alertDialog.setMessage(getResources().getString(R.string.lblAre_you_sure));
            alertDialog.setPositiveButton(getResources().getString(R.string.lblLogout), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    processLogout();
                }
            });
        }

        alertDialog.setNegativeButton(getResources().getString(R.string.lblCancel), null);
        alertDialog.show();
    }

//------------------------------------------------------- Process LogOut ---------------------------------------------------------------------------------------------------------------------------------

    private void processLogout() {
        String date = Utility.getSavedData(mActivity,Utility.OLD_DATE);
        Utility.clearData(mActivity);
        Utility.saveData(mActivity,Utility.OLD_DATE, date);
        dataBaseHelper.open();
        dataBaseHelper.logout();
        dataBaseHelper.close();
        startActivity(new Intent(mActivity, SplashActivity.class));
    }

//------------------------------------------------------- Redo Action ---------------------------------------------------------------------------------------------------------------------------------

    private void redoAction() {
        if (llFeature.getVisibility() == View.VISIBLE) {
            if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
                if (redoListPolygonLatLongs.size() > 0) {
                    int index = undoListPolygonLatLongs.size() == 0 ? -1 : undoListPolygonLatLongs.size() - 1;
                    if (index + 1 < redoListPolygonLatLongs.size()) {
                        undoListPolygonLatLongs.add(redoListPolygonLatLongs.get((index + 1)));

                        listPolygonLatLongs.clear();
                        for (Marker marker : listPolygonMarkers) {
                            marker.remove();
                        }
                        listPolygonMarkers.clear();
                        if (undoListPolygonLatLongs.size() > 0) {
                            drawPolygonMarkers(undoListPolygonLatLongs.get(undoListPolygonLatLongs.size() - 1));
                        }
                    }
                }
            }
            else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))){
                if (redoListWalkingPolygonLatLongs.size() > 0) {
                    int index = undoListWalkingPolygonLatLongs.size() == 0 ? -1 : undoListWalkingPolygonLatLongs.size() - 1;
                    if (index + 1 < redoListWalkingPolygonLatLongs.size()) {
                        undoListWalkingPolygonLatLongs.add(redoListWalkingPolygonLatLongs.get((index + 1)));

                        listWalkingPolygonLatLongs.clear();
                        for (Marker marker : listWalkingPolygonMarkers) {
                            marker.remove();
                        }
                        listWalkingPolygonMarkers.clear();
                        if (undoListWalkingPolygonLatLongs.size() > 0) {
                            drawWalkingPolygonMarkers(undoListWalkingPolygonLatLongs.get(undoListWalkingPolygonLatLongs.size() - 1));
                        }
                    }
                }
            }
            else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                if (redoListLineLatLongs.size() > 0) {
                    int index = undoListLineLatLongs.size() == 0 ? -1 : undoListLineLatLongs.size() - 1;
                    if (index + 1 < redoListLineLatLongs.size()) {
                        undoListLineLatLongs.add(redoListLineLatLongs.get((index + 1)));

                        listLinesLatLngs.clear();
                        for (Marker marker : listLinesMarkers) {
                            marker.remove();
                        }
                        listLinesMarkers.clear();
                        if (undoListLineLatLongs.size() > 0) {
                            drawLineMarkers(undoListLineLatLongs.get(undoListLineLatLongs.size() - 1));
                        }
                    }
                }
            }
            else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))) {
//                if (redoListPointLatLongs.size() > 0) {
//                    int index = undoListPointLatLongs.size() == 0 ? -1 : undoListPointLatLongs.size() - 1;
//                    if (index + 1 < redoListPointLatLongs.size()) {
//                        undoListPointLatLongs.add(redoListPointLatLongs.get((index + 1)));
//
//                        for (Marker marker : listCirclesMarker) {
//                            marker.remove();
//                        }
//                        listCirclesMarker.clear();
//                        for (Circle circle : listCircles) {
//                            circle.remove();
//                        }
//                        listCircles.clear();
//
//                        if (undoListPointLatLongs.size() > 0) {
//                            drawCirclePoints(undoListPointLatLongs.get(undoListPointLatLongs.size() - 1));
//                        }
//                    }
//                }
            }
        }
    }

//------------------------------------------------------- Undo Action ---------------------------------------------------------------------------------------------------------------------------------

    private void undoAction() {
        if (llFeature.getVisibility() == View.VISIBLE) {

            if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))) {
//                if (undoListPointLatLongs.size() > 0) {
//                    // Clears original list data
//                    // Remove index from Undo list
//                    // Remove markers from Map for existing index
//                    // Add new markers on same positions for new index
//                    undoListPointLatLongs.remove(undoListPointLatLongs.size() - 1);
//
//                    for (Marker marker : listCirclesMarker) {
//                        marker.remove();
//                    }
//                    listCirclesMarker.clear();
//                    for (Circle circle : listCircles) {
//                        circle.remove();
//                    }
//                    listCircles.clear();
//
//                    if (undoListPointLatLongs.size() > 0) {
//                        drawCirclePoints(undoListPointLatLongs.get(undoListPointLatLongs.size() - 1));
//                    }
//                }
            }

            else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
                if (mPolygon != null) {
                    if (undoListPolygonLatLongs.size() > 0) {
                        // Clears original list data
                        // Remove index from Undo list
                        // Remove markers from Map for existing index
                        // Add new markers on same positions for new index
                        listPolygonLatLongs.clear();
                        undoListPolygonLatLongs.remove(undoListPolygonLatLongs.size() - 1);

                        for (Marker marker : listPolygonMarkers) {
                            marker.remove();
                        }
                        listPolygonMarkers.clear();

                        if (undoListPolygonLatLongs.size() > 0) {
                            drawPolygonMarkers(undoListPolygonLatLongs.get(undoListPolygonLatLongs.size() - 1));
                        } else {
                            mPolygon.remove();
                            mPolygon = null;
                        }
                    }
                }
            }
            else if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))){
                if (mWalkingPolygon != null) {
                    if (undoListWalkingPolygonLatLongs.size() > 0) {
                        // Clears original list data
                        // Remove index from Undo list
                        // Remove markers from Map for existing index
                        // Add new markers on same positions for new index
                        listWalkingPolygonLatLongs.clear();
                        undoListWalkingPolygonLatLongs.remove(undoListWalkingPolygonLatLongs.size() - 1);

                        for (Marker marker : listWalkingPolygonMarkers) {
                            marker.remove();
                        }
                        listWalkingPolygonMarkers.clear();

                        if (undoListWalkingPolygonLatLongs.size() > 0) {
                            drawWalkingPolygonMarkers(undoListWalkingPolygonLatLongs.get(undoListWalkingPolygonLatLongs.size() - 1));
                        } else {
                            mWalkingPolygon.remove();
                            mWalkingPolygon = null;
                        }
                    }
                }
            }

            else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                if (mPolyline != null) {
                    if (undoListLineLatLongs.size() > 0) {
                        // Clears original list data
                        // Remove index from Undo list
                        // Remove markers from Map
                        // Add new markers on same positions for new index
                        listLinesLatLngs.clear();
                        undoListLineLatLongs.remove(undoListLineLatLongs.size() - 1);

                        for (Marker marker : listLinesMarkers) {
                            marker.remove();
                        }
                        listLinesMarkers.clear();

                        if (undoListLineLatLongs.size() > 0) {
                            drawLineMarkers(undoListLineLatLongs.get(undoListLineLatLongs.size() - 1));
                        } else {
                            mPolyline.remove();
                            mPolyline = null;
                        }
                    }
                }
            }
        }
    }

//------------------------------------------------------- OnCreate Options Menu ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (isMeasurementModeON) {
            inflater.inflate(R.menu.maps_measuring, menu);
            menu.findItem(R.id.ic_reset_measure).setVisible(true);
            menu.findItem(R.id.ic_close_measure).setVisible(true);
//            for (int i = 0; i < menu.size(); i++) {
//                MenuItem item = menu.getItem(i);
//                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
//                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
//                item.setTitle(spanString);
//            }
        } else if (!isDrawing) {
            inflater.inflate(R.menu.maps_main, menu);
            //menu.findItem(R.id.ic_layers).setVisible(Utility.getBooleanSavedData(mActivity, Utility.TOP_PANEL.LAYERS));
            // menu.findItem(R.id.ic_mesur).setVisible(Utility.getBooleanSavedData(mActivity, Utility.TOP_PANEL.MEASURE));
            menu.findItem(R.id.ic_layers).setVisible(true);
            menu.findItem(R.id.ic_mesur).setVisible(false);
            menu.findItem(R.id.formSync).setVisible(true);
            menu.findItem(R.id.mapType).setVisible(true);
            // menu.findItem(R.id.ic_gpscompass).setVisible(Utility.getBooleanSavedData(mActivity, Utility.TOP_PANEL.GPS_COMPASS));
//            for (int i = 0; i < menu.size(); i++) {
//                MenuItem item = menu.getItem(i);
//                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
//                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); //fix the color to white
//                item.setTitle(spanString);
//            }

        }
        return true;
    }

//------------------------------------------------------- On Options Item Selected ---------------------------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // case android.R.id.home:
//                mDrawer.openDrawer(GravityCompat.START);
            //finish();
            //    return true;

            case R.id.mapType:
                showMapTypeOptionDialog();
                break;

            case R.id.formSync:
                //Toast.makeText(this, "sync", Toast.LENGTH_SHORT).show();
                if(SystemUtility.isInternetConnected(this)) {
                    //baseApplication.startSyncService();
                    //startMyDialog("Sync...");
                    //SyncFormData();
                    SyncAllData();
                }
                else{
                    Utility.getOKDialogBox(this, "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
                }
                break;

            case R.id.ic_mesur:
                if (isDrawing) {
                    Utility.showDoubleBtnDialog(mActivity, "Alert!!", "There are unsaved changes. Discard anyway ?", "Yes", "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    clearDrawing();
                                    updateCheckMarkFloatingButton(false, "");
                                    turnMeasurementMode(true);
                                    dialog.dismiss();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    });
                } else {
                    clearDrawing();
                    turnMeasurementMode(true);
                }
                return true;
            case R.id.ic_layers:
                if (!isDrawing)
                    redirectToProjectLayersList();
                return true;

            case R.id.ic_reset_measure:
                Utility.showDoubleBtnDialog(mActivity, "Alert!!", "Reset current measurement ?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                clearDrawing();
                                turnMeasurementMode(true);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                return true;
            case R.id.ic_close_measure:
                Utility.showDoubleBtnDialog(mActivity, "Alert!!", "Close measurement ?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                clearDrawing();
                                turnMeasurementMode(false);
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showMapTypeOptionDialog(){

        Utility.showMapTypeDialog(mActivity, mapType -> {
            Utility.saveData(MapsActivity.this, Utility.BASE_MAP,mapType);
            Toast.makeText(mActivity, mapType + " Mode", Toast.LENGTH_SHORT).show();
            setBaseMap();

        });
    }

//---------------------------------------------------------- Draw Layout On Map ---------------------------------------------------------------------------------------------------------------------------------

    private void drawLayersOnMap(LatLng latLng) {

        if (isMeasurementModeON && Utility.isEmptyString(selectedFeature)) {
            Utility.showToast(mActivity, "Select measuring tool", txtDistance);
            return;
        }

        // Walking mode!
        if (isMeasurementModeON && isWalkingTool) {
            Utility.showToast(mActivity, "Walking measuring tool is recording..", txtDistance);
            return;
        }

//---------------------------------------------------------- On Map Click Camera Point Option------------------------------------------------------------
        // Camera Mode!
        if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.cameraPoint))){
                if (cameraPointCounter < 1) {
                    cameraPointCounter++;
                    MarkerOptions cameraPointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.BitmapFromVector(mActivity,R.drawable.icon_camera_marker));
                    cameraPointMarker = mMap.addMarker(cameraPointMarkerOptions);
                    cameraPointMarker.setTag(getResources().getString(R.string.cameraPoint));
                }
        }

//---------------------------------------------------------- On Map Click Point Option------------------------------------------------------------
        // Point mode!
        if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))){

            //----------------------------
            // When layer On then!
            // Point
            if (pointCounter < 1) {
                pointCounter++;
                if(projectLayerModel != null){
                    String icon =  projectLayerModel.getLayerIcon(); //Utility.getSavedData(this,Utility.LAYER_POINT_ICON);
                    if(!icon.equals("")) {
                        String w = projectLayerModel.getLayerIconWidth(); //(Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_WIDTH));
                        String h = projectLayerModel.getLayerIconHeight(); //(Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_HEIGHT));
                        int width = w.equals("") ? 50 : Integer.parseInt(w);
                        int height = h.equals("") ? 50 : Integer.parseInt(h);

                        try {
                            Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(icon), width, height, true);
                            MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(bmp));
                            pointMarker = mMap.addMarker(pointMarkerOptions);
                            assert pointMarker != null;
                            pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                        } catch (Exception e) {
                            MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                            pointMarker = mMap.addMarker(pointMarkerOptions);
                            assert pointMarker != null;
                            pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                        }
                    }
                    else{
                        MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                        pointMarker = mMap.addMarker(pointMarkerOptions);
                        assert pointMarker != null;
                        pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                    }
                }
                else{
                    MarkerOptions pointMarkerOptions = new MarkerOptions().position(latLng).draggable(true).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                    pointMarker = mMap.addMarker(pointMarkerOptions);
                    assert pointMarker != null;
                 //   pointMarker.setTag("Lat: " + new DecimalFormat("0.000000").format(latLng.latitude) + " " + "Lon: " + new DecimalFormat("0.000000").format(latLng.longitude));
                }

            }

        }

//---------------------------------------------------------- On Map Click Line Option------------------------------------------------------------
        // Line Mode!
        else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
            // Logic of adding centre marker on map will start when there's already one marker in polyline
            // When a new latlong is trying to add to polyline
            // calculate middle latlong
            // Add middle latlong in List of that Polyline
            // After that add New Latlong in List of that Polyline
            // Now when going to calculate distance in between 2 latlong then update centre marker's Icon with updated distance text
            // On Start Drag > Copy middle marker as temp. And Remove existing middle markers from list.
            // Now on Dragging > fetch new middle latlong and set it to temp middle marker.
            // On Drag end > Copy latlong from temp middle markers
            // remove temp markers
            // and add new middle marker with previously copied temp middle marlkers' latlong
            // and set them as part of list.
            //------ FIND CENTRE LAT-LONG ————————————
            if (listLinesLatLngs.size() > 0) {
                LatLng pos1 = listLinesLatLngs.get(listLinesLatLngs.size() - 1);
                LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, latLng);
                Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                listLinesLatLngs.add(centrePosition);
                listLinesMarkers.add(markerMiddle);
            }
            //-------------------------------
            listLinesLatLngs.add(latLng);
            Marker marker = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
            marker.setTag(latLng.latitude + ", " + latLng.longitude);
            listLinesMarkers.add(marker);

            if (listLinesMarkers.size() > 0) {
                listLinesMarkers.get(0).setDraggable(listLinesMarkers.size() > 1);
            }

            if (mPolyline != null) {
                mPolyline.setPoints(listLinesLatLngs);
            }else {

//                if(projectLayerModel != null){
//                    PolylineOptions rectOptions = new PolylineOptions()
//                            .clickable(true)
//                            .color(Color.parseColor(projectLayerModel.getLayerLineColor()))
//                            .width(SystemUtility.getLineSize(mActivity))
//                            .startCap(new RoundCap())
//                            .endCap(new RoundCap())
//                            .jointType(JointType.ROUND)
//                            .addAll(listLinesLatLngs)
//                            .geodesic(true);
//                    mPolyline = mMap.addPolyline(rectOptions);
//                }
//                else{

//                PolylineOptions rectOptions = new PolylineOptions()
//                        .clickable(true)
//                        .color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)))
//                        .width(SystemUtility.getLineSize(mActivity))
//                        .startCap(new RoundCap())
//                        .endCap(new RoundCap())
//                        .jointType(JointType.ROUND)
//                        .addAll(listLinesLatLngs)
//                        .geodesic(true);

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.clickable(false);
                polylineOptions.color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)));
                polylineOptions.width(SystemUtility.getLineSize(mActivity));
                String line_type = Utility.getSavedData(mActivity,Utility.LAYER_LINE_TYPE);
                if(!Utility.isEmptyString(line_type)){

                    switch (line_type) {

                        case LAYER_LINE_TYPE.Normal:
                            polylineOptions.startCap(new RoundCap());
                            polylineOptions.endCap(new RoundCap());
                            polylineOptions.jointType(JointType.ROUND);
                            break;

                        case LAYER_LINE_TYPE.SmallDash:
                            polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                            polylineOptions.jointType(JointType.BEVEL);
                            break;

                        case LAYER_LINE_TYPE.LargeDash:
                            polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                            polylineOptions.jointType(JointType.BEVEL);
                            break;
                    }
                }
                // Line Type is Empty then
                else{
                    polylineOptions.startCap(new RoundCap());
                    polylineOptions.endCap(new RoundCap());
                    polylineOptions.jointType(JointType.ROUND);
                }
                polylineOptions.addAll(listLinesLatLngs);
                polylineOptions.geodesic(true);

                mPolyline = mMap.addPolyline(polylineOptions);

            }

//            Log.e(TAG, "Add markers, size: " + listLinesLatLngs.size());
//            for (int i = 0; i < listLinesLatLngs.size(); i++) {
//                Log.e(TAG, "" + listLinesLatLngs.get(i));
//            }
            undoListLineLatLongs.add(SystemUtility.getClonedListLatLng(listLinesLatLngs));
            redoListLineLatLongs = SystemUtility.getClonedList(undoListLineLatLongs);
            updateDistanceBetweenLines(listLinesLatLngs.size() - 1, listLinesLatLngs);
        }

//---------------------------------------------------------- On Map Click Polygon Option------------------------------------------------------------
        // Polygon mode!
        else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
          //  Log.e(TAG,"drawLayersOnMap Polygon");
            //------ FIND CENTRE LAT-LONG ————————————
            if (listPolygonLatLongs.size() > 0) {

                if (listPolygonLatLongs.size() == 1) {
                    // 2nd and 3rd marker in the list
                    LatLng pos1 = listPolygonLatLongs.get(listPolygonLatLongs.size() - 1);
                    LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, latLng);

                    Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                    markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                    listPolygonLatLongs.add(centrePosition);
                    listPolygonMarkers.add(markerMiddle);

                    listPolygonLatLongs.add(latLng);
                    Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                    marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                    listPolygonMarkers.add(marker2);
                }
                else {
                    // When >= 3 points
                    if (listPolygonLatLongs.size() >= 6) {// Remove Last Middle Marker & Position
                        listPolygonMarkers.get(listPolygonLatLongs.size() - 1).remove();
                        listPolygonMarkers.remove(listPolygonLatLongs.size() - 1);
                        listPolygonLatLongs.remove(listPolygonLatLongs.size() - 1);
                    }
                    LatLng lastPos = listPolygonLatLongs.get(listPolygonLatLongs.size() - 1);
                    LatLng centrePosition1 = SystemUtility.getCentreLatLngFrom2LatLng(lastPos, latLng);

                    LatLng firstPos = listPolygonLatLongs.get(0);
                    LatLng centrePosition2 = SystemUtility.getCentreLatLngFrom2LatLng(firstPos, latLng);

                    Marker markerMiddle1 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition1, "", mActivity);
                    markerMiddle1.setTag(centrePosition1.latitude + ", " + centrePosition1.longitude);
                    listPolygonLatLongs.add(centrePosition1);
                    listPolygonMarkers.add(markerMiddle1);

                    listPolygonLatLongs.add(latLng);
                    Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                    marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                    listPolygonMarkers.add(marker2);

                    Marker markerMiddle2 = SystemUtility.addSmallMarkerToMap(mMap, centrePosition2, "", mActivity);
                    markerMiddle2.setTag(centrePosition2.latitude + ", " + centrePosition2.longitude);
                    listPolygonLatLongs.add(centrePosition2);
                    listPolygonMarkers.add(markerMiddle2);
                }

            }
            else {   // First Marker in the list
                listPolygonLatLongs.add(latLng);
                Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, latLng, mActivity);
                marker2.setTag(latLng.latitude + ", " + latLng.longitude);
                listPolygonMarkers.add(marker2);
            }
            //-------------------------------
            if (mPolygon != null) {
                mPolygon.setPoints(listPolygonLatLongs);
            } else {

                PolygonOptions rectOptions = new PolygonOptions()
                        .clickable(false)
                        .addAll(listPolygonLatLongs)
                        .strokeWidth(SystemUtility.getLineSize(mActivity))
                        .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                        .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                mPolygon = mMap.addPolygon(rectOptions);
            }
            undoListPolygonLatLongs.add(SystemUtility.getClonedListLatLng(listPolygonLatLongs));
            redoListPolygonLatLongs = SystemUtility.getClonedList(undoListPolygonLatLongs);
            updateDistanceBetweenPolygonPoints(listPolygonLatLongs.size() - 2, listPolygonLatLongs);
        }

        updateSaveUndoUi();
    }

//------------------------------------------------------- Render layout Online/ Offline ---------------------------------------------------------------------------------------------------------------------------------

    private void renderLayersOffline(ArrayList<ProjectLayerModel> listLayersReadOnly, boolean toRenderPolygonLayer, boolean toRenderPolyLineLayer, boolean toRenderPointLayer) {


        if (toRenderPolygonLayer){
            for (ArrayList<Polygon> listPolygon : listOfMultiLayersPolygons){
                if(listPolygon.size() > 0){
                    for (Polygon polygon : listPolygon) {
                        polygon.remove();
                    }
                }

            }
            listOfMultiLayersPolygons.clear();
            listOfMultiLayerPolygonLatlngsReadOnly.clear();
            colorListPolygonLayers = new ArrayList<>();
            layer_viewonly = new ArrayList<>();
        }
        if (toRenderPolyLineLayer) {
            for (ArrayList<Polyline> listPolyLine : listOfMultiLayerPolyLine) {
                if(listPolyLine.size() > 0){
                    for (Polyline polyline : listPolyLine) {
                        polyline.remove();
                    }
                }
            }
            listOfMultiLayerPolyLine.clear();
            listOfMultiLayerPolyLineReadOnly.clear();
            colorListPolyLineLayers = new ArrayList<>();
            lineTypePolyLineLayer = new ArrayList<>();
        }
        if (toRenderPointLayer) {
            for (ArrayList<Marker> listMarkers : listOfMultiPointsLayers) {
                if(listMarkers.size() > 0){
                    for (Marker marker : listMarkers) {
                        marker.remove();
                    }
                }
            }
            listOfMultiPointsLayers.clear();
            listOfMultiLayerPointsReadOnly.clear();
            colorListPointLayers = new ArrayList<>();
            iconListPointLayers = new ArrayList<>();
        }
        listOfWMSLayer.clear();

        mMap.clear();
        // Start ProgressBar
        startMyDialog("Loading Layer...");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        service.execute(() -> {
            for(ProjectLayerModel bin : listLayersReadOnly){
                if (bin.isEnable()){
                    String layerLatLongData = bin.getLatLong();

//                String flag = bin.getOnly_view();
//                // Base Layer here we get link Data
//                if(!Utility.isEmptyString(flag) && flag.equalsIgnoreCase("t")) {
//                    listOfWMSLayer.add(link);
//                }
//                else{
//                    // Normal working layer
//                }

                    // polygon
                    if (toRenderPolygonLayer && bin.getLayerType().equalsIgnoreCase(LAYER_TYPE.Polygon) && !Utility.isEmptyString(layerLatLongData)) {
                        colorListPolygonLayers.add(bin.getLayerLineColor());
                        layer_viewonly.add(bin.getOnly_view());
                        try{
                            listOfPolygonLatlngsBoundryLayerReadOnly = Utility.convertStringToListOfPolygonLine(layerLatLongData);
                            listOfMultiLayerPolygonLatlngsReadOnly.add(listOfPolygonLatlngsBoundryLayerReadOnly);
                        }
                        catch (Exception e){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    // polyline
                    else if (toRenderPolyLineLayer && bin.getLayerType().equalsIgnoreCase(LAYER_TYPE.Line) && !Utility.isEmptyString(layerLatLongData)) {
                        Log.e(TAG, ""+ bin.getLayerLineColor());
                        colorListPolyLineLayers.add(bin.getLayerLineColor());
                        lineTypePolyLineLayer.add(bin.getLayerLineType());
                        try{
                            listLatLngPolyLineLayerReadOnly = (Utility.convertStringToListOfPolygonLine(layerLatLongData));
                            listOfMultiLayerPolyLineReadOnly.add(listLatLngPolyLineLayerReadOnly);

                        }catch (Exception e){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    // point
                    else if (toRenderPointLayer && bin.getLayerType().equalsIgnoreCase(LAYER_TYPE.Point) && !Utility.isEmptyString(layerLatLongData)) {
                        colorListPointLayers.add(bin.getLayerLineColor());
                        Utility.saveData(MapsActivity.this,Utility.LAYER_POINT_ICON,bin.getLayerIcon());
                        Utility.saveData(MapsActivity.this,Utility.LAYER_POINT_ICON_WIDTH,bin.getLayerIconWidth());
                        Utility.saveData(MapsActivity.this,Utility.LAYER_POINT_ICON_HEIGHT,bin.getLayerIconHeight());
                        iconListPointLayers.add(bin.getLayerIcon());
                        try{
                            listOfMarkersLatLngLayersReadOnly = Utility.convertStringToListOfPolygonLine(layerLatLongData);
                            listOfMultiLayerPointsReadOnly.add(listOfMarkersLatLngLayersReadOnly);

                        }
                        catch (Exception e){
                            Log.e(TAG, e.getMessage());
                        }
                    }


                }
            }

            // Post ----------------------------------------
            handler.post(() -> {
                // Polygon
                if (toRenderPolygonLayer && listOfMultiLayerPolygonLatlngsReadOnly != null && listOfMultiLayerPolygonLatlngsReadOnly.size() > 0) {

                    for (int i = 0; i < listOfMultiLayerPolygonLatlngsReadOnly.size(); i++) {
                        listOfPolygonsBoundryLayerReadOnly.clear();
                        if(listOfMultiLayerPolygonLatlngsReadOnly.get(i).size() > 0){
                            ArrayList<ArrayList<LatLng>> listLayerArr = listOfMultiLayerPolygonLatlngsReadOnly.get(i);
                            String colorCode = colorListPolygonLayers.get(i);
                            String isViewOnly = layer_viewonly.get(i);
                            if (!Utility.isEmptyString(colorCode) && colorCode.startsWith("#")) {
                                colorPolyStroke = Color.parseColor(colorCode);
                                colorCode = colorCode.substring(1);
                                colorPolyFill = Color.parseColor("#4D" + colorCode);
                            }
                            else{
                                colorPolyStroke = Color.YELLOW;
                                colorPolyFill = colorFillTransparent;
                            }
                            // no view
                            if(isViewOnly.equals("f")){
                                for (int j = 0; j < listLayerArr.size(); j++) {
                                    ArrayList<LatLng> listOfLatLngs = listLayerArr.get(j);
                                    if(listOfLatLngs.size() > 0){
                                        PolygonOptions rectOptions = new PolygonOptions()
                                                .clickable(false)
                                                .addAll(listOfLatLngs)
                                                .strokeWidth(7)
                                                .strokeColor(colorPolyStroke)
                                                .fillColor(colorPolyFill);
                                        listOfPolygonsBoundryLayerReadOnly.add(mMap.addPolygon(rectOptions));
                                    }
                                }
                            }
                            // only view
                            else{
                                for (int j = 0; j < listLayerArr.size(); j++) {
                                    ArrayList<LatLng> listOfLatLngs = listLayerArr.get(j);
                                    if(listOfLatLngs.size() > 0){
                                        PolygonOptions rectOptions = new PolygonOptions()
                                                .clickable(false)
                                                .addAll(listOfLatLngs)
                                                .strokeWidth(7)
                                                .strokeColor(colorPolyStroke)
                                                .fillColor(Color.TRANSPARENT);
                                        listOfPolygonsBoundryLayerReadOnly.add(mMap.addPolygon(rectOptions));

                                    }
                                }
                            }
                            listOfMultiLayersPolygons.add(listOfPolygonsBoundryLayerReadOnly);
                        }


                    }
                }
                // Polyline
                if (toRenderPolyLineLayer && listOfMultiLayerPolyLineReadOnly != null && listOfMultiLayerPolyLineReadOnly.size() > 0) {
                    Log.e(TAG,"line");
                    for (int i = 0; i < listOfMultiLayerPolyLineReadOnly.size(); i++) {
                        listPolyLineLayerReadOnly.clear();
                        if(listOfMultiLayerPolyLineReadOnly.size() > 0){

                            ArrayList<ArrayList<LatLng>> listLayerArr = listOfMultiLayerPolyLineReadOnly.get(i);
                            String colorCode = colorListPolyLineLayers.get(i);
                            String lineType = lineTypePolyLineLayer.get(i);
                            if (!Utility.isEmptyString(colorCode) && colorCode.startsWith("#")) {
                                colorPolyLine = Color.parseColor(colorCode);
                            }
                            else{
                                colorPolyLine = Color.BLUE;
                            }
                            for (int j = 0; j < listLayerArr.size(); j++) {
                                ArrayList<LatLng> listOfLatLngs = listLayerArr.get(j);
                                if(listOfLatLngs.size() > 0) {
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.clickable(false);
                                    polylineOptions.color(colorPolyLine);
                                    polylineOptions.width(SystemUtility.getLineSize(mActivity));
                                    if (!Utility.isEmptyString(lineType)) {
                                        switch (lineType) {

                                            case LAYER_LINE_TYPE.Normal:
                                                polylineOptions.startCap(new RoundCap());
                                                polylineOptions.endCap(new RoundCap());
                                                polylineOptions.jointType(JointType.ROUND);
                                                break;

                                            case LAYER_LINE_TYPE.SmallDash:
                                                polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                                                polylineOptions.jointType(JointType.BEVEL);
                                                break;

                                            case LAYER_LINE_TYPE.LargeDash:
                                                polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                                                polylineOptions.jointType(JointType.BEVEL);
                                                break;
                                        }
                                    }
                                    // Line Type is Empty then
                                    else {
                                        polylineOptions.startCap(new RoundCap());
                                        polylineOptions.endCap(new RoundCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                    }
                                    polylineOptions.addAll(listOfLatLngs);
                                    polylineOptions.geodesic(true);

                                    listPolyLineLayerReadOnly.add(mMap.addPolyline(polylineOptions));
                                }
                            }
                            listOfMultiLayerPolyLine.add(listPolyLineLayerReadOnly);
                        }
                    }
                }
                // Point
                if (toRenderPointLayer && listOfMultiLayerPointsReadOnly != null && listOfMultiLayerPointsReadOnly.size() > 0) {

                    for (int i = 0; i < listOfMultiLayerPointsReadOnly.size(); i++) {
                        listOfMarkersLayersReadOnly.clear();
                        String icon = iconListPointLayers.get(i);
                        if(listOfMultiLayerPointsReadOnly.size() > 0) {

                            ArrayList<ArrayList<LatLng>> listLayerArr = listOfMultiLayerPointsReadOnly.get(i);
                            for (int j = 0; j < listLayerArr.size(); j++) {
                                ArrayList<LatLng> listOfLatLngs = listLayerArr.get(j);
                                if(listOfLatLngs.size() > 0) {
                                    for (int z = 0; z < listOfLatLngs.size(); z++) {
                                        LatLng latLng = listOfLatLngs.get(z);
                                        if (!icon.equals("")) {
                                            String w = (Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_WIDTH));
                                            String h = (Utility.getSavedData(MapsActivity.this, Utility.LAYER_POINT_ICON_HEIGHT));
                                            int width = w.equals("") ? 50 : Integer.parseInt(w);
                                            int height = h.equals("") ? 50 : Integer.parseInt(h);
                                            try {
                                                Bitmap bmp = Bitmap.createScaledBitmap(decodeBase64Image(icon), width, height, true);
                                                Marker marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bmp)).position(latLng).draggable(false));
                                                marker.setTag(latLng.latitude + ", " + latLng.longitude);
                                                listOfMarkersLayersReadOnly.add(marker);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Marker marker = mMap.addMarker(new MarkerOptions().icon(SystemUtility.getRoundedMarkerIconGreen(mActivity)).position(latLng).draggable(false));
                                                marker.setTag(latLng.latitude + ", " + latLng.longitude);
                                                listOfMarkersLayersReadOnly.add(marker);
                                            }
                                        } else {
                                            Marker marker = mMap.addMarker(new MarkerOptions().icon(SystemUtility.getRoundedMarkerIconGreen(mActivity)).position(latLng).draggable(false));
                                            marker.setTag(latLng.latitude + ", " + latLng.longitude);
                                            listOfMarkersLayersReadOnly.add(marker);
                                        }
                                    }
                                    listOfMultiPointsLayers.add(listOfMarkersLayersReadOnly);
                                }
                            }
                        }
                    }
                }

                loadLocalData();
                dismissMyDialog();
            });
        });

        // WMS Layer
//        if(listOfWMSLayer != null && listOfWMSLayer.size() > 0){
//            if(SystemPermission.isInternetConnected(this)){
//                for(int i=0; i<listOfWMSLayer.size(); i++){
//                    TileProvider tileProvider = WMSTileProviderFactory.getWMSTileProvider(WMSProvider.getWMSLayer(listOfWMSLayer.get(i)));
//                    mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
//                }
//            }
//        }

     //   dismissMyDialog();


//            }
//        }).start();

//        drawGeoFence();
//        GetAllSurveyData();
//        GetSurveyFormData();
//        readMapCameraImageS();
//        readMapCameraImageLocal();

    }

//---------------------------------------------------------- rlWalkingMinDistance ---------------------------------------------------------------------------------------------

    private void rlWalkingMinDistance(){

            Utility.getSelectedItemDialogBox(mActivity, "Walking Distance", new String[]{Utility.MIN_DISTANCE.METER_5,Utility.MIN_DISTANCE.METER_10,Utility.MIN_DISTANCE.METER_15,Utility.MIN_DISTANCE.METER_20}, new Utility.DialogBoxSelectedItem() {
                @Override
                public void onSelectedItem(String selectedItem) {

                    switch (selectedItem){
                        case Utility.MIN_DISTANCE.METER_5:
                            MIN_DISTANCE_UPDATE = 5d;
                            Toast.makeText(mActivity, "Select "+MIN_DISTANCE_UPDATE+" Meter", Toast.LENGTH_SHORT).show();
                            Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_5);
                            break;

                        case Utility.MIN_DISTANCE.METER_10:
                            MIN_DISTANCE_UPDATE = 10d;
                            Toast.makeText(mActivity, "Select "+MIN_DISTANCE_UPDATE+" Meter", Toast.LENGTH_SHORT).show();
                            Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_10);
                            break;

                        case Utility.MIN_DISTANCE.METER_15:
                            MIN_DISTANCE_UPDATE = 15d;
                            Toast.makeText(mActivity, "Select "+MIN_DISTANCE_UPDATE+" Meter", Toast.LENGTH_SHORT).show();
                            Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_15);
                            break;

                        case Utility.MIN_DISTANCE.METER_20:
                            MIN_DISTANCE_UPDATE = 20d;
                            Toast.makeText(mActivity, "Select "+MIN_DISTANCE_UPDATE+" Meter", Toast.LENGTH_SHORT).show();
                            Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_20);
                            break;

                        default:
                            MIN_DISTANCE_UPDATE = 5d;
                            Toast.makeText(mActivity, "Select "+MIN_DISTANCE_UPDATE+" Meter", Toast.LENGTH_SHORT).show();
                            Utility.saveData(mActivity,Utility.MIN_WALKING_DISTANCE,Utility.MIN_WALKING.METER_5);
                            break;
                    }
                }
            });

        }

//---------------------------------------------------------- rlWalkingReset --------------------------------------------------------------------------------------------------

    private void rlWalkingReset(){

        // PolyLine Clear  --------------------------
        listLinesLatLngs.clear();
        for (Marker marker : listLinesMarkers) {
            marker.remove();
        }
        listLinesMarkers.clear();
        undoListLineLatLongs.clear();
        redoListLineLatLongs.clear();
        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        // Polygon Clear  ---------------------------
        listPolygonLatLongs.clear();
        for (Marker marker : listPolygonMarkers) {
            marker.remove();
        }
        listPolygonMarkers.clear();
        undoListPolygonLatLongs.clear();
        redoListPolygonLatLongs.clear();
        if (mPolygon != null) {
            mPolygon.remove();
            mPolygon = null;
        }

        listWalkingPolygonLatLongs.clear();
        for(Marker marker : listWalkingPolygonMarkers){
            marker.remove();
        }
        listWalkingPolygonMarkers.clear();
        undoListWalkingPolygonLatLongs.clear();
        redoListWalkingPolygonLatLongs.clear();
        if(mWalkingPolygon != null){
            mWalkingPolygon.remove();
            mWalkingPolygon = null;
        }
        updateSaveUndoUi();

    }

//---------------------------------------------------------- Draw Walking Measure -------------------------------------------------------------------------------------

    // Tracking Start when latLon is coming!
    private void drawLayerOnWalkingMeasure(LatLng currentLatLng) {
            // do something...
            boolean hasEnoughDistance;
            // tracking is on and user not dragging marker now
            if (isWalkingTool && !isDraggingMarker) {

                // Polygon Walking Options ------------------------------------------------------------------------------------------------------------------------------------------
                if (isPolygonWalkingOn) {

                     // Log.e(TAG,"Size: " + listWalkingPolygonLatLongs.size());
                      if(listWalkingPolygonLatLongs.size() > 0){
                        double distance = SphericalUtil.computeDistanceBetween(currentLatLng, listWalkingPolygonLatLongs.get(listWalkingPolygonLatLongs.size() - 1));
                        hasEnoughDistance = (distance > Integer.parseInt(Utility.getSavedData(mActivity, Utility.MIN_WALKING_DISTANCE))); // -> min distance update set at -> 10 meter
                        Log.e(TAG, "hasEnoughDistance: "+ hasEnoughDistance);

                        if(hasEnoughDistance) {
                                LatLng pos1 = listWalkingPolygonLatLongs.get(listWalkingPolygonLatLongs.size() - 1);
                                // get centre Position for last marker and current marker
                                LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, currentLatLng);
                                // Now Add Middle Marker or small Marker
                                Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
                                markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                                listWalkingPolygonLatLongs.add(centrePosition);
                                listWalkingPolygonMarkers.add(markerMiddle);
                                // Add Current Marker
                                listWalkingPolygonLatLongs.add(currentLatLng);
                                Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
                                marker2.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                                listWalkingPolygonMarkers.add(marker2);

                            // Add Polygon!
                            if (mWalkingPolygon != null) {
                                mWalkingPolygon.remove();
                            }
                            PolygonOptions rectOptions = new PolygonOptions()
                                    .clickable(false)
                                    .addAll(listWalkingPolygonLatLongs)
                                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                            mWalkingPolygon = mMap.addPolygon(rectOptions);
                            calculateTotalDistance(listWalkingPolygonLatLongs);
                            // undo list
                            undoListWalkingPolygonLatLongs.add(SystemUtility.getClonedListLatLng(listWalkingPolygonLatLongs));
                            // redo list
                            redoListWalkingPolygonLatLongs = SystemUtility.getClonedList(undoListWalkingPolygonLatLongs);
                            updateSaveUndoUi();
                        }
                      }
                      else{
                          //mWalkingPolygonMarkerCount++;
                          hasEnoughDistance = true;
                            Marker marker2 = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
                            marker2.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                            listWalkingPolygonMarkers.add(marker2);
                            listWalkingPolygonLatLongs.add(currentLatLng);
                            //-------------------------------
                            if (mWalkingPolygon != null) {
                                mWalkingPolygon.remove();
                            }
                                PolygonOptions rectOptions = new PolygonOptions()
                                        .clickable(false)
                                        .addAll(listWalkingPolygonLatLongs)
                                        .strokeWidth(SystemUtility.getLineSize(mActivity))
                                        .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                                        .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
                                mWalkingPolygon = mMap.addPolygon(rectOptions);
                      }

                }

                // Line Walking Options---------------------------------------------------------------------------------------------------------------------------------------------------
                else {
                    // Line Walking Options
                    if (listLinesLatLngs.size() > 0) {
                        // distance in meter!
                        double distance = SphericalUtil.computeDistanceBetween(currentLatLng, listLinesLatLngs.get(listLinesLatLngs.size() - 1));
                        hasEnoughDistance = (distance > Integer.parseInt(Utility.getSavedData(mActivity,Utility.MIN_WALKING_DISTANCE))); // -> min distance update set at -> 10 meter
                        //hasEnoughDistance = (distance > MIN_DISTANCE_UPDATE); // -> min distance update set at -> 10 meter
                    }
                    else
                        hasEnoughDistance = true;

                    // if distance is 10 meter then
                    if (hasEnoughDistance) {
                        // we add small marker at 5 meter position here
                        if (listLinesLatLngs.size() > 0) {
                            LatLng pos1 = listLinesLatLngs.get(listLinesLatLngs.size() - 1);
                            LatLng centrePosition = SystemUtility.getCentreLatLngFrom2LatLng(pos1, currentLatLng);
                            Marker markerMiddle = SystemUtility.addSmallMarkerToMap(mMap, centrePosition, "", mActivity);
//                    markerMiddle.setDraggable(false);
                            markerMiddle.setTag(centrePosition.latitude + ", " + centrePosition.longitude);
                            listLinesLatLngs.add(centrePosition);
                            listLinesMarkers.add(markerMiddle);
                        }
                        //-------------------------------
                        listLinesLatLngs.add(currentLatLng);
                        // add big marker at 10 meter position here
                        Marker marker = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
//                marker.setDraggable(false);
                        marker.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                        listLinesMarkers.add(marker);

                        // we can move 1 marker only if we have more then 1 marker!
                        if (listLinesMarkers.size() > 0) {
                            listLinesMarkers.get(0).setDraggable(listLinesMarkers.size() > 1);
                        }
                        // Add polyline
                        if (mPolyline != null) {
                            mPolyline.setPoints(listLinesLatLngs);
                        } else {
                            PolylineOptions rectOptions = new PolylineOptions()
                                    .clickable(false)
                                    .color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)))
                                    .width(SystemUtility.getLineSize(mActivity))
                                    .startCap(new RoundCap())
                                    .endCap(new RoundCap())
                                    .jointType(JointType.ROUND)
                                    .addAll(listLinesLatLngs)
                                    .geodesic(true);
                            mPolyline = mMap.addPolyline(rectOptions);
                        }
                        // undo list
                        undoListLineLatLongs.add(SystemUtility.getClonedListLatLng(listLinesLatLngs));
                        // redo list
                        redoListLineLatLongs = SystemUtility.getClonedList(undoListLineLatLongs);
                        // update distance b/w lines
                        updateDistanceBetweenLines(listLinesLatLngs.size() - 1, listLinesLatLngs);
                        updateSaveUndoUi();
                    }


                }

            }

    }

//---------------------------------------------------------- Draw Line Markers ---------------------------------------------------------------------------------------------------------------------------------

    private void drawLineMarkers(ArrayList<LatLng> latLngsArray) {
        listLinesLatLngs.clear();
        listLinesMarkers.clear();
        for (int i = 0; i < latLngsArray.size(); i++) {
            LatLng currentLatLng = latLngsArray.get(i);
            listLinesLatLngs.add(currentLatLng);
            if (i % 2 == 0) {
                Marker markerBig = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
                markerBig.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                listLinesMarkers.add(markerBig);
            } else {
                LatLng prevLatLng = null, nextLatLng = null;
                if (i + 1 < latLngsArray.size()) {
                    // Can take value from index +1
                    prevLatLng = latLngsArray.get(i - 1);
                    nextLatLng = latLngsArray.get(i + 1);
                }
                String display_distance = getDistanceBetween2Points(prevLatLng, nextLatLng);
                Marker markerSmall = SystemUtility.addSmallMarkerToMap(mMap, currentLatLng, display_distance, mActivity);
                markerSmall.setTag(display_distance);
                listLinesMarkers.add(markerSmall);
            }
        }
        if (listLinesMarkers.size() > 0) {
            listLinesMarkers.get(0).setDraggable(listLinesMarkers.size() > 1);
        }
        if (mPolyline != null) {
            mPolyline.setPoints(listLinesLatLngs);
        } else {
            PolylineOptions rectOptions = new PolylineOptions()
                    .clickable(false)
                    .color(Color.parseColor(SystemUtility.getPolyLineColorHexCode(mActivity)))
                    .width(SystemUtility.getLineSize(mActivity))
                    .startCap(new RoundCap())
                    .endCap(new RoundCap())
                    .jointType(JointType.ROUND)
                    .addAll(listLinesLatLngs)
                    .geodesic(true);
            mPolyline = mMap.addPolyline(rectOptions);
        }
        calculateTotalDistance(listLinesLatLngs);
    }

//---------------------------------------------------------- Draw Polygon Markers ---------------------------------------------------------------------------------------------------------------------------------

    private void drawWalkingPolygonMarkers(ArrayList<LatLng> latLngsArray){
        listWalkingPolygonLatLongs.clear();
        listWalkingPolygonMarkers.clear();
        for (int i = 0; i < latLngsArray.size(); i++) {
            LatLng currentLatLng = latLngsArray.get(i);
            listWalkingPolygonLatLongs.add(currentLatLng);
            if (i % 2 == 0) {
                Marker markerBig = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
                markerBig.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                listWalkingPolygonMarkers.add(markerBig);
            } else {
                LatLng prevLatLng, nextLatLng;
                if (i + 1 < latLngsArray.size()) {
                    // Can take value from index +1
                    prevLatLng = latLngsArray.get(i - 1);
                    nextLatLng = latLngsArray.get(i + 1);
                } else {
                    // take value from index = 0
                    prevLatLng = latLngsArray.get(i - 1);
                    nextLatLng = latLngsArray.get(0);
                }
                String display_distance = getDistanceBetween2Points(prevLatLng, nextLatLng);
                Marker markerSmall = SystemUtility.addSmallMarkerToMap(mMap, currentLatLng, display_distance, mActivity);
                markerSmall.setTag(display_distance);
                listWalkingPolygonMarkers.add(markerSmall);
            }
        }
        if (mWalkingPolygon != null) {
            mWalkingPolygon.remove();
        }

            PolygonOptions rectOptions = new PolygonOptions()
                    .clickable(false)
                    .addAll(listWalkingPolygonLatLongs)
                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
            mWalkingPolygon = mMap.addPolygon(rectOptions);

        calculateTotalDistance(listWalkingPolygonLatLongs);
    }

    private void drawPolygonMarkers(ArrayList<LatLng> latLngsArray) {
        listPolygonLatLongs.clear();
        listPolygonMarkers.clear();
        for (int i = 0; i < latLngsArray.size(); i++) {
            LatLng currentLatLng = latLngsArray.get(i);
            listPolygonLatLongs.add(currentLatLng);
            if (i % 2 == 0) {
                Marker markerBig = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
                markerBig.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
                listPolygonMarkers.add(markerBig);
            } else {
                LatLng prevLatLng, nextLatLng;
                if (i + 1 < latLngsArray.size()) {
                    // Can take value from index +1
                    prevLatLng = latLngsArray.get(i - 1);
                    nextLatLng = latLngsArray.get(i + 1);
                } else {
                    // take value from index = 0
                    prevLatLng = latLngsArray.get(i - 1);
                    nextLatLng = latLngsArray.get(0);
                }
                String display_distance = getDistanceBetween2Points(prevLatLng, nextLatLng);
                Marker markerSmall = SystemUtility.addSmallMarkerToMap(mMap, currentLatLng, display_distance, mActivity);
                markerSmall.setTag(display_distance);
                listPolygonMarkers.add(markerSmall);
            }
        }
        if (mPolygon != null) {
            mPolygon.setPoints(listPolygonLatLongs);
        } else {
            PolygonOptions rectOptions = new PolygonOptions()
                    .clickable(false)
                    .addAll(listPolygonLatLongs)
                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)));
            mPolygon = mMap.addPolygon(rectOptions);
        }
        calculateTotalDistance(listPolygonLatLongs);
    }

//---------------------------------------------------------- Draw Circle Points ---------------------------------------------------------------------------------------------------------------------------------

    private void drawCirclePoints(ArrayList<LatLng> latLngsArray) {
        listCirclesMarker.clear();
        listCircles.clear();
        for (int i = 0; i < latLngsArray.size(); i++) {
            LatLng currentLatLng = latLngsArray.get(i);
            listCircles.add(mMap.addCircle(new CircleOptions()
                    .center(currentLatLng)
                    .radius(RADIUS_CIRCLE_MTR)
                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                    .strokeColor(Color.parseColor(SystemUtility.getColorHexCode(mActivity)))
                    .fillColor(Color.parseColor(SystemUtility.getColorTransparentCode(mActivity)))));
            Marker marker = SystemUtility.addBigMarkerToMap(mMap, currentLatLng, mActivity);
            marker.setTag(currentLatLng.latitude + ", " + currentLatLng.longitude);
            listCirclesMarker.add(marker);
        }
    }

//------------------------------------------------------- Measurement Mode ---------------------------------------------------------------------------------------------------------------------------------

    private void turnMeasurementMode(boolean isMeasurementModeON) {
        this.isMeasurementModeON = isMeasurementModeON;
        txtDistance.setVisibility(View.GONE);
        llFeature.setVisibility(isMeasurementModeON ? View.VISIBLE : View.GONE);
        selectedFeature = "";
        isWalkingTool = false;
        if (isMeasurementModeON) {
            updateMeasuringTool(true);
//            rlSaveProjectLayer.setVisibility(View.GONE);
            imgSavePlus.setImageResource(R.drawable.setting);
            toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.siemensBlueColor));
//            toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_measuring_toolbar));
            toolbar.setTitle(getString(R.string.measurement));
            toolbar.setSubtitle(null);
            llMenu.setVisibility(View.GONE);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            updateVisibilityOfLayersSettings(false);
        }
        else {
            updateMeasuringTool(false);
//            rlSaveProjectLayer.setVisibility(View.VISIBLE);
            imgSavePlus.setImageResource(android.R.drawable.ic_input_add);
            llMenu.setVisibility(View.VISIBLE);
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            updateCheckMarkFloatingButton(false, "");
        }
        invalidateOptionsMenu();
    }

//---------------------------------------------------------- Calculate total Distance ---------------------------------------------------------------------------------------------------------------------------------

    private void calculateTotalDistance(ArrayList<LatLng> listLatLng) {
        if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))){
            if (listLatLng.size() > 0) {
                totalDistanceLineVertices = SystemUtility.getTotalDistanceFromAllVertices(listLatLng, mActivity);
                txtDistance.setText("Total distance: " + totalDistanceLineVertices);
                txtDistance.setVisibility(View.VISIBLE);
            } else
                txtDistance.setVisibility(View.GONE);
        }
        else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone)) || selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))) {
            int totalDistance = 0;
            for (int i = 0; i < listLatLng.size(); i++) {
                if (i % 2 != 0) {
                    LatLng prevLatLng, nextLatLng;
                    if (i + 1 < listLatLng.size()) {
                        // Can take value from index +1
                        prevLatLng = listLatLng.get(i - 1);
                        nextLatLng = listLatLng.get(i + 1);
                    } else {
                        // take value from index = 0
                        prevLatLng = listLatLng.get(i - 1);
                        nextLatLng = listLatLng.get(0);
                    }
                    double distance = SphericalUtil.computeDistanceBetween(prevLatLng, nextLatLng);
                    totalDistance += distance;
                }
            }
            if(totalDistance > 0) {
                totalDistanceLineVertices = convertDistanceFromMeter(mActivity, totalDistance);
                if (listLatLng.size() > 3)
                    txtDistance.setText("Total Perimeter: " + totalDistanceLineVertices + "\n" + "Area: " + convertAreaFromSqMeter(mActivity, SphericalUtil.computeArea(listLatLng)));
                else
                    txtDistance.setText("Total Perimeter: " + totalDistanceLineVertices);
                txtDistance.setVisibility(View.VISIBLE);
            } else
                txtDistance.setVisibility(View.GONE);
        }
        else
            txtDistance.setVisibility(View.GONE);
    }

//---------------------------------------------------------- Distance b/w 2 points ---------------------------------------------------------------------------------------------------------------------------------

    private String getDistanceBetween2Points(LatLng latLng1, LatLng latLng2) {
        double distance = SphericalUtil.computeDistanceBetween(latLng1, latLng2);
        return convertDistanceFromMeter(mActivity, distance);
    }

//------------------------------------------------------- Update ---------------------------------------------------------------------------------------------------------------------------------

    private void updateVisibilityOfLayersSettings() {
        if (rcvLayers.getVisibility() == View.VISIBLE)
            rcvLayers.setVisibility(View.GONE);
        else
            rcvLayers.setVisibility(View.VISIBLE);
    }

    private void updateVisibilityOfrlCamera(boolean isVisible){
        rlCamera.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void updateVisibilityOfLayersSettings(boolean isVisible) {
        rcvLayers.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void updateMeasuringTool(boolean toVisible) {
        llMeasuringTools.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void updateSaveWalking(boolean toVisible) {
        rlSaveWalkingLocation.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void updateLayerWalkingStop(boolean toVisible) {
        rlWalkingStop.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void showConverterMeasurmentButton(boolean toVisible) {
        rlConverterMeasurement.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void showrlWalkingResetButton(boolean toVisible) {
        rlWalkingReset.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void showrlWalkingMinDistanceButton(boolean toVisible) {
        rlWalkingMinDistance.setVisibility(toVisible ? View.VISIBLE : View.GONE);
    }

    private void updateCheckMarkFloatingButton(boolean isDrawing, String name) {
        this.isDrawing = isDrawing;
        if (isDrawing) {
            if(isMapCameraOn){
                imgSavePlus.setImageResource(android.R.drawable.ic_input_add);
            }
            else{
                imgSavePlus.setImageResource(R.drawable.icon_save);
            }
            //toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.bg_measuring_toolbar));
            toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.siemensBlueColor));
            toolbar.setTitle(name);
            toolbar.setSubtitle(null);
            updateVisibilityOfLayersSettings(false);
            // Camera OFF
            updateVisibilityOfrlCamera(false);

        }
        else {
            selectedFeature = "";
            binLayerProjectSelected = null;
            projectLayerModel = null;
            imgSavePlus.setImageResource(android.R.drawable.ic_input_add);
            toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.siemensBlueColor));
            String projectName = Utility.getSavedData(mActivity, Utility.PROJECT_PROJECT_NAME);
            String projectworkName = Utility.getSavedData(mActivity, Utility.PROJECT_WORK_NAME);
            if (Utility.isEmptyString(projectName))
                toolbar.setTitle(Utility.getSavedData(mActivity, Utility.PROFILE_FIRSTNAME));
            else{
                toolbar.setTitle(projectName);
                toolbar.setSubtitle(projectworkName);
            }
            updateVisibilityOfLayersSettings(true);
            // Camera OFF
            updateVisibilityOfrlCamera(true);
            txtDistance.setVisibility(View.GONE);
        }
        invalidateOptionsMenu();
    }

    private void updateDistanceBetweenPolygonPoints(int current_index_main_vertex, ArrayList<LatLng> listLatLng) {
        double distance = 0, distance2 = 0;
        if (listLatLng.size() < 3)
            return;
        if (listLatLng.size() == 3) {
            distance = SphericalUtil.computeDistanceBetween(listLatLng.get(0), listLatLng.get(listLatLng.size() - 1));
            String displayDistance = convertDistanceFromMeter(mActivity, distance);
            listPolygonMarkers.get(1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance));
            listPolygonMarkers.get(1).setTag(displayDistance);
        }
        else if (listLatLng.size() > 3 && (current_index_main_vertex == listLatLng.size() - 2)) {
            // (BIG POINT IS ALWAYS GOING TO BE 2nd LAST)
            distance = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index_main_vertex), listLatLng.get(current_index_main_vertex - 2));
            distance2 = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index_main_vertex), listLatLng.get(0));
            String displayDistance1 = convertDistanceFromMeter(mActivity, distance);
            String displayDistance2 = convertDistanceFromMeter(mActivity, distance2);
            listPolygonMarkers.get(current_index_main_vertex - 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance1));
            listPolygonMarkers.get(current_index_main_vertex + 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance2));
            listPolygonMarkers.get(current_index_main_vertex - 1).setTag(displayDistance1);
            listPolygonMarkers.get(current_index_main_vertex + 1).setTag(displayDistance2);
        }
        calculateTotalDistance(listLatLng);
    }
    private void updateDistanceBetweenLines(int current_index, ArrayList<LatLng> listLatLng) {
        double distance = 0, distance2 = 0;
        if (listLatLng.size() < 3) {
            txtDistance.setVisibility(View.GONE);
            return;
        } else if (current_index == 0) {//FIRST POINT
            if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                distance = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index), listLatLng.get(current_index + 2));
                String displayDistance = convertDistanceFromMeter(mActivity, distance);
                listLinesMarkers.get(current_index + 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance));
                listLinesMarkers.get(current_index + 1).setTag(displayDistance);
            }
        } else if (current_index == listLatLng.size() - 1) {//LAST POINT
            if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                distance = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index), listLatLng.get(current_index - 2));
                String displayDistance = convertDistanceFromMeter(mActivity, distance);
                listLinesMarkers.get(current_index - 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance));
                listLinesMarkers.get(current_index - 1).setTag(displayDistance);
            }
        } else {// MID POINT
            if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
                // Changing Icon for Middle Marker (2nd last from added)
                distance = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index), listLatLng.get(current_index - 2));
                distance2 = SphericalUtil.computeDistanceBetween(listLatLng.get(current_index), listLatLng.get(current_index + 2));
                String displayDistance1 = convertDistanceFromMeter(mActivity, distance);
                String displayDistance2 = convertDistanceFromMeter(mActivity, distance2);
                listLinesMarkers.get(current_index - 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance1));
                listLinesMarkers.get(current_index + 1).setIcon(SystemUtility.getCustomMarkerIconForDistance(mActivity, displayDistance2));
                listLinesMarkers.get(current_index - 1).setTag(displayDistance1);
                listLinesMarkers.get(current_index + 1).setTag(displayDistance2);
            }
        }
        calculateTotalDistance(listLatLng);
    }

    void updateSaveUndoUi() {
        if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Polygone))) {
            rlUndo.setVisibility(undoListPolygonLatLongs.size() > 0 ? View.VISIBLE : View.GONE);
            rlRedo.setVisibility(undoListPolygonLatLongs.size() < redoListPolygonLatLongs.size() ? View.VISIBLE : View.GONE);
        } else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))) {
            rlUndo.setVisibility(undoListWalkingPolygonLatLongs.size() > 0 ? View.VISIBLE : View.GONE);
            rlRedo.setVisibility(undoListWalkingPolygonLatLongs.size() < redoListWalkingPolygonLatLongs.size() ? View.VISIBLE : View.GONE);
        } else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Line))) {
            rlUndo.setVisibility(undoListLineLatLongs.size() > 0 ? View.VISIBLE : View.GONE);
            rlRedo.setVisibility(undoListLineLatLongs.size() < redoListLineLatLongs.size() ? View.VISIBLE : View.GONE);
        } else if (selectedFeature.equalsIgnoreCase(getResources().getString(R.string.Point))) {
            rlUndo.setVisibility(undoListPointLatLongs.size() > 0 ? View.VISIBLE : View.GONE);
            rlRedo.setVisibility(undoListPointLatLongs.size() < redoListPointLatLongs.size() ? View.VISIBLE : View.GONE);
        } else {
            rlUndo.setVisibility((listLinesLatLngs.size() > 0 || listPolygonLatLongs.size() > 0) ? View.VISIBLE : View.GONE);
            rlRedo.setVisibility((listLinesLatLngs.size() > 0 || listPolygonLatLongs.size() > 0) ? View.VISIBLE : View.GONE);
        }
       // rlCentre.setVisibility(Utility.isEmptyString(selectedFeature) ? View.GONE : View.VISIBLE);
    }

//------------------------------------------------------- Remove ---------------------------------------------------------------------------------------------------------------------------------

    private void removeCircle() {
        if (circleTemp != null) {
            circleTemp.remove();
            circleMarkerTemp.remove();
        }
    }

    private void removeTempPolyLine() {
        if (mPolylineTemp != null) {
            mPolylineTemp.remove();
            listLinesLatLngsTemp.clear();
            for (int i = 0; i < listLinesMarkersTemp.size(); i++) {
                listLinesMarkersTemp.get(i).remove();
            }
            listLinesMarkersTemp.clear();
        }
    }

    private void clearDrawing() {
//        ------- POINT -------------------
        updateLayerWalkingStop(false);
        showConverterMeasurmentButton(false);
        showrlWalkingResetButton(false);
        showrlWalkingMinDistanceButton(false);

        if(cameraPointMarker != null){
            cameraPointMarker.remove();
            cameraPointMarker = null;
        }
        cameraPointCounter = 0;
        isMapCameraOn = false;

        if(pointMarker != null){
            pointMarker.remove();
            pointMarker = null;
        }
        pointCounter = 0;

        for(Marker marker : listPointMarker){
            marker.remove();
        }
        listPointMarker.clear();

        // Circle!
        for (Circle circle : listCircles) {
            circle.remove();
        }
        listCircles.clear();
        for (Marker marker : listCirclesMarker) {
            marker.remove();
        }
        listCirclesMarker.clear();
        undoListPointLatLongs.clear();
        redoListPointLatLongs.clear();

//      --------------------------
        // Clear Line
        listLinesLatLngs.clear();
        for (Marker marker : listLinesMarkers) {
            marker.remove();
        }
        listLinesMarkers.clear();

        undoListLineLatLongs.clear();
        redoListLineLatLongs.clear();

        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
//        --------------------------
        // Clear Polygon
        listPolygonLatLongs.clear();
        for (Marker marker : listPolygonMarkers) {
            marker.remove();
        }
        listPolygonMarkers.clear();
        undoListPolygonLatLongs.clear();
        redoListPolygonLatLongs.clear();
        if (mPolygon != null) {
            mPolygon.remove();
            mPolygon = null;
        }

        // clear  walking polygon
        listWalkingPolygonLatLongs.clear();
        mWalkingPolygonMarkerCount = 0;
        for(Marker marker : listWalkingPolygonMarkers){
            marker.remove();
        }
        listWalkingPolygonMarkers.clear();
        undoListWalkingPolygonLatLongs.clear();
        redoListWalkingPolygonLatLongs.clear();

        if(mWalkingPolygonLastPointMarker != null){
            mWalkingPolygonLastPointMarker.remove();
        }
        if(mWalkingPolygon != null){
            mWalkingPolygon.remove();
            mWalkingPolygon = null;
        }
//        --------------------------
        selectedFeature = "";
        isWalkingTool = false;
        isPolygonWalkingOn = false;
        binLayerProjectSelected = null;
        projectLayerModel = null;
        // Save Option On -> Change By Rahul Suthar
        updateSaveWalking(false);
        updateSaveUndoUi();

    }


//------------------------------------------------------- Dialog Box ---------------------------------------------------------------------------------------------------------------------------------

    private void dismissMyDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void startMyDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        } else {
            progressDialog.setMessage(message);
        }
    }

    void dismissRevokeDialog() {
        if (alertDialog != null && alertDialog.isShowing() && !isFinishing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

//------------------------------------------------------- Set Active UI ---------------------------------------------------------------------------------------------------------------------------------

    private void setActiveUI(boolean toggle) {
        if (toggle) {
            getSupportActionBar().show();
            llFeature.setVisibility(View.VISIBLE);
            rlBottomView.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().hide();
            llFeature.setVisibility(View.GONE);
            rlBottomView.setVisibility(View.GONE);
        }
    }

//-------------------------------------------- Location Permission ----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
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
        }).setCancelable(true).show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.lbl_Ok, fromDialog)
                .setCancelable(true)
                .show();
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
        //Toast.makeText(mActivity, "Mock Location Detected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
    }

    // LatLon is coming form gps
    @Override
    public void onNewLocationAvailable(Location location) {

        if (location == null) return;
        mCurrentLocation = location;
        txtAccuracy1.setText("Accuracy: " + mCurrentLocation.getAccuracy() + " mtr");
        txtAccuracy1.setVisibility(View.VISIBLE);

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        if (isNormalUserMode && !isCurrentLocInvoked) {
            isCurrentLocInvoked = true;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, ZOOM_LEVEL_BOUNDRY));
        }

        if(geoFence.size() > 0){
            if(mCurrentLocation != null){
                if(!isUserInsideBoundary(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()))){
                   // Log.e("Zone","User OutSide Zone");
                    Utility.saveData(this,Utility.INSIDE_ZONE, false);
                }
                else{
                   // Log.e("Zone","User InSide Zone");
                    Utility.saveData(this,Utility.INSIDE_ZONE, true);
                }
            }
        }
        else{
            Utility.saveData(this,Utility.INSIDE_ZONE, true);
        }

        drawLayerOnWalkingMeasure(currentLatLng);

    }

    public void revokeLocation() {
       // Log.e("ProjectActivity", "Received revokeLocation");
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(R.string.switchOnLocationShort).setPositiveButton(R.string.lbl_Ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismissRevokeDialog();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    // android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    // Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }).setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

//-------------------------------------------- ReDirect/ Intent ----------------------------------------------------------------------------------------------------------------------------------------

    private void reDirectTimeLine() {
        Intent intent = new Intent(MapsActivity.this, TimeLineMapsActivity.class);
        startActivity(intent);
    }

    private void reDirectBackupManger() {
        Intent intent = new Intent(MapsActivity.this, BackupManagerActivity.class);
        startActivity(intent);
    }

    private void reDirectSetting() {
        Intent intent = new Intent(MapsActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void redirectToFormDetails(ProjectLayerModel binLayer, JsonArray geomArray) {
        Intent intent = new Intent(MapsActivity.this, FormDetailsActivity.class);
        intent.putExtra(Utility.PASS_LAYER_ID      , binLayer.getLayerID());
        intent.putExtra(Utility.PASS_FORM_ID       , binLayer.getFormID());
        intent.putExtra(Utility.PASS_WORK_ID       , binLayer.getWorkID());
        intent.putExtra(Utility.PASS_PROJECT_ID    , binLayer.getProjectID());
        intent.putExtra(Utility.PASS_LINE_COLOR    , binLayer.getLayerLineColor());
        intent.putExtra(Utility.PASS_LINE_TYPE     , binLayer.getLayerLineType());
        intent.putExtra(Utility.PASS_ICON          , binLayer.getLayerIcon());
        intent.putExtra(Utility.PASS_ICON_WIDTH    , binLayer.getLayerIconWidth());
        intent.putExtra(Utility.PASS_ICON_HEIGHT   , binLayer.getLayerIconHeight());
        intent.putExtra(Utility.PASS_LAYER_TYPE    , binLayer.getLayerType());
        intent.putExtra(Utility.PASS_FORM_BG_COLOR , binLayer.getFormbg_color());
        intent.putExtra(Utility.PASS_FORM_LOGO     , binLayer.getForm_logo());
        intent.putExtra(Utility.PASS_FORM_SNO      , binLayer.getForm_sno());
        intent.putExtra(Utility.PASS_GEOM_ARRAY    , ""+ geomArray);
        if(selectedFeature.equalsIgnoreCase(getResources().getString(R.string.WalkingPolygone))){
            selectedFeature = getResources().getString(R.string.Polygone);
            intent.putExtra(Utility.PASS_GEOM_TYPE, "" + selectedFeature);
        }
        else{
            intent.putExtra(Utility.PASS_GEOM_TYPE, "" + selectedFeature);
        }
        startActivityForResult(intent, 1001);
    }


    private void redirectToProjectLayersList() {
        mDrawer.closeDrawer(GravityCompat.START);
        startActivityForResult(new Intent(mActivity, LayersActivity.class), REQUEST_CODES.LAYERS_LIST);
    }

    private void redirectToCompassActivity() {
        startActivity(new Intent(mActivity, MainCompassActivity.class));
    }

    private void redirectToDashBoardActivity() {
        startActivity(new Intent(mActivity, DashBoardBNActivity.class));
        finish();
    }


    private void redirectToProjectList() {
        mDrawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent(MapsActivity.this, ProjectActivity.class);
        startActivityForResult(intent, REQUEST_CODES.PROJECT_LIST);
    }

    public String getProject_id() {
        return project_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public String getForm_name() {
        return form_name;
    }

//------------------------------------------------------- On Request Permission ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (assistant.onPermissionsUpdated(requestCode, grantResults)) {

        }
    }

//------------------------------------------------------- On Activity Result ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Form Successfully Submit or not!
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Received form response
            clearDrawing();
            updateCheckMarkFloatingButton(false, "");
            GetSurveyFormData();
        }
        // Project List
        else if (requestCode == REQUEST_CODES.PROJECT_LIST && resultCode == RESULT_OK && data != null) {

            if (data.getExtras() != null && data.getExtras().containsKey(keyParam_data)) {
                ProjectDTO binProject = new Gson().fromJson(data.getExtras().getString(keyParam_data), ProjectDTO.class);
                if (!Utility.getSavedData(mActivity, Utility.KEY_PROJECT_ID).equalsIgnoreCase(binProject.getId())) {
                    // Making sure selected project is not same as old selected project.
                    Utility.saveData(mActivity, Utility.KEY_PROJECT_ID, binProject.getId());
                    Utility.saveData(mActivity, Utility.KEY_PROJECT_NAME, binProject.getProject());
                    Utility.saveData(mActivity, Utility.KEY_PROJECT_LAT, binProject.getLatitude());
                    Utility.saveData(mActivity, Utility.KEY_PROJECT_LONG, binProject.getLongitude());
                    if (toolbar != null)
                        toolbar.setTitle(binProject.getProject());
                    Utility.showToast(mActivity, "Project Activated: " + Utility.getSavedData(mActivity, Utility.KEY_PROJECT_NAME), null);
                    initExtra();//loading GeoJson
                }
            }
        }
        // Adapter Layer List
        else if (requestCode == REQUEST_CODES.LAYERS_LIST && resultCode == RESULT_OK) {
            listLayersReadOnly.clear();
            listLayersReadOnly.addAll(dataBaseHelper.getProjectLayersList(Utility.getSavedData(this, Utility.PROJECT_WORK_ID), false));
            renderLayersOffline(listLayersReadOnly, true, true, true);

        }
        // Map Camera Image
        else if(requestCode == MAP_CAMERA_REQUESTCODE && resultCode == RESULT_OK){
            // when user save image
            removeCameraPoint();
            Log.e(TAG,"Map Image Upload Save Successfully");
            readMapCameraImageLocal();
            // Read Form Server;
            readMapCameraImageS();
        }

    }

//------------------------------------------------------- On Resume ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        assistant.start();
    }

//------------------------------------------------------- On Destroy ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Maps Activity OnDestroy");
        if (Utility.getBooleanSavedData(this, Utility.IS_USER_TRACKING)) {

            if(geoFence.size() > 0){
                if(mCurrentLocation != null){
                    if(!isUserInsideBoundary(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()))){
                        // Log.e("Zone","User OutSide Zone");
                        Utility.saveData(this,Utility.INSIDE_ZONE, false);
                    }
                    else{
                        // Log.e("Zone","User InSide Zone");
                        Utility.saveData(this,Utility.INSIDE_ZONE, true);
                    }
                }
            }
            else{
                Utility.saveData(this,Utility.INSIDE_ZONE, true);
            }
        }
    }

//------------------------------------------------------------ On Pause ----------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        assistant.stop();
        dismissRevokeDialog();
        if (listLayersReadOnly.size() > 0) {
            dataBaseHelper.updateProjectLayersData(listLayersReadOnly);
        }
        super.onPause();
    }

//------------------------------------------------------------ On Back Pressed ----------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if (isMeasurementModeON) {
            Utility.showDoubleBtnDialog(mActivity, "Alert!!", "Close measurement ?", "Yes", "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            clearDrawing();
                            turnMeasurementMode(false);
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
        // then
        else if(cameraPointMarker != null || isMapCameraOn ){
            //Toast.makeText(baseApplication, "camera", Toast.LENGTH_SHORT).show();
            Utility.showDoubleBtnDialog(mActivity, "Alert!!", "There are unsaved changes. Discard anyway ?", "Yes", "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            clearDrawing();
                            updateCheckMarkFloatingButton(false, "");
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
        else if (isDrawing) {
            //Toast.makeText(mActivity, "drawing", Toast.LENGTH_SHORT).show();
            Utility.showDoubleBtnDialog(mActivity, "Alert!!", "There are unsaved changes. Discard anyway ?", "Yes", "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            clearDrawing();
                            updateCheckMarkFloatingButton(false, "");
                            dialog.dismiss();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
        else if (rcvLayers.getVisibility() == View.VISIBLE)
            updateVisibilityOfLayersSettings(false);
        else
            super.onBackPressed();
    }

//------------------------------------------------------- Interface ---------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------- Interface Request codes---------------------------------------------------------------------------------------------------------------------------------

    private interface REQUEST_CODES {
        int PROJECT_LIST = 6789;
        int LAYERS_LIST = 4567;
        int FORM = 1001;
    }
//------------------------------------------------------- Interface Layer type ---------------------------------------------------------------------------------------------------------------------------------

    public interface LAYER_TYPE {
        String Point = "Point";
        String Line = "LineString";
        String Polygon = "Polygon";
    }
//------------------------------------------------------- Interface Layer Line type ---------------------------------------------------------------------------------------------------------------------------------

    public interface LAYER_LINE_TYPE {
        String Normal    = "1";
        String SmallDash = "2";
        String LargeDash = "3";
    }
//------------------------------------------------------- decode Base64 Image ---------------------------------------------------------------------------------------------------------------------------------

    private Bitmap decodeBase64Image(String base64Image){
        // decode base64 string
        byte[] bytes= Base64.decode(base64Image,Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

//------------------------------------------------------- Sync Form Data ---------------------------------------------------------------------------------------------------------------------------------

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
            //Log.e(TAG, "Sync Service No Data Found in Local DataBase");
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

    private class MapCameraSyncToServe extends AsyncTask<Void, Integer, String> {

        public MapCameraSyncToServe() {
            isMapCameraSync = false;
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
                                //Log.e(TAG,"Logout Sync Map Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        //Log.e(TAG, "Logout Sync Delete Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        //Log.e(TAG, "Logout Sync Delete Duplicate Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    dismissMyDialog();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dismissMyDialog();
                            }
                        }
                    }
                    else {
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                }
            } catch (IOException e) {
                responseString = e.toString();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            String response = AESCrypt.decryptResponse(result);
            if(!response.equals("")) {
                try {
                    JSONObject mLoginObj = new JSONObject(response);
                    String status = mLoginObj.optString("status");
                    if (status.equalsIgnoreCase("Success")) {
                        isMapCameraSync = true;
                       // Log.e(TAG, "Sync Map Camera Off");
                        if(isFormSync && isMapCameraSync){
                           // Log.e(TAG, "Sync Complete");
                            Utility.getOKDialogBox(mActivity, "Sync", "Data Sync Successfully", dialog -> {
                                dialog.dismiss();
                                listLayersReadOnly.clear();
                                listLayersReadOnly.addAll(dataBaseHelper.getProjectLayersList(Utility.getSavedData(mActivity, Utility.PROJECT_WORK_ID), false));
                                renderLayersOffline(listLayersReadOnly, true, true, true);
                            });
                            // Progress Bar
                            dismissMyDialog();
                        }
                    }
                    else{
                        dismissMyDialog();
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                    dismissMyDialog();
                }
            }
            else{
                Log.e(TAG, response);
                dismissMyDialog();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- GetAllSurveyData ----------------------------------------------------------------------------------------------------------------------

    private void GetAllSurveyData(){
        ArrayList<ProjectLayerModel> surveyFormList = dataBaseHelper.getSurveyFormList(Utility.getSavedData(this,PROJECT_WORK_ID));
        //Log.e(TAG,"Survey Form List: "+surveyFormList.size());
        if(surveyFormList.size() > 0){
         //   startMyDialog("Work Record Fetch....");
            // Layer Array
            for(int i=0; i<surveyFormList.size(); i++){
                String type          = surveyFormList.get(i).getLayerType();
                String icon          = surveyFormList.get(i).getLayerIcon();
                String icon_width    = surveyFormList.get(i).getLayerIconWidth();
                String icon_height   = surveyFormList.get(i).getLayerIconHeight();
                String line_color    = surveyFormList.get(i).getLayerLineColor();
                String line_type     = surveyFormList.get(i).getLayerLineType();
                String form_bg_color = surveyFormList.get(i).getFormbg_color();
                String form_logo     = surveyFormList.get(i).getForm_logo();
                String form_sno      = surveyFormList.get(i).getForm_sno();

                //Log.e(TAG,i+" All Survey Data Form Bg Color: "+ form_bg_color);
                //Log.e(TAG,"All Survey Data Form Logo: "+ form_logo);
                //Log.e(TAG,i+" All Survey Data Form Sno: "+ form_sno);

                try
                {
                    // here -> form Data fetch
                    JSONArray jsonResponse = new JSONArray(surveyFormList.get(i).getFilledForms());
                    try {
                        Gson gson = new Gson();
                        java.lang.reflect.Type listType = new TypeToken<ArrayList<SurveyFormModel>>() {}.getType();
                        // Here Array of Form Fetch
                        ArrayList<SurveyFormModel> list = gson.fromJson(jsonResponse.toString(), listType);

                        //Log.e(TAG, i+" Form Size: "+list.size());

                        if(list.size() > 0){
                            DrawForms(list,type,icon,icon_width,icon_height,line_color,form_bg_color,form_logo,form_sno,line_type);
                        }
                        else{
                            dismissMyDialog();
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "Error Message: "+e.getMessage());
                        dismissMyDialog();
                        Utility.showInfoDialog(this, getResources().getString(R.string.lbl_Something_wrong_please_try_again));
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    //e.printStackTrace();
                    dismissMyDialog();
                }
            }
        }
        else{
            Log.e(TAG,"Form Not Found");
            dismissMyDialog();
        }

    }

//------------------------------------------------------- DrawForms ----------------------------------------------------------------------------------------------------------------------

    private void DrawForms(ArrayList<SurveyFormModel> list,String type, String icon, String icon_width, String icon_height, String line_color,String form_bg_color,String form_logo, String form_sno,String lineType){

        if(list.size() > 0){
            // Log.e(TAG,"Layer Type: "+ type + " Size: "+ list.size());
            for(int i=0; i<list.size(); i++){

                ArrayList<FormDetailData> formDetailDataArrayList = list.get(i).getForm();
                ArrayList<FormLatLon> formGeomArrayList = list.get(i).getForm_latlong();

                if(formDetailDataArrayList.size() > 0) {
                    // Point Layer --------------------------------------------------------------------------
                    if (type.equals(MapsActivity.LAYER_TYPE.Point)) {

                        if(formGeomArrayList.size() > 0){

                            if (!icon.equals("")) {
                                int width = icon_width.equals("")  ? 50 : Integer.parseInt(icon_width);
                                int height = icon_height.equals("") ? 50 : Integer.parseInt(icon_height);
                                try
                                {
                                    Bitmap bmp = Bitmap.createScaledBitmap(Utility.decodeBase64Image(icon), width, height, true);
                                    MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(formGeomArrayList.get(0).getLatitude(), formGeomArrayList.get(0).getLongitude())).draggable(false).icon(BitmapDescriptorFactory.fromBitmap(bmp));
                                    Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                    assert pointMarker != null;
                                    list.get(i).setForm_logo(form_logo);
                                    list.get(i).setFormbg_color(form_bg_color);
                                    list.get(i).setForm_sno(form_sno);
                                   // Log.e(TAG,"All Survey Data Point: "+ new Gson().toJson(list.get(i)));
                                    pointMarker.setTag(list.get(i));

                                }
                                catch (Exception e) {
                                    MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(formGeomArrayList.get(0).getLatitude(), formGeomArrayList.get(0).getLongitude())).draggable(false).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                    Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                    assert pointMarker != null;
                                    list.get(i).setForm_logo(form_logo);
                                    list.get(i).setFormbg_color(form_bg_color);
                                    list.get(i).setForm_sno(form_sno);
                                  //  Log.e(TAG,"All Survey Data Point: "+ new Gson().toJson(list.get(i)));
                                    pointMarker.setTag(list.get(i));
                                }
                            } else {
                                MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(formGeomArrayList.get(0).getLatitude(), formGeomArrayList.get(0).getLongitude())).draggable(false).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                assert pointMarker != null;
                                list.get(i).setForm_logo(form_logo);
                                list.get(i).setFormbg_color(form_bg_color);
                                list.get(i).setForm_sno(form_sno);
                               // Log.e(TAG,"All Survey Data Point: "+ new Gson().toJson(list.get(i)));
                                pointMarker.setTag(list.get(i));

                            }

                        }


                    }

                    // Line Layer ---------------------------------------------------------------------------
                    if (type.equals(MapsActivity.LAYER_TYPE.Line)) {
                        ArrayList<LatLng> latLongList = new ArrayList<>();

                        if(formDetailDataArrayList.size() > 0){
                            for(int j=0; j < formGeomArrayList.size(); j++){
                                latLongList.add(new LatLng(formGeomArrayList.get(j).getLatitude(), formGeomArrayList.get(j).getLongitude()));
                            }

                            String default_color = Utility.COLOR_CODE.BLUE;
                            String color = line_color;
                            try {
                                if (!Utility.isEmptyString(color) && color.startsWith("#")) {
                                    color = line_color;
                                }
                                else {
                                    color = default_color;
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                color = default_color;
                            }

                            Marker lineMarker = SystemUtility.addBigMarkerToMap(mMap, new LatLng(latLongList.get(0).latitude, latLongList.get(0).longitude), mActivity);
                            assert lineMarker != null;
                            list.get(i).setForm_logo(form_logo);
                            list.get(i).setFormbg_color(form_bg_color);
                            list.get(i).setForm_sno(form_sno);
                           // Log.e(TAG,"All Survey Data Line: "+ new Gson().toJson(list.get(i)));
                            lineMarker.setTag(list.get(i));

//
//                            Polyline polyline = mMap.addPolyline(new PolylineOptions()
//                                    .color(Color.parseColor(color))
//                                    .width(7f)
//                                    .clickable(true)
//                                    .startCap(new RoundCap())
//                                    .endCap(new RoundCap())
//                                    .jointType(JointType.ROUND)
//                                    .addAll(latLongList));

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.clickable(false);
                            polylineOptions.color(Color.parseColor(color));
                            polylineOptions.width(7f);

                            if(!Utility.isEmptyString(lineType)){

                                switch (lineType) {

                                    case LAYER_LINE_TYPE.Normal:
                                        polylineOptions.startCap(new RoundCap());
                                        polylineOptions.endCap(new RoundCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                        break;

                                    case LAYER_LINE_TYPE.SmallDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;

                                    case LAYER_LINE_TYPE.LargeDash:
                                        polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                                        polylineOptions.jointType(JointType.BEVEL);
                                        break;
                                }
                            }
                            // Line Type is Empty then
                            else{
                                polylineOptions.startCap(new RoundCap());
                                polylineOptions.endCap(new RoundCap());
                                polylineOptions.jointType(JointType.ROUND);
                            }
                            polylineOptions.addAll(latLongList);
                            polylineOptions.geodesic(true);
                            Polyline mPolyline = mMap.addPolyline(polylineOptions);
                        }
                    }

                    // Polygon ------------------------------------------------------------------------------
                    if(type.equals(MapsActivity.LAYER_TYPE.Polygon)){

                        ArrayList<LatLng> latLongList = new ArrayList<>();

                        if(formDetailDataArrayList.size() > 0){

                            for(int j=0; j < formGeomArrayList.size(); j++){
                                latLongList.add(new LatLng(formGeomArrayList.get(j).getLatitude(), formGeomArrayList.get(j).getLongitude()));
                            }
                            String default_color = Utility.COLOR_CODE.YELLOW;
                            String color = line_color;
                            String color_transparent = color.replace("#", "#4D");
                            try {
                                if (!Utility.isEmptyString(color) && color.startsWith("#")) {
                                    color = line_color;
                                    color_transparent = color.replace("#", "#4D");
                                }else {
                                    color = default_color;
                                    color_transparent = color.replace("#", "#4D");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                color = default_color;
                                color_transparent = color.replace("#", "#4D");
                            }
                            // Polygon
                            Polygon polygon = mMap.addPolygon(new PolygonOptions()
                                    .clickable(false)
                                    .addAll(latLongList)
                                    .strokeWidth(SystemUtility.getLineSize(mActivity))
                                    .strokeColor(Color.parseColor(color))
                                    .fillColor(Color.parseColor(color_transparent)));

                            // Polygon Center LatLong
                            LatLng centerLatLong = getPolygonCenterPoint(latLongList);
                            // Marker
                            Marker polygonMarker = SystemUtility.addBigMarkerToMap(mMap, centerLatLong, mActivity);
                            list.get(i).setForm_logo(form_logo);
                            list.get(i).setFormbg_color(form_bg_color);
                            list.get(i).setForm_sno(form_sno);
                           // Log.e(TAG,"All Survey Data Polygon: "+ new Gson().toJson(list.get(i)));
                            polygonMarker.setTag(list.get(i));

                        }
                    }
                }
            }
            dismissMyDialog();
        }
        else{
            dismissMyDialog();
        }

    }

    // get Polygon Center Point
    public LatLng getPolygonCenterPoint(ArrayList<LatLng> polygonPointsList){
        LatLng centerLatLng;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++) { builder.include(polygonPointsList.get(i)); }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();
        return centerLatLng;
    }

//------------------------------------------------------- OnSurveyMarkerClick ----------------------------------------------------------------------------------------------------------------------

    private void OnSurveyMarkerClick(Marker marker){

            try{
                SurveyFormModel surveyFormModel = (SurveyFormModel) marker.getTag();
                assert surveyFormModel != null;
                // Dialog Box
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.survey_custom_marker_view);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                // Close Button
                Button surveyFormCloseButton = dialog.findViewById(R.id.surveyFormCloseButton);
                surveyFormCloseButton.setOnClickListener(v -> dialog.dismiss());
                // RecycleView
                RecyclerView surveyFormRecycleView = dialog.findViewById(R.id.surveyFormRecycleView);

                LinearLayout surveyFormLogoLayout = dialog.findViewById(R.id.surveyFormLogoLayout);
                RelativeLayout surveyFormLayout = dialog.findViewById(R.id.surveyFormLayout);
                ImageView surveyFormLogo = dialog.findViewById(R.id.surveyFormLogo);
                surveyFormLogoLayout.setVisibility(View.GONE);

                if(!Utility.isEmptyString(surveyFormModel.getFormbg_color())){
                    int color = Color.parseColor(surveyFormModel.getFormbg_color());
                    surveyFormLayout.setBackgroundColor(color);
                }
                else{
                    surveyFormLayout.setBackgroundColor(Color.WHITE);
                }
                // Form Logo
                if(!Utility.isEmptyString(surveyFormModel.getForm_logo())){
                    surveyFormLogoLayout.setVisibility(View.VISIBLE);
                    try{
                        // Set profile Image
                        Glide.with(this).load(Utility.decodeBase64Image(surveyFormModel.getForm_logo())).error(R.drawable.icon_error).into(surveyFormLogo);
                    }
                    catch (Exception e){
                        // Set profile Image
                        Glide.with(this).load(R.drawable.icon_error).into(surveyFormLogo);
                        Log.e(TAG, e.getMessage());
                    }
                }
                else{
                    surveyFormLogoLayout.setVisibility(View.GONE);
                }
                if(!Utility.isEmptyString(surveyFormModel.getForm_sno())){
                    Utility.saveData(this,Utility.SURVEY_FORM_SNO, surveyFormModel.getForm_sno().equalsIgnoreCase("t"));
                }
                else{
                    Utility.saveData(this,Utility.SURVEY_FORM_SNO, false);
                }
                // Adapter
                AdapterSurveyMarker adapterSurveyMarker = new AdapterSurveyMarker(this,surveyFormModel.getForm());
                // Set Adapter
                surveyFormRecycleView.setAdapter(adapterSurveyMarker);
                // Set Layout
                surveyFormRecycleView.setLayoutManager(new LinearLayoutManager(this));
                // Dialog Box Show
                dialog.show();
            }
            catch (Exception e){
                Log.e("SurveyActivity", e.getMessage());
            }

    }

//------------------------------------------------------- onMarkerClick ----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        try{
            if(marker.getTag() instanceof  String){
                Log.e(TAG, "Instance of String");
                // if it is cameraPoint String then
                if(((String) marker.getTag()).equalsIgnoreCase(getResources().getString(R.string.cameraPoint))){
                    Utility.getYesNoDialogBox(mActivity, "Capture", "Are you sure u want to capture this place?", new Utility.DialogBoxOKClick() {
                        @Override
                        public void OkClick(DialogInterface dialog) {
                            // yes then open map camera option
                            reDirectMapCameraActivity();
                            dialog.dismiss();
                        }
                    });
                }

            }
            else if(marker.getTag() instanceof TimeLineModel){
                Log.e(TAG, "Instance of MapCameraModel");
                onMapCameraMarkerClick(marker);
            }
            else if(marker.getTag() instanceof TimeLineSModel){
                Log.e(TAG, "Instance of MapCameraSModel");
                onMapCameraSMarkerClick(marker);
            }
            else{
                //if(marker.getTag() instanceof SurveyFormModel){
                    OnSurveyMarkerClick(marker);
                //}
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

//------------------------------------------------------- GetSurveyFormData ----------------------------------------------------------------------------------------------------------------------

    // Online Form with offline Form
    private void GetSurveyFormData(){
        try{
            ArrayList<FormDataModel> formDataList = dataBaseHelper.getProjectSurveyFormListLocal(Utility.getSavedData(this,PROJECT_WORK_ID));
            if(formDataList.size() > 0){
             //   startMyDialog("Work Record Fetch..");
                for(int i=0; i<formDataList.size(); i++){
                    FormDataModel formDataModel = formDataList.get(i);
                    Log.e(TAG, formDataModel.getFormData());
                    // Convert!
                    SurveyFormLocalModel formDetailDataLocalList = Utility.convertStringToSurveyFormLocalModel(formDataModel.getFormData());
                    ArrayList<FormDetailData> formDetailDataArrayList = formDetailDataLocalList.getForm_data();
                    SurveyFormModel surveyFormModel = new SurveyFormModel();
                    surveyFormModel.setForm(formDetailDataArrayList);
                    surveyFormModel.setForm_sno(formDetailDataLocalList.getForm_sno());
                    surveyFormModel.setForm_logo(formDetailDataLocalList.getForm_logo());
                    surveyFormModel.setFormbg_color(formDetailDataLocalList.getFormbg_color());

                    if(formDetailDataArrayList.size() > 0) {
                        // Point Layer --------------------------------------------------------------------------
                        if (formDetailDataLocalList.getType().equals(MapsActivity.LAYER_TYPE.Point)) {
                            ArrayList<FormLatLon> geomArray = Utility.convertStringToFormLatLon(formDetailDataLocalList.getOther_data().getGeom_array());

                            if(geomArray.size() > 0){

                                if (!formDataModel.getIcon().equals("")) {
                                    int width = formDetailDataLocalList.getIcon_width().equals("")  ? 50 : Integer.parseInt(formDetailDataLocalList.getIcon_width());
                                    int height = formDetailDataLocalList.getIcon_height().equals("") ? 50 : Integer.parseInt(formDetailDataLocalList.getIcon_height());
                                    try {
                                        Bitmap bmp = Bitmap.createScaledBitmap(Utility.decodeBase64Image(formDataModel.getIcon()), width, height, true);
                                        MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(geomArray.get(0).getLatitude(), geomArray.get(0).getLongitude())).draggable(false).icon(BitmapDescriptorFactory.fromBitmap(bmp));
                                        Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                        assert pointMarker != null;
                                        pointMarker.setTag(surveyFormModel);
                                    }
                                    catch (Exception e) {
                                        MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(geomArray.get(0).getLatitude(), geomArray.get(0).getLongitude())).draggable(false).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                        Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                        assert pointMarker != null;
                                        pointMarker.setTag(surveyFormModel);
                                    }
                                } else {
                                    MarkerOptions pointMarkerOptions = new MarkerOptions().position(new LatLng(geomArray.get(0).getLatitude(), geomArray.get(0).getLongitude())).draggable(false).icon(SystemUtility.getRoundedMarkerIconGreen(mActivity));
                                    Marker pointMarker = mMap.addMarker(pointMarkerOptions);
                                    assert pointMarker != null;
                                    pointMarker.setTag(surveyFormModel);
                                }
                            }
                        }

                        // Line Layer ---------------------------------------------------------------------------
                        if (formDetailDataLocalList.getType().equals(MapsActivity.LAYER_TYPE.Line)) {
                            ArrayList<LatLng> latLongList = new ArrayList<>();
                            ArrayList<FormLatLon> geomArray = Utility.convertStringToFormLatLon(formDetailDataLocalList.getOther_data().getGeom_array());

                            if (formDetailDataArrayList.size() > 0) {

                                if(geomArray.size() > 0){

                                    for (int j = 0; j < geomArray.size(); j++) {
                                        latLongList.add(new LatLng(geomArray.get(j).getLatitude(), geomArray.get(j).getLongitude()));
                                    }

                                    String default_color = Utility.COLOR_CODE.BLUE;
                                    String color = formDetailDataLocalList.getLine_color();
                                    try {
                                        if (!Utility.isEmptyString(color) && color.startsWith("#")) {
                                            color = formDetailDataLocalList.getLine_color();
                                        } else {
                                            color = default_color;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        color = default_color;
                                    }

                                    Marker lineMarker = SystemUtility.addBigMarkerToMap(mMap, new LatLng(latLongList.get(0).latitude, latLongList.get(0).longitude), mActivity);
                                    assert lineMarker != null;
                                    lineMarker.setTag(surveyFormModel);
//                                Polyline polyline = mMap.addPolyline(new PolylineOptions()
//                                        .color(Color.parseColor(color))
//                                        .width(7f)
//                                        .clickable(true)
//                                        .startCap(new RoundCap())
//                                        .endCap(new RoundCap())
//                                        .jointType(JointType.ROUND)
//                                        .addAll(latLongList));

                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.clickable(false);
                                    polylineOptions.color(Color.parseColor(color));
                                    polylineOptions.width(7f);
                                    String line_type = formDetailDataLocalList.getLine_type();
                                    // Log.e(TAG,"Map Line Type: " + line_type);
                                    if(!Utility.isEmptyString(line_type)){

                                        switch (line_type) {

                                            case LAYER_LINE_TYPE.Normal:
                                                polylineOptions.startCap(new RoundCap());
                                                polylineOptions.endCap(new RoundCap());
                                                polylineOptions.jointType(JointType.ROUND);
                                                break;

                                            case LAYER_LINE_TYPE.SmallDash:
                                                polylineOptions.pattern(Arrays.asList(new Dash(smallDashLength), new Gap(smallDashGap)));
                                                polylineOptions.jointType(JointType.BEVEL);
                                                break;

                                            case LAYER_LINE_TYPE.LargeDash:
                                                polylineOptions.pattern(Arrays.asList(new Dash(largeDashLength), new Gap(largeDashGap)));
                                                polylineOptions.jointType(JointType.BEVEL);
                                                break;
                                        }
                                    }
                                    // Line Type is Empty then
                                    else{
                                        polylineOptions.startCap(new RoundCap());
                                        polylineOptions.endCap(new RoundCap());
                                        polylineOptions.jointType(JointType.ROUND);
                                    }
                                    polylineOptions.addAll(latLongList);
                                    polylineOptions.geodesic(true);
                                    Polyline mPolyline = mMap.addPolyline(polylineOptions);

                                }
                            }
                        }

                        // Polygon ------------------------------------------------------------------------------
                        if (formDetailDataLocalList.getType().equals(MapsActivity.LAYER_TYPE.Polygon)) {

                            ArrayList<LatLng> latLongList = new ArrayList<>();
                            ArrayList<FormLatLon> geomArray = Utility.convertStringToFormLatLon(formDetailDataLocalList.getOther_data().getGeom_array());

                            if (formDetailDataArrayList.size() > 0) {

                                if(geomArray.size() > 0){
                                    for (int j = 0; j < geomArray.size(); j++) {
                                        latLongList.add(new LatLng(geomArray.get(j).getLatitude(), geomArray.get(j).getLongitude()));
                                    }

                                    String default_color = Utility.COLOR_CODE.YELLOW;
                                    String color = formDetailDataLocalList.getLine_color();
                                    String color_transparent = color.replace("#", "#4D");
                                    try {
                                        if (!Utility.isEmptyString(color) && color.startsWith("#")) {
                                            color = formDetailDataLocalList.getLine_color();
                                            color_transparent = color.replace("#", "#4D");
                                        } else {
                                            color = default_color;
                                            color_transparent = color.replace("#", "#4D");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        color = default_color;
                                        color_transparent = color.replace("#", "#4D");
                                    }
                                    // Polygon
                                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                                            .clickable(false)
                                            .addAll(latLongList)
                                            .strokeWidth(SystemUtility.getLineSize(mActivity))
                                            .strokeColor(Color.parseColor(color))
                                            .fillColor(Color.parseColor(color_transparent)));

                                    // Polygon Center LatLong
                                    LatLng centerLatLong = getPolygonCenterPoint(latLongList);
                                    // Marker
                                    Marker polygonMarker = SystemUtility.addBigMarkerToMap(mMap, centerLatLong, mActivity);
                                    polygonMarker.setTag(surveyFormModel);
                                }
                            }

                        }

                    }

                }
                dismissMyDialog();
            }
            else{
                Log.e(TAG,"Survvey Form Data Not Found");
                dismissMyDialog();
            }
        }
        catch (Exception e){
            dismissMyDialog();
            Log.e(TAG, e.getMessage());
        }

    }

    private boolean isFormDataNotSync(){

        ArrayList<FormDataModel>      formDataList    = dataBaseHelper.getProjectFormList();
        ArrayList<FormDataModel>      gisFormDataList = dataBaseHelper.getGISSurveyFormList();
        ArrayList<GpsTrackingModule>  gpsTrackingList = dataBaseHelper.getGpsTracking();
       // ArrayList<TrackingStatusData> trackingList    = dataBaseHelper.getTrackingStatusDetails();
        ArrayList<CameraModule>       cameraImageList = dataBaseHelper.getCameraImage();
        ArrayList<CameraModule>       timeLineList    = dataBaseHelper.getTimeLineImage();
        ArrayList<CameraModule>       mapCameraList   = dataBaseHelper.getMapCameraImage();
        ArrayList<CameraModule>       gisCameraList   = dataBaseHelper.getGISMapCameraImage();

        // here when there is not data present in local database then logout directly!
       // if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0){
            // Log.e(TAG, "Sync Service No Data Found in Local DataBase");
            return false;
        }
        else{
            return true;
        }
    }

    private void drawGeoFence(){
        try{
            GeoFenceModel geoFenceDataList = dataBaseHelper.getGeoFenceListLocal(Utility.getSavedData(this,PROJECT_WORK_ID));
            String strokeColor = Utility.COLOR_CODE.RED;
            String fillColor   =  strokeColor.replace("#","#1D");
            ArrayList<LatLng> geoFenceLatLonArray = Utility.convertStringToGeoFenceLatLon(geoFenceDataList.getGeoFence());
            if(geoFenceLatLonArray.size() > 0){
                geoFence.addAll(geoFenceLatLonArray);
                polygonGeoFence = mMap.addPolygon(new PolygonOptions()
                        .clickable(false)
                        .addAll(geoFenceLatLonArray)
                        .strokeWidth(5f)
                        .strokeColor(Color.parseColor(strokeColor))
                        .fillColor(Color.parseColor(fillColor)));

                // Polygon Center LatLong
                LatLng centerLatLong = getPolygonCenterPoint(geoFenceLatLonArray);
                // Marker
            //    markerGeoFence = SystemUtility.addBigMarkerToMap(mMap, centerLatLong, mActivity);
              //  markerGeoFence.setTag(geoFenceDataList);
            }
        }
        catch (Exception e){
            dismissMyDialog();
            Log.e(TAG, e.getMessage());
        }


    }

    public boolean isUserInsideBoundary(LatLng current_location){
        boolean isInsideArea = false;
        GeoFenceModel geoFenceDataList = dataBaseHelper.getGeoFenceListLocal(Utility.getSavedData(this,PROJECT_WORK_ID));
        String strokeColor = Utility.COLOR_CODE.YELLOW;
        String fillColor   =  strokeColor.replace("#","#1D");
        ArrayList<LatLng> geoFenceLatLonArray = Utility.convertStringToGeoFenceLatLon(geoFenceDataList.getGeoFence());
        if(geoFenceLatLonArray.size() > 0){
            int cor_size = geoFenceLatLonArray.size();
            com.surveybaba.PolygonInsideOutSide.Point[] polygon_point = new com.surveybaba.PolygonInsideOutSide.Point[cor_size];
            for (int i = 0; i < cor_size; i++) {
                // lat lon
                polygon_point[i] = new com.surveybaba.PolygonInsideOutSide.Point(geoFenceLatLonArray.get(i).latitude, geoFenceLatLonArray.get(i).longitude);
            }
            com.surveybaba.PolygonInsideOutSide.Point p = new com.surveybaba.PolygonInsideOutSide.Point(current_location.latitude,current_location.longitude);
            // inside or not!
            if (PolygonInsideOutside.isInside(polygon_point, cor_size, p)) {
                // Here now we can find the area.
                Log.e("Zone","Contain in polyline");
                isInsideArea = true;
            }
            else{
                Log.e("Zone","not Contain polyline");
                isInsideArea = false;
            }

        }
        else{
            Log.e("Zone","data not found");
            isInsideArea = true;
        }
        return isInsideArea;
    }

//------------------------------------------------------- Camera ----------------------------------------------------------------------------------------------------------------------

    private void rlCamera(){
        SystemPermission systemPermission = new SystemPermission(mActivity);
        if(systemPermission.isExternalStorage()){
            if(systemPermission.isCamera()){
                reDirectMapCameraActivity();
            }
            else{
                Toast.makeText(baseApplication, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void reDirectMapCameraActivity(){
        Intent intent = new Intent(mActivity, MapCameraActivity.class);
        if(cameraPointMarker != null){
            intent.putExtra(PASS_LAT,""+ cameraPointMarker.getPosition().latitude);
            intent.putExtra(PASS_LONG,""+ cameraPointMarker.getPosition().longitude);
            intent.putExtra(PASS_PROJECT_ID,"" + Utility.getSavedData(mActivity,PROJECT_PROJECT_ID));
            intent.putExtra(PASS_WORK_ID,"" +   Utility.getSavedData(mActivity, PROJECT_WORK_ID));
            startActivityForResult(intent, MAP_CAMERA_REQUESTCODE);
        }
    }

    // Read Data Form Loca Database
    private void readMapCameraImageLocal(){
        try{
        //    startMyDialog("Loading...");
            ArrayList<CameraModule> mapCameraDataLocalList = dataBaseHelper.getMapCameraImageLocal(Utility.getSavedData(this, PROJECT_WORK_ID));
            if(mapCameraDataLocalList.size() > 0){
                for(int i=0; i<mapCameraDataLocalList.size(); i++){
                    CameraModule data = mapCameraDataLocalList.get(i);
                    if(!Utility.isEmptyString(data.getImageLat()) && !Utility.isEmptyString(data.getImageLon())){
                        TimeLineModel dataModel = new TimeLineModel();
                        dataModel.setImagePath(data.getImagePath());
                        dataModel.setDescription(data.getImageDesc());
                        dataModel.setDatetime(data.getImageDateTime());
                        LatLng timeLineLatLng = new LatLng(Double.parseDouble(data.getImageLat()),Double.parseDouble(data.getImageLon()));
                        MarkerOptions timeLineMarkerOptions = new MarkerOptions().position(timeLineLatLng).draggable(false).icon(SystemUtility.BitmapFromVector(mActivity,R.drawable.icon_camera_marker));
                        Marker mapCameraMarker  = mMap.addMarker(timeLineMarkerOptions);
                        assert mapCameraMarker != null;
                        mapCameraMarker.setTag(dataModel);
                    }
                }
            }
            dismissMyDialog();
        }
        catch (Exception e){
            Log.e(TAG,e.getMessage());
            dismissMyDialog();
        }

    }

    // Read Data Form Server
    private void readMapCameraImageS(){
        try{
     //       startMyDialog("Loading...");
            ArrayList<CameraModule> timeLineDataSLocalList = dataBaseHelper.getMapCameraImageSLocal(Utility.getSavedData(this, PROJECT_WORK_ID));
            if(timeLineDataSLocalList.size() > 0) {
                for (int i = 0; i < timeLineDataSLocalList.size(); i++) {
                    CameraModule data = timeLineDataSLocalList.get(i);
                    if (!Utility.isEmptyString(data.getImageLat()) && !Utility.isEmptyString(data.getImageLon())) {
                        TimeLineSModel dataSModel = new TimeLineSModel();
                        dataSModel.setImagePath(data.getImagePath());
                        dataSModel.setDescription(data.getImageDesc());
                        dataSModel.setDatetime(data.getImageDateTime());
                        LatLng timeLineLatLng = new LatLng(Double.parseDouble(data.getImageLat()), Double.parseDouble(data.getImageLon()));
                        MarkerOptions timeLineMarkerOptions = new MarkerOptions().position(timeLineLatLng).draggable(false).icon(SystemUtility.BitmapFromVector(mActivity,R.drawable.icon_camera_marker));
                        Marker mapCameraSMarker = mMap.addMarker(timeLineMarkerOptions);
                        assert mapCameraSMarker != null;
                        mapCameraSMarker.setTag(dataSModel);
                    }
                }
            }
            dismissMyDialog();
        }
        catch (Exception e){
            dismissMyDialog();
            Log.e(TAG, e.getMessage());
        }

    }

//------------------------------------------------------- onMapCameraImageMarkerClick ---------------------------------------------------------------------------------------------------------------------------

    private void onMapCameraMarkerClick(Marker marker){

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
            try{
                Glide.with(mActivity).load(timeLineModel.getImagePath()).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
                Glide.with(mActivity).load(R.drawable.icon_no_image).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            }
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
                try{
                    Glide.with(mActivity).load(timeLineModel.getImagePath()).error(R.drawable.icon_no_image).into(imageView);
                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                    Glide.with(mActivity).load(R.drawable.icon_no_image).error(R.drawable.icon_no_image).into(imageView);
                }
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
    private void onMapCameraSMarkerClick(Marker marker){

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
            try{
                Glide.with(mActivity).load(Utility.decodeBase64Image(timeLineSModel.getImagePath())).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
            }catch (Exception e){
                Glide.with(mActivity).load(R.drawable.icon_no_image).placeholder(R.drawable.image_load_bar).error(R.drawable.icon_no_image).into(timelineMarkerImage);
                Log.e(TAG,e.getMessage());
            }
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
                try{
                    Glide.with(mActivity).load(Utility.decodeBase64Image(timeLineSModel.getImagePath())).error(R.drawable.icon_no_image).into(imageView);
                }
                catch (Exception e){
                    Glide.with(mActivity).load(R.drawable.icon_no_image).error(R.drawable.icon_no_image).into(imageView);
                }
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

    private void removeCameraPoint(){
        if(cameraPointMarker != null){
            cameraPointMarker.remove();
            cameraPointMarker = null;
        }
        cameraPointMarker = null;
        cameraPointCounter = 0;
        isMapCameraOn = false;
        selectedFeature = "";
        updateCheckMarkFloatingButton(false, "");
        clearDrawing();
    }

    private void loadLocalData(){
      //  new Thread(new Runnable() {
     //       @Override
     //       public void run() {
                //  Log.e(TAG, "Draw GeoFence");
                drawGeoFence();
                // Log.e(TAG, "Get All Survey Data");
                GetAllSurveyData();
                //  Log.e(TAG, "Get Survey Form Data");
                GetSurveyFormData();
                //   Log.e(TAG, "Get Map Camera Local  Data");
                readMapCameraImageLocal();
                //   Log.e(TAG, "Get Map Camera S Data");
                readMapCameraImageS();
       //     }
     //   }).start();
    }
}