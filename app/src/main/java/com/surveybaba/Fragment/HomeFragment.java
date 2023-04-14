package com.surveybaba.Fragment;

import static com.surveybaba.Utilities.Utility.getUploadFilePath;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.duy.compass.MainCompassActivity;
import com.fileupload.AndroidMultiPartEntity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.surveybaba.ADAPTER.AdapterDashboard;
import com.surveybaba.AESCrypt.AESCrypt;
import com.surveybaba.FormBuilder.FormDataModel;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.LocationAssist.LocationAssistant;
import com.surveybaba.Modules.BluetoothModule.BluetoothActivity;
import com.surveybaba.Modules.BusinessHubModule.BusinessHubActivity;
import com.surveybaba.Modules.CameraActivity;
import com.surveybaba.Database.DataBaseHelper;
import com.surveybaba.Modules.GISSurveyModule.SurveyResurveyActivity;
import com.surveybaba.Modules.GPSTrackingActivity;
import com.surveybaba.Modules.GPSTrackingModule.GPSTrackingMapActivity;
import com.surveybaba.Modules.ProjectModule.ProjectResurveyActivity;
import com.surveybaba.Modules.TimeLineMapActivity;
import com.surveybaba.R;
import com.surveybaba.SplashActivity;
import com.surveybaba.Utilities.SystemPermission;
import com.surveybaba.Utilities.SystemUtility;
import com.surveybaba.Utilities.Utility;
import com.surveybaba.model.CameraModule;
import com.surveybaba.model.DashBoardModule;
import com.surveybaba.model.GpsTrackingModule;
import com.surveybaba.model.ProjectLayerModel;
import com.surveybaba.model.TrackingStatusData;
import com.surveybaba.service.SyncService;
import com.surveybaba.setting.SettingActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener, AdapterDashboard.AdapterDashBoardListener, WSResponseInterface, LocationAssistant.Listener {

    // TAG
    private static final String TAG = "HomeFragment";

    // Database
    private DataBaseHelper dataBaseHelper;
    // Base Application
    BaseApplication baseApplication;
    // Location
    private LocationAssistant assistant;
    // View
    private View view;
    // Toolbar
    Toolbar toolbar;
    // Navigation bar
    ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout dashboardDrawerLayout;
    private NavigationView navigationView;

    TextView tvUsername, tvUserEmailID;
    ImageView imgUserProfileImage;

    // Floating Button
    FloatingActionButton floatingActionButton;

    LinearLayout llSetting, llLogout,llCompass,llGpsSatellite, llSync;
    // Recycle View
    RecyclerView dashBoardRecycleView;
    // DashBoard Modules List
    ArrayList<DashBoardModule> dashBoardModulesList = new ArrayList<>();
    // ProgressDialog
    private ProgressDialog progressDialog;

    // Sync Data
    public static boolean isSyncOn = false;
    long totalSize = 0;
    private static boolean isOnlineLayerSync = true;
    private static boolean isCameraSync      = true;
    private static boolean isGPSTrackingSync = true;
    private static boolean isFormSync        = true;
    private static boolean isTrackingSync    = true;
    private static boolean isTimeLineSync    = true;
    private static boolean isMapCameraSync   = true;
    private static boolean isGISCameraSync   = true;
    private static boolean isErrorInApiOccurs = false;
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
    ArrayList<TrackingStatusData> syncTrackingList = new ArrayList<>();
    TrackingStatusData trackingStatusModel;

    public static final String TYPE_FILE   = "file";
    public static final String TYPE_CAMERA = "cameraUploader";
    public static final String TYPE_VIDEO  = "videoUploader";
    public static final String TYPE_AUDIO  = "audioUploader";
    public static boolean isFileUpload   = true;
    public static boolean isCameraUpload = true;
    public static boolean isVideoUpload  = true;
    public static boolean isAudioUpload  = true;

//------------------------------------------------------- Constructor -------------------------------------------------------------------------------------------------------------

    public HomeFragment() {}

//------------------------------------------------------- On Create View ----------------------------------------------------------------------------------------------------------

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Toolbar
        toolbar = view.findViewById(R.id.dashboard_toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);

        baseApplication = (BaseApplication) requireActivity().getApplication();
        // Location
        assistant = new LocationAssistant(getContext(), this, LocationAssistant.Accuracy.HIGH, 0, false);
        assistant.setVerbose(true);
        // Init
        init();
        // Set onClickListener
        setOnclickListener();
        // Init Slider Bar Button
        initSliderBarButton();
        // Database Init
        dataBaseHelper = new DataBaseHelper(getContext());

        // Navigation Toggle Button
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), dashboardDrawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        // Set Toggle color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(requireContext().getColor(R.color.white));
        } else {
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(requireActivity(), R.color.white));
        }
        dashboardDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        // Bottom Navigation View
        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        navView.setBackground(null);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_dash_board_bnactivity);
        NavigationUI.setupWithNavController(navView, navController);

        DynamicDashBoard();

        SystemPermission systemPermission = new SystemPermission(requireActivity());
        if(systemPermission.isLocation()){
            baseApplication.startMyService();
        }

        logoutAfter24hr();

        return view;
    }

//------------------------------------------------------- Init ------------------------------------------------------------------------------------------------------------------

    private void init(){
        dashboardDrawerLayout = view.findViewById(R.id.dashboard_drawer_layout);
        navigationView = view.findViewById(R.id.nav_slider_bar);
        floatingActionButton = view.findViewById(R.id.mfloatingbutton);
        dashBoardRecycleView = view.findViewById(R.id.dashBoardRecycleView);
    }

//------------------------------------------------------- Init Slider Bar Button ---------------------------------------------------------------------------------------------------

    private void initSliderBarButton() {
        // navigation slider init!
        View hView = navigationView.getHeaderView(0);

        tvUsername = hView.findViewById(R.id.tvUsername);
        tvUserEmailID = hView.findViewById(R.id.tvUserEmailID);
        imgUserProfileImage = hView.findViewById(R.id.UserProfileImage);

        llCompass = hView.findViewById(R.id.llCompass);
        llGpsSatellite = hView.findViewById(R.id.llGpsSatellite);
        llSetting = hView.findViewById(R.id.llSetting);
        llLogout = hView.findViewById(R.id.llLogout);
        llSync = hView.findViewById(R.id.llSync);

        // Set User Name
        tvUsername.setText(Utility.getSavedData(requireContext(), Utility.PROFILE_FIRSTNAME));
        // Set User Email ID
        tvUserEmailID.setText(Utility.getSavedData(requireContext(), Utility.PROFILE_EMAILID));

        try{
            // Set profile Image
            Glide.with(requireContext()).load(decodeBase64Image(Utility.getSavedData(requireContext(), Utility.PROFILE_IMAGE))).error(R.drawable.ic_login_user_icon).circleCrop().into(imgUserProfileImage);
        }
        catch (Exception e){
            // Set profile Image
            Glide.with(requireContext()).load(R.drawable.ic_login_user_icon).circleCrop().into(imgUserProfileImage);
            Log.e(TAG, e.getMessage());
        }

        llCompass.setOnClickListener(clickSlidebar);
        llGpsSatellite.setOnClickListener(clickSlidebar);
        llSetting.setOnClickListener(clickSlidebar);
        llLogout.setOnClickListener(clickSlidebar);
        llSync.setOnClickListener(clickSlidebar);
        llSync.setVisibility(View.VISIBLE);
    }

    private Bitmap decodeBase64Image(String base64Image) {
        // decode base64 string
        byte[] bytes = Base64.decode(base64Image, Base64.DEFAULT);
        // Initialize bitmap
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

//------------------------------------------------------- Set on Click Listener -------------------------------------------------------------------------------------------------------

    private void setOnclickListener() {
        floatingActionButton.setOnClickListener(this);
    }

//------------------------------------------------------- RecycleView ----------------------------------------------------------------------------------------------------------------

    private void DynamicDashBoard() {
        String module = Utility.getSavedData(requireContext(), Utility.MODULES);
        if (dashBoardModulesList.size() != 0) {
            dashBoardModulesList.clear();
        }
        if(!module.equals("")){

            for(int i=0; i<module.split(",").length; i++){
                String module_name = module.split(",")[i].split("#")[0];
                String modules     = module.split(",")[i].split("#")[1];
                dashBoardModulesList.add(new DashBoardModule(module_name,modules));
            }
        }
        // is Admin panel
//        if(Utility.getBooleanSavedData(requireContext(), Utility.isAdmin)){
//           dashBoardModulesList.add(AdapterDashboard.PROJECT_MANAGEMENT_MODULES);
//           dashBoardModulesList.add(AdapterDashboard.Users);
//        }
//        else{
//
//        }
        // Default Module Add!
        dashBoardModulesList.add(new DashBoardModule(AdapterDashboard.CAMERA      , AdapterDashboard.CAMERA));
        dashBoardModulesList.add(new DashBoardModule(AdapterDashboard.TIMELINE    , AdapterDashboard.TIMELINE));
        dashBoardModulesList.add(new DashBoardModule(AdapterDashboard.BUSINESS_HUB, AdapterDashboard.BUSINESS_HUB));
        dashBoardModulesList.add(new DashBoardModule(AdapterDashboard.BLUETOOTH   , AdapterDashboard.BLUETOOTH));
        dashBoardModulesList.add(new DashBoardModule(AdapterDashboard.SETTING     , AdapterDashboard.SETTING));
        AdapterDashboard adapterDashboard = new AdapterDashboard(getContext(), dashBoardModulesList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        dashBoardRecycleView.setLayoutManager(gridLayoutManager);
        dashBoardRecycleView.setAdapter(adapterDashboard);
    }

//------------------------------------------------------- On Click ----------------------------------------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
    }

//------------------------------------------------------- On Click Slider Bar Button Action ----------------------------------------------------------------------------------------------

    private final View.OnClickListener clickSlidebar = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int getid = v.getId();
            switch (getid) {

                // Compass
                case R.id.llCompass:
                    dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                    redirectToCompassActivity();
                    break;

                // Gps Satellite
                case R.id.llGpsSatellite:
                  //  redirectToGpsSatellite();
                   // dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                    break;

                // Setting
                case R.id.llSetting:
                    dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                    reDirectSetting();
                    break;

                // Sync
                case R.id.llSync:
                    dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                    if(SystemUtility.isInternetConnected(requireActivity())) {
                        SyncAllData();
                    }
                    else{
                        Utility.getOKDialogBox(requireContext(), "Sync Alert", "Need Internet Connection To Sync Data", DialogInterface::dismiss);
                    }
                    break;


                // Logout
                case R.id.llLogout:
                    isSyncOn = false;
                    dashboardDrawerLayout.closeDrawer(GravityCompat.START);
                    if(SystemPermission.isInternetConnected(requireContext())){
                        onClickLogout();
                    }
                    else{
                        isSyncOn = false;
                        Utility.getOKDialogBox(requireContext(), "Connection Error", "Need Internet Connection to Logout", DialogInterface::dismiss);
                    }
                    break;
            }
        }
    };

//------------------------------------------------------- On Click Logout ------------------------------------------------------------------------------------------------------------

    private void onClickLogout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(getResources().getString(R.string.lblAre_you_sure));
        alertDialog.setPositiveButton(getResources().getString(R.string.lblLogout), (dialog, which) -> {
            dialog.dismiss();
            processLogout();
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.lblCancel), null);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void processLogout() {
        // ProgressBar
        showProgressBar();
        Sync();
    }


//------------------------------------------------------- On Item Adapter Dashboard Click ----------------------------------------------------------------------------------------------------

    @Override
    public void OnItemClick(int position) {

        String module = dashBoardModulesList.get(position).getModule();

        switch (module) {

            case AdapterDashboard.SURVEY_MODULES:
                reDirectSurvey();
                break;

            case AdapterDashboard.PROJECT_MODULES:
                reDirectProject();
                break;

            case AdapterDashboard.GPS_TRACKING_MODULES:
                reDirectGPSTracking();
                break;

            case AdapterDashboard.GIS_SURVEY_MODULES:
                reDirectGISSurvey();
                break;

            case AdapterDashboard.MANAGE_ORGANISATION_MODULES:
                reDirectManageOrganisation();
                break;

            case AdapterDashboard.CAMERA:
                reDirectCamera();
                break;

            case AdapterDashboard.BLUETOOTH:
                reDirectBluetooth();
                break;

            case AdapterDashboard.SETTING:
                reDirectSetting();
                break;

            case AdapterDashboard.TIMELINE:
                reDirectTimeLine();
                break;

            case AdapterDashboard.BUSINESS_HUB:
                reDirectBusinessHub();
                break;

            case AdapterDashboard.Users:
                reDirectUser();
                break;


        }
    }

//------------------------------------------------------- Redirect to Activity -------------------------------------------------------------------------------------------------------

    private void reDirectUser(){
        // startActivity(new Intent(getActivity(), TimeLineMapActivity.class));
    }

    private void reDirectTimeLine() {
        startActivity(new Intent(getActivity(), TimeLineMapActivity.class));
    }

    private void reDirectBusinessHub(){
        startActivity(new Intent(getActivity(), BusinessHubActivity.class));
    }

    private void reDirectSetting() {
       startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    private void reDirectSurvey() {
        Toast.makeText(requireActivity(), AdapterDashboard.SURVEY_MODULES, Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(requireActivity(), SurveyActivity.class));
    }

    private void reDirectProject() {
        //startActivity(new Intent(requireActivity(), ProjectAssignActivity.class));
        startActivity(new Intent(requireActivity(), ProjectResurveyActivity.class));
    }

    private void reDirectGPSTracking() {
        //startActivity(new Intent(requireActivity(), GPSTrackingActivity.class));
        startActivity(new Intent(requireActivity(), GPSTrackingMapActivity.class));
    }

    private void reDirectGISSurvey() {
        startActivity(new Intent(requireActivity(), SurveyResurveyActivity.class));
        //Toast.makeText(requireContext(), AdapterDashboard.GIS_SURVEY_MODULES, Toast.LENGTH_SHORT).show();
    }

    private void reDirectManageOrganisation() {
        Toast.makeText(requireContext(), AdapterDashboard.MANAGE_ORGANISATION_MODULES, Toast.LENGTH_SHORT).show();
    }

    private void reDirectCamera() {
        startActivity(new Intent(requireActivity(), CameraActivity.class));
    }

    private void reDirectBluetooth() {
        startActivity(new Intent(requireActivity(), BluetoothActivity.class));
    }

    private void redirectToCompassActivity() {
        startActivity(new Intent(requireActivity(), MainCompassActivity.class));
    }

//------------------------------------------------------- ProgressBar Show/ Dismiss ------------------------------------------------------------------------------------------------------

    private void dismissProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading and Sync Data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }else {
            progressDialog.setMessage("Profile Uploading...");
        }
    }

//------------------------------------------------------- Sync All Data ------------------------------------------------------------------------------------------------------------------

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
            Utility.getOKDialogBox(requireContext(), "Sync", "Data Already Sync", DialogInterface::dismiss);
        }
        else{
            if(SystemPermission.isInternetConnected(requireActivity())){
                baseApplication.startSyncService();
            }
        }
    }

//------------------------------------------------------- Sync Data ------------------------------------------------------------------------------------------------------------------

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
                        j1.put("user_id",Utility.getSavedData(requireContext(),Utility.LOGGED_USERID));
                        JSONObject j = new JSONObject(list.get(i).getLatLong());
                        j1.put("latlong",j);
                        jsonArray.put(j1);
                    }
                    catch (Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
            //   Log.e(TAG,"Array -> "+ jsonArray.toString());
            if(jsonArray.length() > 0){
                onlineLayerList.add(jsonArray.toString());
            }
        }

        // here when there is not data present in local database then logout directly!
        if(cameraImageList.size() == 0 && gpsTrackingList.size() == 0 && formDataList.size() == 0 && trackingList.size() == 0 && timeLineList.size() == 0 && mapCameraList.size() == 0 && gisCameraList.size() == 0 && gisFormDataList.size() == 0 && onlineLayerList.size() == 0){
                // Remove All Database Data here!
                String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                Utility.clearData(requireActivity());
                Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                dataBaseHelper.open();
                dataBaseHelper.logout();
                dataBaseHelper.close();
                // Progress Bar
                dismissProgressBar();
                startActivity(new Intent(getActivity(), SplashActivity.class));
                Log.e(TAG, "Logout No Data Found in Local DataBase");
        }
        else
        {
            // local Database contain some Data!
            Log.e(TAG, "Logout Local Database Contain some Data");
            Log.e(TAG, (cameraImageList.size() > 0 ? "Logout Camera    Data present"    : "Logout Camera Data not Present"));
            Log.e(TAG, (gpsTrackingList.size() > 0 ? "Logout Gps Tracking Data present" : "Logout Gps Tracking Data not Present"));
            Log.e(TAG, (formDataList.size()    > 0 ? "Logout Form      Data present"    : "Logout Form Data not Present"));
            Log.e(TAG, (trackingList.size()    > 0 ? "Logout Tracking  Data present"    : "Logout Tracking Data not Present"));
            Log.e(TAG, (timeLineList.size()    > 0 ? "Logout TimeLine  Data present"    : "Logout TimeLine Data not Present"));
            Log.e(TAG, (mapCameraList.size()   > 0 ? "Logout MapCamera Data present"    : "Logout Map Camera Data not Present"));
            Log.e(TAG, (gisCameraList.size()   > 0 ? "Logout GISCamera Data present"    : "Logout GIS Camera Data not Present"));
            Log.e(TAG, (gisFormDataList.size() > 0 ? "Logout GIS Form  Data present"    : "Logout GIS Form Data not Present"));
            Log.e(TAG, (onlineLayerList.size() > 0 ? "Logout Service OnlineLayer Data present"  : "Logout Service OnlineLayer Data not Present"));

            if(cameraImageList.size() > 0 || gpsTrackingList.size() > 0 || formDataList.size() > 0 || trackingList.size() > 0 || timeLineList.size() > 0 || mapCameraList.size() > 0 || gisCameraList.size() > 0 || onlineLayerList.size() > 0){

                // Camera Data!
                if(cameraImageList.size() > 0){
                    Log.e(TAG, "Logout Sync Camera On");
                    new HomeFragment.CameraSyncToServe().execute();
                }
                else{
                    isCameraSync = true;
                }

                // GPS Tracking Data!
                if(gpsTrackingList.size() > 0){
                    Log.e(TAG, "Logout Sync GPS Tracking On");
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
                    Log.e(TAG, "Logout Sync Form On");
                    syncFormDataList = dataBaseHelper.getProjectFormList();
                    if(gisFormDataList.size() > 0){
                        syncFormDataList.addAll(dataBaseHelper.getGISSurveyFormList());
                    }
                    SyncFormDataDetails();
                }
                else{
                    isFormSync = true;
                }

                // Tracking Data!
                if(trackingList.size() > 0){
                    Log.e(TAG, "Logout Sync Tracking On");
                    syncTrackingList = dataBaseHelper.getTrackingStatusDetails();
                    SyncTrackingDataDetails();
                }
                else{
                    isTrackingSync = true;
                }

                // Time Line Data!
                if(timeLineList.size() > 0){
                    Log.e(TAG, "Logout Sync TimeLine On");
                    new HomeFragment.TimeLineSyncToServe().execute();
                }
                else{
                    isTimeLineSync = true;
                }

                // Map  Camera Data!
                if(mapCameraList.size() > 0){
                    Log.e(TAG, "Logout Sync MapCamera On");
                    new HomeFragment.MapCameraSyncToServe().execute();
                }
                else{
                    isMapCameraSync = true;
                }

                // GIS  Camera Data!
                if(gisCameraList.size() > 0){
                    Log.e(TAG, "Logout Sync GISCamera On");
                    new HomeFragment.GISCameraSyncToServe().execute();
                }
                else{
                    isGISCameraSync = true;
                }
            }
            else{
                    String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                    Utility.clearData(requireActivity());
                    Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                    // Remove All Database Data here!
                    dataBaseHelper.open();
                    dataBaseHelper.logout();
                    dataBaseHelper.close();
                    // Progress Bar
                    dismissProgressBar();
                    startActivity(new Intent(getActivity(), SplashActivity.class));
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
            Log.e(TAG, "Logout Sync Tracking Off");
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Logout Sync Data Save Successfully");
                String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                Utility.clearData(requireActivity());
                Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                // Remove All Database Data here!
                dataBaseHelper.open();
                dataBaseHelper.logout();
                dataBaseHelper.close();
                // Progress Bar
                dismissProgressBar();
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }

        }
    }
    private void SyncTrackingDataToServer(TrackingStatusData trackingStatusData){
        if(SystemUtility.isInternetConnected(requireContext())) {
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
            Log.e(TAG, "Form LayerID: "+formDataModel.getLayerID() + " Form ID: " + formDataModel.getFormID());
            syncFormDataList.remove(0);
            SyncFormToServer(formDataModel);
        }
        else{
            isFileUpload   = true;
            isCameraUpload = true;
            isVideoUpload  = true;
            isAudioUpload  = true;
            isFormSync     = true;
            Log.e(TAG, "Logout Sync Form Off");
            /// here no Data found in form
            if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync   && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                Log.e(TAG, "Logout Sync Data Save Successfully");
                String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                Utility.clearData(requireActivity());
                Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                // Remove All Database Data here!
                dataBaseHelper.open();
                dataBaseHelper.logout();
                dataBaseHelper.close();
                // Progress Bar
                dismissProgressBar();
                startActivity(new Intent(getActivity(), SplashActivity.class));
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
        BaseApplication.getInstance().makeHttpPostRequest(HomeFragment.this, responseCode, URL_Utility.WS_LAYER_FORM_UPLOAD, params, false, false);
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
            Log.e(TAG, "Logout Sync GPS Tracking Off");
                if(isCameraSync && isGPSTrackingSync && isFormSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                    Log.e(TAG, "Logout Sync Data Save Successfully");
                    String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                    Utility.clearData(requireActivity());
                    Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                    // Remove All Database Data here!
                    dataBaseHelper.open();
                    dataBaseHelper.logout();
                    dataBaseHelper.close();
                    // Progress Bar
                    dismissProgressBar();
                    startActivity(new Intent(getActivity(), SplashActivity.class));
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
            jsonObject.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(requireActivity(),Utility.LOGGED_TOKEN));
            jsonObject.put(URL_Utility.PARAM_LATLON,   gpsTrackingModule.getLatLong());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e(TAG, jsonObject.toString());
        try {
            data =  AESCrypt.encrypt(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("data",data);
        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD;
        BaseApplication.getInstance().makeHttpPostRequest(HomeFragment.this, responseCode, URL_Utility.WS_GPS_TRACKING_UPLOAD, params, false, false);
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
                Log.e(TAG, "Logout Sync Data Save Successfully");
                String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                Utility.clearData(requireActivity());
                Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                // Remove All Database Data here!
                dataBaseHelper.open();
                dataBaseHelper.logout();
                dataBaseHelper.close();
                // Progress Bar
                dismissProgressBar();
                startActivity(new Intent(getActivity(), SplashActivity.class));

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

        URL_Utility.ResponseCode responseCode = URL_Utility.ResponseCode.WS_ONLINE_LAYER;
        BaseApplication.getInstance().makeHttpPostRequest(this, responseCode, URL_Utility.WS_ONLINE_LAYER, params, false, false);

    }



//------------------------------------------------------- onSuccessResponse ------------------------------------------------------------

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
                        Utility.someThingIsWrongToaster(getContext());
                        //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        isTrackingSync = true;
                        Log.e(TAG,"Logout Sync Tracking Api status fail / not work");
                        dismissProgressBar();
                        Utility.saveData(requireActivity(),Utility.IS_USER_TRACKING,false);
                    }
                }
                catch (JSONException e) {
                    // Fail
                    Log.e(TAG,"Logout Sync Tracking Api status fail / not work");
                  //  isTrackingSync = true;
                    dismissProgressBar();
                    Utility.someThingIsWrongToaster(getContext());
                    //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                    Utility.saveData(requireActivity(),Utility.IS_USER_TRACKING,false);
                }
            }
            else{
                Utility.someThingIsWrongToaster(getContext());
                Log.e(TAG,"Logout Sync Tracking Api null");
               // isTrackingSync = true;
            }
        }

        // GPS TRACKING UPLOAD DATA
        if (responseCode == URL_Utility.ResponseCode.WS_GPS_TRACKING_UPLOAD) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Logout Sync Gps Tracking Data: "+status);
                    // Success
                    if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)) {
                        if(gpsSyncTrackingModule != null && gpsSyncTrackingModule.getId() != null){
                            if(dataBaseHelper.getGpsTracking().size() > 0){
                                dataBaseHelper.deleteGPSTracking(gpsSyncTrackingModule.getId());
                                Log.e(TAG, "Logout Sync Delete Gps Tracking Data form Local Database: " + gpsSyncTrackingModule.getId());
                             }
                            SyncGPSTrackingDetails();
                        }
                    }
                    // Duplicate
                    else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                        if(gpsSyncTrackingModule != null && gpsSyncTrackingModule.getId() != null){
                            if(dataBaseHelper.getGpsTracking().size() > 0){
                                dataBaseHelper.deleteGPSTracking(gpsSyncTrackingModule.getId());
                                Log.e(TAG, "Logout Sync Delete Duplicate Gps Tracking Data form Local Database: " + gpsSyncTrackingModule.getId());
                            }
                            SyncGPSTrackingDetails();
                        }
                    }
                    // Fail
                    else{
                        // Fail
                      //  isGPSTrackingSync = true;
                        Utility.someThingIsWrongToaster(getContext());
                        // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        dismissProgressBar();
                    }
                }
                catch (JSONException e) {
                   // isGPSTrackingSync = true;
                    Log.e(TAG, e.getMessage());
                    Utility.someThingIsWrongToaster(getContext());
                    // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                    dismissProgressBar();
                }
            }
            else{
                Log.e(TAG,"Logout Syn Gps Tracking Api null");
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                dismissProgressBar();
            }
        }

        // FORM UPLOAD DATA
        if (responseCode == URL_Utility.ResponseCode.WS_LAYER_FORM_UPLOAD) {
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")){
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    if (status.equalsIgnoreCase("Success")){
                        Log.e(TAG, "Logout Sync Form Data: "+ status +" LayerID: "+ formDataModel.getLayerID());
                        Log.e(TAG, formDataModel.getFormData());
                        JSONObject mLoginObj = new JSONObject(formDataModel.getFormData());
                        JSONArray jsonResponse = new JSONArray(mLoginObj.getString("form_data"));
                        Gson gson = new Gson();
                        java.lang.reflect.Type listType = new TypeToken<ArrayList<FormDetailData>>() {}.getType();

                        ArrayList<FormDetailData> listFormDetailsData = gson.fromJson(jsonResponse.toString(), listType);
                        Log.e(TAG, ""+listFormDetailsData.size());
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
                                    new HomeFragment.FileUploadServer(fileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_FILE).execute();
                                }
                                else{
                                    isFileUpload = true;
                                }

                                if (isCamera && !cameraFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Camera");
                                    new HomeFragment.FileUploadServer(cameraFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_CAMERA).execute();
                                }
                                else{
                                    isCameraUpload = true;
                                }
                                if (isVideo && !videoFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Video");
                                    new HomeFragment.FileUploadServer(videoFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_VIDEO).execute();
                                }
                                else{
                                    isVideoUpload = true;
                                }
                                if (isAudio && !audioFileData.isEmpty()) {
                                    Log.e(TAG, "User Selected Audio");
                                    new HomeFragment.FileUploadServer(audioFileData,formDataModel.getFormID(),formDataModel.getUniqueNumber(),TYPE_AUDIO).execute();
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
                    else if(status.equalsIgnoreCase("Duplicate")){
                        if(formDataModel != null && formDataModel.getID() != null){
                            // then
                            if(dataBaseHelper.getProjectFormList().size() > 0){
                                dataBaseHelper.deleteProjectFormDetails(formDataModel.getID());
                                Log.e(TAG, "Logout Sync Delete Form Data: "+ formDataModel.getID());
                            }
                            SyncFormDataDetails();
                        }
                    }
                    else {
                        Log.e(TAG,status);
                      //  isFormSync = true;
                        Utility.someThingIsWrongToaster(getContext());
                        // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        dismissProgressBar();
                    }
                }
                catch (JSONException e) {
                    dismissProgressBar();
                    Utility.someThingIsWrongToaster(getContext());
                    //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                    //isFormSync = true;
                    Log.e(TAG,e.getMessage());
                }
            }
            else{
                Log.e(TAG,res);
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
               // isFormSync = true;
                dismissProgressBar();
            }
        }

        // Online Layer Data
        if(responseCode == URL_Utility.ResponseCode.WS_ONLINE_LAYER){
            String res = AESCrypt.decryptResponse(response);
            if(!res.equals("")) {
                try {
                    JSONObject mObj = new JSONObject(res);
                    String status = mObj.optString("status");
                    Log.e(TAG, "Logout Service Online Layer Status: "+status);
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
                        Log.e(TAG, "Logout Service Online Layer Api status fail / not working");
                        isOnlineLayerSync = true;
                    }
                }
                catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG,"Logout Service Online layer Api not working");
                    isGISCameraSync = true;
                }
            }
            else{
                isOnlineLayerSync = true;
                Log.e(TAG," Logout Service Online Layer Api null");
            }
        }

    }

//------------------------------------------------------- onErrorResponse ------------------------------------------------------------

    @Override
    public void onErrorResponse(URL_Utility.ResponseCode responseCode, VolleyError error) {
        Log.e(TAG, "Error Response Code: "+responseCode +" Error: "+error.getMessage());
        Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
    }

//------------------------------------------------------- Form File Upload to Server ------------------------------------------------------------
    private class FileUploadServer extends AsyncTask<Void, Integer, String> {
    HashMap<String,String> filePathData;
    String form_id;
    String unique_number;
    String type;

    public FileUploadServer(HashMap<String,String> filePathData, String form_id, String unique_number,String type) {
        this.filePathData = filePathData;
        this.form_id = form_id;
        this.unique_number = unique_number;
        this.type = type;

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
                                    }
                                }
                                else {
                                    isFormSync = true;
                                }
                            } else {
                                isFormSync = true;
                                responseString = "Error occurred! Http Status Code: " + statusCode;
                                Log.e(TAG, responseString);
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
        } catch (Exception exception) {
            Log.e(TAG, exception.getMessage());
            isFormSync = true;
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
                }

            } catch (JSONException e) {
                isFormSync = true;
                Log.e(TAG,e.getMessage());
            }
        }
        else{
            Log.e(TAG, response);
            isFormSync = true;
        }
        super.onPostExecute(result);
    }
}

    private class CameraSyncToServe extends AsyncTask<Void, Integer, String> {

        public CameraSyncToServe() {
            isCameraSync = false;
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
                        params.put(URL_Utility.PARAM_LOGIN_TOKEN,Utility.getSavedData(requireContext(),Utility.LOGGED_TOKEN));
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
                                Log.e(TAG,"Logout Sync Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete Duplicate Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Utility.someThingIsWrongToaster(getContext());
                                    ///Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                    //isCameraSync = true;
                                    Log.e(TAG, "Logout Sync Camera Api status fail / not working");
                                    dismissProgressBar();
                                }
                            } catch (JSONException e) {
                                Utility.someThingIsWrongToaster(getContext());
                                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, e.getMessage());
                              //  isCameraSync = true;
                                Log.e(TAG, "Logout Sync Camera Api status fail / not working");
                                dismissProgressBar();
                            }
                        }
                        else{

                            Utility.someThingIsWrongToaster(getContext());
                           // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                          //  isCameraSync = true;
                            Log.e(TAG, "Logout Sync Camera Api status null");
                        }
                    }
                    else {
                        Utility.someThingIsWrongToaster(getContext());
                        // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                      //  isCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                }
            } catch (IOException e) {
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isCameraSync = true;
                responseString = e.toString();
            } catch (Exception exception) {
                //isCameraSync = true;

                Utility.someThingIsWrongToaster(getContext());
                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
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
                        isCameraSync = true;
                        Log.e(TAG, "Logout Sync Camera Off");
                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            //dismissProgressBar();
                            Log.e(TAG, "Logout Sync Data Save Successfully");

                                String date = Utility.getSavedData(requireActivity(), Utility.OLD_DATE);
                                Utility.clearData(requireActivity());
                                Utility.saveData(requireActivity(), Utility.OLD_DATE, date);
                                // Remove All Database Data here!
                                dataBaseHelper.open();
                                dataBaseHelper.logout();
                                dataBaseHelper.close();
                                // Progress Bar
                                dismissProgressBar();
                                startActivity(new Intent(getActivity(), SplashActivity.class));
                        }
                    }
                    else{
                        Utility.someThingIsWrongToaster(getContext());
                        //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                       // isCameraSync = true;
                        Log.e(TAG, "Logout Sync Camera Api status fail / not working");
                        dismissProgressBar();
                    }
                }
                catch (JSONException e){
                    Utility.someThingIsWrongToaster(getContext());
                    // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                   // isCameraSync = true;
                    Log.e(TAG, "Logout Sync Camera Api status fail / not working");
                    dismissProgressBar();
                }
            }
            else{
                Utility.someThingIsWrongToaster(getContext());
                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, response);
              //  isCameraSync = true;
                Log.e(TAG, "Logout Sync Camera Api status null");
                dismissProgressBar();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- TimeLine Sync To Server ------------------------------------------------------------

    private class TimeLineSyncToServe extends AsyncTask<Void, Integer, String> {

        public TimeLineSyncToServe() {
            isTimeLineSync = false;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {}

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
                                Log.e(TAG,"Logout Sync TimeLine Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteTimeLineImage(id);
                                        Log.e(TAG, "Logout Sync Delete TimeLine Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteTimeLineImage(id);
                                        Log.e(TAG, "Logout Sync Delete Duplicate TimeLine Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Utility.someThingIsWrongToaster(getContext());
                                    //  Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                 //   isTimeLineSync = true;
                                    Log.e(TAG, "Logout Sync TimeLine status fail / not working");
                                    dismissProgressBar();
                                }
                            } catch (JSONException e) {
                                Utility.someThingIsWrongToaster(getContext());
                                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                Log.e(TAG,e.getMessage());
                              //  isTimeLineSync = true;
                                Log.e(TAG, "Logout Sync TimeLine status fail / not working");
                                dismissProgressBar();
                            }
                        }
                    }
                    else {
                       // isTimeLineSync = true;
                        Utility.someThingIsWrongToaster(getContext());
                        // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                }
            }
            catch (IOException e) {
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isTimeLineSync = true;
                responseString = e.toString();
            }
            catch (Exception exception) {
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isTimeLineSync = true;
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
                        isTimeLineSync = true;
                        Log.e(TAG, "Logout Sync TimeLine Off");

                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            //dismissProgressBar();
                            Log.e(TAG, "Logout Sync Data Save Successfully");
                            String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                            Utility.clearData(requireActivity());
                            Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                            // Remove All Database Data here!
                            dataBaseHelper.open();
                            dataBaseHelper.logout();
                            dataBaseHelper.close();
                            // Progress Bar
                            dismissProgressBar();
                            startActivity(new Intent(getActivity(), SplashActivity.class));
                        }
                    }
                    else{
                       // isTimeLineSync = true;
                        Utility.someThingIsWrongToaster(getContext());
                        //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Logout Syn TimeLine Api status fail / not working");
                        dismissProgressBar();
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    Utility.someThingIsWrongToaster(getContext());
                    // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                   // isTimeLineSync = true;
                    Log.e(TAG, "Logout Syn TimeLine Api status fail / not working");
                    dismissProgressBar();
                }
            }
            else{
                Log.e(TAG, response);
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                // isTimeLineSync = true;
                Log.e(TAG, "Logout Syn TimeLine Api status null");
                dismissProgressBar();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- Map Camera Sync To Server ------------------------------------------------------------

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
                                Log.e(TAG,"Logout Sync Map Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteMapCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete Duplicate Map Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Utility.someThingIsWrongToaster(getContext());
                                    // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                    //isMapCameraSync = true;
                                    Log.e(TAG, "Logout Sync Map Camera Api status fail / not working");
                                    dismissProgressBar();
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                               // isMapCameraSync = true;
                                Utility.someThingIsWrongToaster(getContext());
                                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Logout Sync Map Camera Api status fail / not working");
                                dismissProgressBar();
                            }
                        }
                        else{
                            Utility.someThingIsWrongToaster(getContext());
                           // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                           // isMapCameraSync = true;
                            Log.e(TAG, "Logout Sync Map Camera Api null");
                            dismissProgressBar();
                        }
                    }
                    else {
                        Utility.someThingIsWrongToaster(getContext());
                       // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                       // isMapCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                }
            } catch (IOException e) {
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isMapCameraSync = true;
                responseString = e.toString();
            } catch (Exception exception) {
               // isMapCameraSync = true;
                Utility.someThingIsWrongToaster(getContext());
                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
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
                        Log.e(TAG, "Logout Sync Map Camera Off");

                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            //dismissProgressBar();
                            Log.e(TAG, "Logout Sync Data Save Successfully");
                            String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                            Utility.clearData(requireActivity());
                            Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                            // Remove All Database Data here!
                            dataBaseHelper.open();
                            dataBaseHelper.logout();
                            dataBaseHelper.close();
                            // Progress Bar
                            dismissProgressBar();
                            startActivity(new Intent(getActivity(), SplashActivity.class));
                        }
                    }
                    else{
                       // isMapCameraSync = true;

                        Utility.someThingIsWrongToaster(getContext());
                        // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Logout Sync Map Camera Api status fail / not working");
                        dismissProgressBar();
                    }

                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    Utility.someThingIsWrongToaster(getContext());
                    // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                  //  isMapCameraSync = true;
                    Log.e(TAG, "Logout Sync Map Camera Api status fail / not working");
                    dismissProgressBar();
                }
            }
            else{
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, response);
                //isMapCameraSync = true;
                Log.e(TAG, "Logout Sync Map Camera Api null");
                dismissProgressBar();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- GIS Camera Sync To Server ------------------------------------------------------------

    private class GISCameraSyncToServe extends AsyncTask<Void, Integer, String> {

        public GISCameraSyncToServe() {
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
                                Log.e(TAG,"Logout Sync GIS Camera Data Status: "+ status + " ID: " + id);
                                if (status.equalsIgnoreCase(URL_Utility.STATUS_SUCCESS)){
                                    // Delete Data one by one!
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteGISMapCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete GIS Camera Data form Local Database: "+ id);
                                    }
                                }
                                else if(status.equalsIgnoreCase(URL_Utility.STATUS_DUPLICATE)){
                                    if(imageList.size() > 0){
                                        imageList.remove(i);
                                        dataBaseHelper.deleteGISMapCameraImage(id);
                                        Log.e(TAG, "Logout Sync Delete Duplicate GIS Camera Data form Local Database: "+ id);
                                    }
                                }
                                else{
                                    Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG," Logout Sync GIS Camera Api status fail / not working");
                                    //isGISCameraSync = true;
                                    dismissProgressBar();
                                }

                            }
                            catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                                dismissProgressBar();
                                Utility.someThingIsWrongToaster(getContext());
                                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                                Log.e(TAG," Logout Sync GIS Camera Api status fail / not working");
                                //isGISCameraSync = true;
                            }
                        }
                        else{
                            Utility.someThingIsWrongToaster(getContext());
                            //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                            dismissProgressBar();
                            Log.e(TAG," Logout Sync GIS Camera Api status fail / not working");
                            //isGISCameraSync = true;
                        }
                    }
                    else {
                        Utility.someThingIsWrongToaster(getContext());
                        //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                       // isGISCameraSync = true;
                        responseString = "Error occurred! Http Status Code: " + statusCode;
                    }
                }
            } catch (IOException e) {
                Utility.someThingIsWrongToaster(getContext());
                // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isGISCameraSync = true;
                responseString = e.toString();
            } catch (Exception exception) {
                Utility.someThingIsWrongToaster(getContext());
                //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                //isGISCameraSync = true;
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
                        isGISCameraSync = true;
                        Log.e(TAG, "Logout Sync Map Camera Off");

                        if(isCameraSync && isFormSync && isGPSTrackingSync && isTrackingSync && isTimeLineSync && isMapCameraSync && isGISCameraSync && isOnlineLayerSync){
                            //dismissProgressBar();
                            Log.e(TAG, "Logout Sync Data Save Successfully");
                            String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
                            Utility.clearData(requireActivity());
                            Utility.saveData(requireActivity(),Utility.OLD_DATE, date);
                            // Remove All Database Data here!
                            dataBaseHelper.open();
                            dataBaseHelper.logout();
                            dataBaseHelper.close();
                            // Progress Bar
                            dismissProgressBar();
                            startActivity(new Intent(getActivity(), SplashActivity.class));
                        }
                    }
                    else{
                        Utility.someThingIsWrongToaster(getContext());
                        //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                        //isGISCameraSync = true;
                        Log.e(TAG,"Logout Sync GIS Camera Api status fail / not working");
                        dismissProgressBar();
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    Utility.someThingIsWrongToaster(getContext());
                    //Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"Logout Sync GIS Camera Api status fail / not working");
                    dismissProgressBar();
                    //isGISCameraSync = true;
                }
            }
            else{
                Utility.someThingIsWrongToaster(getContext());
               // Toast.makeText(baseApplication, "Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, response);
                Log.e(TAG,"Logout Sync GIS Camera Api null");
               /// isGISCameraSync = true;
                dismissProgressBar();
            }
            super.onPostExecute(result);
        }
    }

//------------------------------------------------------- Log out in 24 hr ------------------------------------------------------------

    private void logoutAfter24hr(){

        @SuppressLint("SimpleDateFormat") String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String date = Utility.getSavedData(requireActivity(),Utility.OLD_DATE);
        Log.e(TAG,"Current Date: "+ currentDate);
        Log.e(TAG,"Old Date: "+ date);

        if(!Utility.isEmptyString(date)){

            if(date.equals(currentDate)){
                Log.e(TAG, "true");
            }
            else {
                Log.e(TAG, "false");
                if(SystemPermission.isInternetConnected(requireContext())){
                    showProgressBar();
                    Sync();
                }
            }
        }
        Utility.saveData(requireActivity(),Utility.OLD_DATE, currentDate);

    }

//------------------------------------------------------- on Location ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onNeedLocationPermission() {

    }

    @Override
    public void onExplainLocationPermission() {

    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {
    }

    @Override
    public void onNewLocationAvailable(Location location) {

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }


//------------------------------------------------------- on Resume ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        assistant.start();
    }

//------------------------------------------------------- on pause ---------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPause() {
        assistant.stop();
        super.onPause();
    }



}
